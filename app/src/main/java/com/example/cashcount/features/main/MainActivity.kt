package com.example.cashcount.features.main

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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cashcount.datastore.user.UserDataStore
import com.example.cashcount.datastore.user.models.UserDataStoreModel
import com.example.cashcount.features.createaccount.ui.CreateAccountOnboardingScreen
import com.example.cashcount.features.createaccount.ui.CreateAccountScreen
import com.example.cashcount.ui.theme.CashCountTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
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
                                        Toast.makeText(this@MainActivity, "account created", Toast.LENGTH_LONG).show()
                                    }
                                )
                            }

                            composable(route = MainNavigationScreens.MainDashboardScreen.routeName) {

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
                        }
                    }
            }
        }
    }
}
