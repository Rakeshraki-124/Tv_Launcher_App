package com.example.my_tv_application

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.example.my_tv_application.Ui_layer.TVLauncherUI
import com.example.my_tv_application.ui.theme.My_Tv_ApplicationTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            My_Tv_ApplicationTheme {
                TVLauncherUI()

            }
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Handled in composable through BackHandler
    }
}

data class AppInfo(
    val name: String,
    val icon: Drawable,
    val packageName: String
)
