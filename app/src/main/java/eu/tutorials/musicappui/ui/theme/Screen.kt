package eu.tutorials.musicappui.ui.theme

import androidx.annotation.DrawableRes
import eu.tutorials.musicappui.R

sealed class Screen(val title: String , val route: String) {
    sealed class DrawerScreen(val dTitle: String , val dRoute : String , @DrawableRes val icon: Int): Screen(dTitle,dRoute){
        object Account:DrawerScreen(
            dTitle = "Account",
            dRoute = "account",
            R.drawable.ic_account
        )
        object Subscription:DrawerScreen(
            dTitle = "Subscription",
            dRoute = "subscription",
            R.drawable.ic_subscribe
        )
        object AddAccount:DrawerScreen(
            dTitle = "Add Account",
            dRoute = "addaccount",
            R.drawable.baseline_person_add_alt_1_24


        )

    }


}
val screensInDrawer = listOf(
    Screen.DrawerScreen.Account,
    Screen.DrawerScreen.Subscription,
    Screen.DrawerScreen.AddAccount
)