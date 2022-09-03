package com.example.cashcount.features.auth.ui.navigation

sealed class AuthNavigationScreens(val routeName: String) {

    object OnboardingScreen : AuthNavigationScreens("onboardingScreen")
    object LoginScreen : AuthNavigationScreens("loginScreen")
    object VerificationScreen : AuthNavigationScreens("verificationScreen")
    object SetupAccountScreen : AuthNavigationScreens("setupAccountScreen")
}
