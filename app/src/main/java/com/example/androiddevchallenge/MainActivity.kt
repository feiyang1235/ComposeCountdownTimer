/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme(darkTheme = false) {
                MyApp(
                    viewModel,
                    onTextFieldChanged = { viewModel.onTextFieldChanged(it) },
                    onStateChanged = { viewModel.onStateChanged(it) })
            }
        }
    }
}


// Start building your app here!
@Composable
fun MyApp(
    viewModel: MainViewModel = MainViewModel(),
    onTextFieldChanged: (String) -> Unit = {},
    onStateChanged: (BoxState) -> Unit = {}
) {
    val number: String by viewModel.number.observeAsState("")
    val state: BoxState by viewModel.state.observeAsState(BoxState.EDITABLE_STATE)
    Surface(color = MaterialTheme.colors.background) {
        Crossfade(targetState = state) { _state ->
            when (_state) {
                BoxState.EDITABLE_STATE -> EditableLayout(onTextFieldChanged, onStateChanged)
                BoxState.COUNTDOWN_STATE -> CountDownLayout(number,onTextFieldChanged, onStateChanged)
            }
        }
    }
}

@Composable
fun EditableLayout(onTextFieldChanged: (String) -> Unit, onStateChanged: (BoxState) -> Unit) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            modifier = Modifier.padding(bottom = 30.dp),
            onValueChange = {
                text = it
            },
            label = { Text("Countdown Timer(Unit: second)") }
        )
        Button(onClick = {
            onTextFieldChanged(text.text)
            onStateChanged(BoxState.COUNTDOWN_STATE)
        }) {
            Text(
                "Start", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }
    }
}

private var mHandler = Handler(Looper.getMainLooper())

@Composable
fun CountDownLayout(
    number: String,
    onTextFieldChanged: (String) -> Unit,
    onStateChanged: (BoxState) -> Unit
) {
    mHandler.removeCallbacksAndMessages(null)
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        var buttonName by remember{ mutableStateOf("PAUSE")}
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(modifier = Modifier.padding(bottom = 16.dp),text = (number.toInt()).toString(),style = TextStyle(fontWeight = FontWeight.Bold,fontSize = 50.sp))
            Button(onClick = { if (buttonName == "PAUSE"){
                buttonName = "RESTART"
                mHandler.removeCallbacksAndMessages(null)
            }else{
                buttonName = "PAUSE"
                mHandler.postDelayed({
                    if (number.toInt() == 1){
                        onStateChanged(BoxState.EDITABLE_STATE)
                    }else {
                        onTextFieldChanged((number.toInt() - 1).toString())
                    }
                }, 1000)
            } }) {
                Text(text = buttonName,style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp))
            }
        }
    }
    mHandler.postDelayed({
        if (number.toInt() == 1){
            onStateChanged(BoxState.EDITABLE_STATE)
        }else {
            onTextFieldChanged((number.toInt() - 1).toString())
        }
    }, 1000)
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
