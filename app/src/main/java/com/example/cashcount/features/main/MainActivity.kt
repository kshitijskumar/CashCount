package com.example.cashcount.features.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cashcount.R
import com.example.cashcount.components.SuccessScreen
import com.example.cashcount.datastore.user.UserDataStore
import com.example.cashcount.features.createaccount.ui.CreateAccountOnboardingScreen
import com.example.cashcount.features.createaccount.ui.CreateAccountScreen
import com.example.cashcount.ui.theme.CashCountTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var data: UserDataStore

    private val viewModel by viewModels<MainNavViewModel>()

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

                    LaunchedEffect(key1 = navController) {
                        handleSideEffects(navController = navController)
                        viewModel.processIntent(MainNavIntent.InitializationIntent)
                    }

                    val inflateNavigation = remember(key1 = state.value.shouldInflateNavigation) {
                        state.value.shouldInflateNavigation
                    }

                    if (inflateNavigation) {
                        val startDestination = when(state.value.startDestinationsType) {
                            MainScreenStartDestinationsType.SETUP_ACCOUNT_SCREEN -> MainNavigationScreens.SetupAccountInfoScreen
                            MainScreenStartDestinationsType.DASHBOARD_SCREEN -> MainNavigationScreens.MainDashboardScreen
                        }

                        NavHost(navController = navController, startDestination = startDestination.routeName) {

                            composable(route = MainNavigationScreens.SetupAccountInfoScreen.routeName) {
                                CreateAccountOnboardingScreen {
                                    scope.launch {
                                        viewModel.processIntent(MainNavIntent.CreateAccountIntent)
                                    }
                                }
                            }

                            composable(route = MainNavigationScreens.CreateAccountScreen.routeName) {
                                CreateAccountScreen(
                                    onBackPressed = {
                                        navController.navigateUp()
                                    },
                                    onAccountCreated = {
                                        scope.launch {
                                            viewModel.processIntent(MainNavIntent.SuccessScreenIntent)
                                        }
                                    }
                                )
                            }
                            
                            composable(route = MainNavigationScreens.SuccessScreen.routeName) {
                                SuccessScreen(
                                    iconResId = R.drawable.ic_success,
                                    message = stringResource(id = R.string.you_are_set),
                                    onScreenClicked = {
                                        scope.launch {
                                            viewModel.processIntent(MainNavIntent.DashboardScreenIntent)
                                        }
                                    }
                                )
                            }

                            composable(route = MainNavigationScreens.MainDashboardScreen.routeName) {
                                Text(text = "dash")
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
                viewModel
                    .effect
                    .collect {
                        when(it) {
                            MainNavSideEffect.NavigateToMainScreen -> {
                                navController.navigate(MainNavigationScreens.MainDashboardScreen.routeName)
                            }
                            MainNavSideEffect.NavigateToCreateAccountScreen -> {
                                navController.navigate(MainNavigationScreens.CreateAccountScreen.routeName)
                            }
                            MainNavSideEffect.NavigateToSuccessScreen -> {
                                navController.navigate(MainNavigationScreens.SuccessScreen.routeName)
                            }
                            MainNavSideEffect.NavigateToDashboardScreen -> {
                                navController.navigate(MainNavigationScreens.MainDashboardScreen.routeName) {
                                    launchSingleTop = true
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
}
