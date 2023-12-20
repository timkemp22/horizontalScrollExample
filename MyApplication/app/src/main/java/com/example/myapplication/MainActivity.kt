@file:OptIn(ExperimentalFoundationApi::class)

package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tabs = listOf(
            TabItem("Tab1", Icons.Outlined.Home, Icons.Filled.Home),
            TabItem("Tab2", Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle),
            TabItem("Tab3", Icons.Outlined.Build, Icons.Filled.Build),
            TabItem("Tab4", Icons.Outlined.Email, Icons.Filled.Email),
            TabItem("Tab5", Icons.Outlined.Delete, Icons.Filled.Delete)

        )
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var selectedTabIndex by remember {
                        mutableIntStateOf(0)
                    }
                    val pagerState = rememberPagerState {
                        tabs.size
                    }

                    LaunchedEffect(key1 = selectedTabIndex) {
                        pagerState.animateScrollToPage(selectedTabIndex)
                    }
                    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
                        if (!pagerState.isScrollInProgress) {
                            selectedTabIndex = pagerState.currentPage
                        }
                    }

                    Column(Modifier.fillMaxSize()) {
                        ScrollableTabRow(selectedTabIndex = selectedTabIndex, edgePadding = 5.dp) {
                            tabs.forEachIndexed { index, item ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = { Text(text = item.title) },
                                    icon = {
                                        Icon(
                                            imageVector = if (index == selectedTabIndex) {
                                                item.selectedItem
                                            } else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    },
                                )
                            }
                        }
                        HorizontalPager(
                            state = pagerState, modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) { index ->
                            Subpager({
                                GlobalScope.launch {
                                    var currentpage = pagerState.currentPage
                                    pagerState.animateScrollToPage(currentpage++)
                                }
                            }, {
                                GlobalScope.launch {
                                    var currentPage = pagerState.currentPage
                                    pagerState.animateScrollToPage(currentPage--)
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Subpager(moveToNextSection: () -> Unit, moveToPreviousSection: () -> Unit) {
    val subTabs = mutableListOf<String>("SubTab1", "SubTab2", "SubTab3", "SubTab4", "SubTab5")

    var selectedSubTabIndex by remember {
        mutableIntStateOf(0)
    }
    val subPagerState = rememberPagerState(
        pageCount = { subTabs.size }
    )
    LaunchedEffect(key1 = selectedSubTabIndex) {
        subPagerState.animateScrollToPage(selectedSubTabIndex)
    }
    LaunchedEffect(subPagerState.currentPage, subPagerState.isScrollInProgress) {
        if (!subPagerState.isScrollInProgress) {
            selectedSubTabIndex = subPagerState.currentPage
        }
    }
    Scaffold { paddingValues ->
        Column(
            Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            ScrollableTabRow(selectedTabIndex = selectedSubTabIndex, edgePadding = 0.dp) {
                subTabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = index == selectedSubTabIndex,
                        onClick = { selectedSubTabIndex = index },
                        text = {
                            val fontWeight =
                                if (selectedSubTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            Text(
                                text = tab,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = fontWeight,
                                fontSize = 15.sp,
                                color = Color.Black,
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = subPagerState, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) { index ->
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(text = "This is ${subTabs[index]}")
                }
            }
        }
    }
}

data class TabItem(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedItem: ImageVector
)