package hu.ait.andwallet

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.ait.andwallet.screen.MoneyListScreen
import hu.ait.andwallet.screen.MoneySummaryScreen
import hu.ait.andwallet.ui.theme.MoneyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MoneyAppNavHost()
                }
            }
        }
    }
}

@Composable
fun MoneyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = stringResource(R.string.moneylist)
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable("moneylist") { MoneyListScreen(
            onNavigateToSummary = {income, expense->
                navController.navigate("moneysummary/$expense/$income")
            }
        )}

        composable("moneysummary/{numallincome}/{numallexpense}",
            arguments = listOf(
                navArgument("numallincome"){type = NavType.IntType},
                navArgument("numallexpense"){type = NavType.IntType})
        ) {

            val numallincome = it.arguments?.getInt(stringResource(R.string.numallincome))
            val numallexpense = it.arguments?.getInt(stringResource(R.string.numallexpense))

            if (numallincome != null && numallexpense != null) {
                MoneySummaryScreen(
                    numallincome = numallincome,
                    numallexpense = numallexpense
                )
            }
        }
    }
}