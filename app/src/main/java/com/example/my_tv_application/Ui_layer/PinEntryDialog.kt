package com.example.my_tv_application.Ui_layer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PinEntryDialog(
    onCorrectPin: () -> Unit,
    onDismiss: () -> Unit
) {
    val correctPin = "1234" // In production, store securely
    var enteredPin by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.width(500.dp),

            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Enter Parent PIN",
                    style = MaterialTheme.typography.headlineMedium,

                    color = MaterialTheme.colorScheme.onSurface
                )


                // PIN display (dots)
                Text(
                    text = enteredPin.replace(Regex("[0-9]"), "•"),
                    style = MaterialTheme.typography.displayLarge,

                    color = MaterialTheme.colorScheme.onSurface

                )

                // Error message
                if (showError) {
                    Text(
                        text = "Wrong PIN, try again",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }


                // Number pad using manual grid
                NumberPad { number ->
                    if (enteredPin.length < 4) {
                        enteredPin += number.toString()
                        showError = false
                    }
                }


                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            enteredPin = ""
                            showError = false
                        },
                        colors = ButtonDefaults.colors(
                            containerColor = Color(0xFFB00020), // Red
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .width(120.dp)
                            .height(50.dp)
                    ) {
                        Text("CLEAR",style = MaterialTheme.typography.labelLarge,
                            fontSize = 18.sp,
                        )
                    }


                    Button(
                        onClick = {
                            if (enteredPin == correctPin) {
                                onCorrectPin()
                            } else {
                                showError = true
                                enteredPin = ""
                            }
                        },
                        enabled = enteredPin.length == 4,
                        colors = ButtonDefaults.colors(
                            containerColor =  Color(0xFF00FF00), // Teal
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .width(120.dp)
                            .wrapContentHeight()
                    ) {
                        Text("Submit",style = MaterialTheme.typography.bodyLarge,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun NumberPad(onNumberClick: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Rows 1-3 (numbers 1-9)
        for (row in 0..2) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                for (col in 1..3) {
                    val number = row * 3 + col
                    NumberButton(number = number, onClick = { onNumberClick(number) })
                }
            }
        }
        // Row 4 (number 0)
        NumberButton(number = 0, onClick = { onNumberClick(0) })
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun NumberButton(
    number: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(72.dp),
        colors = ButtonDefaults.colors(
            containerColor = Color(0xFF6200EE),
            contentColor = Color.White
        )
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.displaySmall
        )
    }
}