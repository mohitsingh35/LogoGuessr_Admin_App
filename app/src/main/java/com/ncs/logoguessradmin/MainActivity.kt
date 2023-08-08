package com.ncs.logoguessradmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.ncs.logoguessradmin.firebaseDB.ui.RealtimeScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isInsert = remember {
                mutableStateOf(false)
            }
            var isClicked1 = remember {
                mutableStateOf(true)
            }
            var isClicked2 = remember {
                mutableStateOf(false)
            }
            var isClicked3 = remember {
                mutableStateOf(false)
            }
            val childName = remember {
                mutableStateOf("Newbie")
            }
            Surface (modifier = Modifier.fillMaxSize()){
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(onClick = { isInsert.value=true }) {
                            Icon(Icons.Default.Add , contentDescription = "add" )
                        }
                    }
                ) {paddingValues->
                    Column (modifier = Modifier.padding(paddingValues)){
                        Box (modifier = Modifier.fillMaxWidth().padding(start = 40.dp, end = 40.dp,top=30.dp, bottom = 30.dp)){
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                                Box(modifier = Modifier.height(35.dp).width(85.dp).clip(RoundedCornerShape(22.dp)).background(if (isClicked1.value) Color.Gray else Color.Transparent).border(width = 2.dp, color = Color.Gray, shape = CircleShape).clickable {
                                    childName.value="Newbie"
                                    isClicked1.value=true
                                    isClicked2.value=false
                                    isClicked3.value=false

                                }, contentAlignment = Alignment.Center) {
                                    Text(text = "Newbie", color = if (isClicked1.value) Color.White else Color.Black)
                                }
                                Box(modifier = Modifier.height(35.dp).width(85.dp).clip(RoundedCornerShape(22.dp)).background(if (isClicked2.value) Color.Gray else Color.Transparent).border(width = 2.dp, color = Color.Gray, shape = CircleShape).clickable {
                                    childName.value="Moderate"
                                    isClicked1.value=false
                                    isClicked2.value=true
                                    isClicked3.value=false

                                }, contentAlignment = Alignment.Center){
                                    Text(text = "Moderate", color = if (isClicked2.value) Color.White else Color.Black)
                                }
                                Box(modifier = Modifier.height(35.dp).width(85.dp).clip(RoundedCornerShape(22.dp)).background(if (isClicked3.value) Color.Gray else Color.Transparent).border(width = 2.dp, color = Color.Gray, shape = CircleShape).clickable {
                                    childName.value="Master"
                                    isClicked1.value=false
                                    isClicked2.value=false
                                    isClicked3.value=true
                                }, contentAlignment = Alignment.Center){
                                    Text(text = "Master", color = if (isClicked3.value) Color.White else Color.Black)
                                }
                            }
                        }

                        RealtimeScreen(isInsert, childName = childName.value)

                    }
                }
            }
        }
    }
}



