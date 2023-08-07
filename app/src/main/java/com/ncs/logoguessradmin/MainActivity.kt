package com.ncs.logoguessradmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
            Surface (modifier = Modifier.fillMaxSize()){
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(onClick = { isInsert.value=true }) {
                            Icon(Icons.Default.Add , contentDescription = "add" )
                        }
                    }
                ) {paddingValues->
                    Box (modifier = Modifier.padding(paddingValues)){
                        RealtimeScreen(isInsert)

                    }
                }
            }

        }
    }
}

