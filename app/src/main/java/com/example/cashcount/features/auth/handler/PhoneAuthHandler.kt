package com.example.cashcount.features.auth.handler

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

interface PhoneAuthHandler {

    fun verifyPhoneNumber(phoneNumber: String): Boolean

    fun getPhoneAuthStatusAsFlow(): Flow<PhoneAuthStatus>

    fun verifyCodeEntered(code: String)

}

class PhoneAuthHandlerImpl(
    private val activityRef: WeakReference<Activity>,
    private val auth: FirebaseAuth,
) : PhoneAuthHandler {

    private val phoneAuthStatus = Channel<PhoneAuthStatus>(Channel.BUFFERED)

    private var verificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    private val callback by lazy {
        getPhoneAuthCallback()
    }

    override fun verifyPhoneNumber(phoneNumber: String): Boolean {
        return activityRef.get()?.let { activity ->
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callback)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
            true
        } ?: false
    }

    override fun getPhoneAuthStatusAsFlow(): Flow<PhoneAuthStatus> {
        return phoneAuthStatus.receiveAsFlow()
    }

    override fun verifyCodeEntered(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        activityRef.get()?.let { activity ->
            auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        Log.d(this@PhoneAuthHandlerImpl.javaClass.simpleName, "sign in success")
                        val user = task.result?.user
                        phoneAuthStatus.trySend(PhoneAuthStatus.VerificationCompleted(user))
                    } else {
                        Log.d(this@PhoneAuthHandlerImpl.javaClass.simpleName, "sign in failure")
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            phoneAuthStatus.trySend(
                                PhoneAuthStatus.VerificationFailed(
                                    PhoneAuthFailureReason.INVALID_OTP
                                )
                            )
                        } else {
                            phoneAuthStatus.trySend(
                                PhoneAuthStatus.VerificationFailed(
                                    PhoneAuthFailureReason.UNKNOWN_ERROR
                                )
                            )
                        }
                    }
                }
        } ?: kotlin.run {
            Log.d(this@PhoneAuthHandlerImpl.javaClass.simpleName, "sign in dail activity null")
            phoneAuthStatus.trySend(PhoneAuthStatus.VerificationFailed(PhoneAuthFailureReason.UNKNOWN_ERROR))
        }
    }

    private fun getPhoneAuthCallback(): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(this@PhoneAuthHandlerImpl.javaClass.simpleName, "verification completed")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d(this@PhoneAuthHandlerImpl.javaClass.simpleName, "failed: $e")

                val reason = if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    PhoneAuthFailureReason.INVALID_REQUEST
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    PhoneAuthFailureReason.QUOTA_EXCEEDED
                } else {
                    PhoneAuthFailureReason.UNKNOWN_ERROR
                }

                phoneAuthStatus.trySend(PhoneAuthStatus.VerificationFailed(reason))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(this@PhoneAuthHandlerImpl.javaClass.simpleName, "onCodeSend: $verificationId")

                this@PhoneAuthHandlerImpl.verificationId = verificationId
                this@PhoneAuthHandlerImpl.resendToken = token

                phoneAuthStatus.trySend(PhoneAuthStatus.CodeSent)
            }
        }
    }
}

sealed class PhoneAuthStatus {

    data class VerificationCompleted(val user: FirebaseUser?) : PhoneAuthStatus()
    data class VerificationFailed(val reason: PhoneAuthFailureReason) : PhoneAuthStatus()
    object CodeSent : PhoneAuthStatus()
}

enum class PhoneAuthFailureReason {
    INVALID_REQUEST,
    QUOTA_EXCEEDED,
    UNKNOWN_ERROR,
    INVALID_OTP
}