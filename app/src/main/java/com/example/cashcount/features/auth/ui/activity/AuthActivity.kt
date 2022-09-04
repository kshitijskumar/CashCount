package com.example.cashcount.features.auth.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cashcount.MainActivity
import com.example.cashcount.features.auth.handler.PhoneAuthHandler
import com.example.cashcount.features.auth.handler.PhoneAuthHandlerImpl
import com.example.cashcount.features.auth.ui.login.LoginScreen
import com.example.cashcount.features.auth.ui.navigation.AuthNavIntent
import com.example.cashcount.features.auth.ui.navigation.AuthNavSideEffect
import com.example.cashcount.features.auth.ui.navigation.AuthNavigationScreens
import com.example.cashcount.features.auth.ui.navigation.AuthNavigationViewModel
import com.example.cashcount.features.auth.ui.onboarding.OnboardingScreen
import com.example.cashcount.features.auth.ui.verification.VerificationScreen
import com.example.cashcount.ui.theme.CashCountTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    private val viewModel by viewModels<AuthNavigationViewModel>()

    @Inject lateinit var firebaseAuth: FirebaseAuth

    private val phoneAuthHandler: PhoneAuthHandler by lazy {
        PhoneAuthHandlerImpl(
            activityRef = WeakReference<Activity>(this),
            auth = firebaseAuth
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CashCountTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val scope = rememberCoroutineScope()
                    val state = viewModel.state.collectAsState()
                    val phoneAuthStatus = phoneAuthHandler
                        .getPhoneAuthStatusAsFlow()
                        .collectAsState(initial = null)

                    LaunchedEffect(key1 = navController) {
                        Log.d("AuthActStuff", "launch")
                        handleSideEffects(navController)
                        scope.launch {
                            viewModel.processIntent(AuthNavIntent.CheckIsUserLoggedIn)
                        }
                    }

                    if (state.value.setupNavigation) {
                        NavHost(navController = navController, startDestination = AuthNavigationScreens.OnboardingScreen.routeName) {

                            composable(route = AuthNavigationScreens.OnboardingScreen.routeName) {
                                OnboardingScreen(
                                    onLoginClicked = {
                                        scope.launch {
                                            viewModel.processIntent(AuthNavIntent.NavigateToLoginScreen)
                                        }
                                    }
                                )
                            }

                            composable(route = AuthNavigationScreens.LoginScreen.routeName) {
                                LoginScreen(
                                    phoneAuthStatus = phoneAuthStatus.value,
                                    onBackPressed = {
                                        navController.navigateUp()
                                    },
                                    startPhoneLogin = { phoneNumberWithCountryCode ->
                                        phoneAuthHandler.verifyPhoneNumber(phoneNumberWithCountryCode)
                                    },
                                    navigateToVerificationScreen = {
                                        scope.launch {
                                            viewModel.processIntent(AuthNavIntent.NavigateToVerificationScreen)
                                        }
                                    }
                                )
                            }

                            composable(route = AuthNavigationScreens.VerificationScreen.routeName) {
                                VerificationScreen(
                                    phoneAuthStatus = phoneAuthStatus.value,
                                    verifyCodeEntered = { code ->
                                        phoneAuthHandler.verifyCodeEntered(code)
                                        Toast.makeText(this@AuthActivity, code, Toast.LENGTH_LONG).show()
                                    },
                                    onSignInSuccess = { user ->

                                    },
                                    onBackPressed = {
                                        navController.navigateUp()
                                    }
                                )
                            }

                            composable(route = AuthNavigationScreens.SetupAccountScreen.routeName) {

                            }
                        }
                    }
                }
            }
        }

    }

    private fun handleSideEffects(navController: NavHostController) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect
                    .collect {
                        when(it) {
                            AuthNavSideEffect.NavigateToLoginScreen -> {
                                navController.navigate(AuthNavigationScreens.LoginScreen.routeName)
                            }
                            AuthNavSideEffect.NavigateToMainScreen -> {
                                val intent = Intent(this@AuthActivity, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                startActivity(intent)
                            }
                            AuthNavSideEffect.NavigateToSetupAccountScreen -> {
                                navController.navigate(AuthNavigationScreens.SetupAccountScreen.routeName)
                            }
                            AuthNavSideEffect.NavigateToVerificationScreen -> {
                                navController.navigate(AuthNavigationScreens.VerificationScreen.routeName)
                            }
                        }
                    }
            }
        }
    }
}