package com.thetopheadlines.topnews.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.thetopheadlines.topnews.data.utils.Utils
import com.thetopheadlines.topnews.domain.model.NewsItem
import java.net.URLDecoder
import java.nio.charset.StandardCharsets






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailsScreen(navController: NavHostController, newsJson: String?) {
    val news = newsJson?.let { json ->
        Gson().fromJson(URLDecoder.decode(json, StandardCharsets.UTF_8.toString()), NewsItem::class.java)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "News Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        news?.let {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                it.urlToImage?.let { imageUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "News Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = it.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "By ${it.author ?: "Anonymous"}, ${Utils.formatDate(it.publishedAt)}", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it.description.orEmpty())
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it.content ?: "No content available.")
            }
        }
    }
}
