package com.example.cashcount.features.main

sealed class MainNavigationScreens(val routeName: String) {

    object SetupAccountInfoScreen : MainNavigationScreens("setupAccountInfo")

    object  CreateAccountScreen: MainNavigationScreens("createAccount")

    object SuccessScreen : MainNavigationScreens("success")

    object MainDashboardScreen : MainNavigationScreens("mainDashboard")

}
