package com.example.cashcount.features.main

sealed class MainNavigationScreens(val routeName: String) {

    object SetupAccountInfoScreen : MainNavigationScreens("setupAccountInfo")

    object MainDashboardScreen : MainNavigationScreens("mainDashboard")

}
