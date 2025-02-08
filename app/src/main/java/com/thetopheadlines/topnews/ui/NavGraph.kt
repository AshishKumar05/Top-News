package com.thetopheadlines.topnews.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thetopheadlines.topnews.ui.screens.NewsDetailsScreen
import com.thetopheadlines.topnews.ui.screens.NewsListingScreen

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(navHostController, startDestination = "news_listing_page"){
        composable("news_listing_page"){
            NewsListingScreen(navHostController)
        }
        composable("news_detail_page/{news}"){ backStackEntry ->
            val newsJson = backStackEntry.arguments?.getString("news")
            NewsDetailsScreen(navHostController, newsJson)
        }
    }
}