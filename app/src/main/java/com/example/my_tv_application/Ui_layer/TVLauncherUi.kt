package com.example.my_tv_application.Ui_layer

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.my_tv_application.AppInfo

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TVLauncherUI() {
    val context = LocalContext.current
    var apps by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    var showPinDialog by remember { mutableStateOf(false) }
    var currentFocusedIndex by remember { mutableStateOf(-1) }

    BackHandler {
        showPinDialog = true
    }

    LaunchedEffect(Unit) {
        apps = getApprovedApps(context)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFFD166))) {
        if (apps.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading apps...", fontSize = 20.sp, color = Color.Black)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(apps) { index, app ->
                    val isFocused = currentFocusedIndex == index
                    AppItem(
                        app = app,
                        isFocused = isFocused,
                        onFocusChanged = { focused ->
                            if (focused) currentFocusedIndex = index
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        // Exit button with PIN protection
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
                .size(80.dp)
                .background(
                    color = Color(0xFF6200EE),
                    shape = CircleShape
                )
                .border(
                    BorderStroke(4.dp, Color(0xFF03DAC6)),
                    shape = CircleShape
                )
                .clickable { showPinDialog = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "EXIT",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        if (showPinDialog) {
            PinEntryDialog(
                onCorrectPin = {
                    showPinDialog = false
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                },
                onDismiss = { showPinDialog = false }
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AppItem(
    app: AppInfo,
    isFocused: Boolean,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val backgroundColor by animateColorAsState(
        targetValue = if (isFocused) Color(0xFF03DAC6) else Color(0xFFFFF3E0),
        animationSpec = tween(durationMillis = 150),
        label = "backgroundColor"
    )
    val textColor by animateColorAsState(
        targetValue = if (isFocused) Color.Black else Color.Black,
        label = "textColor"
    )
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "scale"
    )

    Column(
        modifier = modifier
            .scale(scale)
            .focusable()
            .onFocusChanged { onFocusChanged(it.isFocused) }
            .size(180.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { launchApp(app.packageName, context) }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(app.icon)
                .crossfade(true)
                .build(),
            contentDescription = app.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Text(
            text = app.name,
            color = textColor,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
fun launchApp(packageName: String, context: Context) {
    val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
    launchIntent?.let {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(it)
    }
}