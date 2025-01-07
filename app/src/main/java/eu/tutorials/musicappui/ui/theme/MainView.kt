package eu.tutorials.musicappui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.tutorials.musicappui.MainViewModel
import eu.tutorials.musicappui.R
import eu.tutorials.musicappui.Screen
import eu.tutorials.musicappui.screenInBottomBar
import eu.tutorials.musicappui.screensInDrawer
import eu.tutorials.musicappui.ui.theme.AccountDialog
import eu.tutorials.musicappui.ui.theme.AccountView
import eu.tutorials.musicappui.ui.theme.Browse
import eu.tutorials.musicappui.ui.theme.Home
import eu.tutorials.musicappui.ui.theme.Library
import eu.tutorials.musicappui.ui.theme.Purple80
import eu.tutorials.musicappui.ui.theme.Subscription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainView() {

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    // Allow us to find out on which "View" we current are
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val dialogOpen = remember {
        mutableStateOf(false)
    }

    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    var title = remember {
        mutableStateOf(currentScreen.title)
    }

    val isSheetFullScreen by remember {
        mutableStateOf(false)
    }

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded })

    val roundedCornerRadius = if (isSheetFullScreen) 0.dp else 12.dp

    val modifier = if (isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()

    val bottomBar: @Composable () -> Unit = {
        if (currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomBarScreen.Home) {
            BottomNavigation(Modifier.wrapContentSize() , backgroundColor = Purple80) {
                screenInBottomBar.forEach { item ->
                    val selected = currentRoute == item.bRoute
                    val tint = if (selected) Color.White else Color.Black
                    BottomNavigationItem(
                        selected = selected,
                        onClick = { controller.navigate(item.bRoute)
                                  title.value = item.bTitle}, icon = {
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = item.bTitle,
                                tint = tint
                            )
                        },
                        label = { Text(text = item.bTitle, color = tint) },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black
                    )
                }
            }
        }

    }

    ModalBottomSheetLayout(sheetContent = { sheetContent(modifier = modifier) },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = roundedCornerRadius,
            topEnd = roundedCornerRadius
        )) {
        Scaffold(
            bottomBar = bottomBar,
            topBar = {
                TopAppBar(
                    title = { Text(title.value) },
                    actions = {
                        IconButton(onClick = {scope.launch {
                            if(modalBottomSheetState.isVisible)
                                modalBottomSheetState.hide()
                            else
                                modalBottomSheetState.show()
                        }
                        }){
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null, tint = Color.Black)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            // Open the drawer
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(Purple80)
                    )
            }, scaffoldState = scaffoldState,
            drawerContent = {
                LazyColumn(Modifier.padding(16.dp)) {
                    items(screensInDrawer) { item ->
                        DrawerItem(selected = currentRoute == item.dRoute, item = item) {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                            if (item.dRoute == "add_account") {
                                dialogOpen.value = true
                            } else {
                                controller.navigate(item.dRoute)
                                title.value = item.dTitle
                            }
                        }
                    }
                }
            }

        ) {
            Navigation(navController = controller, viewModel = viewModel, pd = it)

            AccountDialog(dialogOpen = dialogOpen)

        }

    }
}

@Composable
fun DrawerItem(
    selected: Boolean,
    item: Screen.DrawerScreen,
    onDrawerItemClicked: () -> Unit
) {
    val background = if (selected) Color.DarkGray else Color.Black
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clickable {
                onDrawerItemClicked()
            }) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.dTitle,
            Modifier.padding(end = 8.dp, top = 4.dp),
            tint = background
        )
        Text(
            text = item.dTitle,
            style = MaterialTheme.typography.h5,
        )
    }
}

@Composable

fun sheetContent(modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Purple80)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.baseline_settings_25),
                    contentDescription = "Settings"
                )
                Text(text = "Settings", fontSize = 20.sp, color = Color.White)
            }
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.baseline_share_25),
                    contentDescription = "Share"
                )
                Text(text = "Share", fontSize = 20.sp, color = Color.White)
            }
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.baseline_help_outline_25),
                    contentDescription = "Help"
                )
                Text(text = "Help", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, pd: PaddingValues) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.DrawerScreen.Account.route, modifier = Modifier.padding(pd)
    ) {
        composable(Screen.BottomBarScreen.Home.bRoute) {
            Home()
        }
        composable(Screen.BottomBarScreen.Library.bRoute) {
            Library()
        }
        composable(Screen.BottomBarScreen.Search.bRoute) {
            Browse()
        }
        composable(Screen.DrawerScreen.Account.route) {
            AccountView()
        }
        composable(Screen.DrawerScreen.Subscription.route) {
            Subscription()
        }
    }

}