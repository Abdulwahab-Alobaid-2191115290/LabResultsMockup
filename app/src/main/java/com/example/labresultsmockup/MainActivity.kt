package com.example.labresultsmockup

import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.labresultsmockup.domain.LabResultsViewModel
import com.example.labresultsmockup.ui.screens.LabResults
import com.example.labresultsmockup.ui.theme.LabResultsMockupTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WebView.setWebContentsDebuggingEnabled(true)


        enableEdgeToEdge()
        setContent {
            LabResultsMockupTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenSetup(
                        modifier = Modifier.padding(innerPadding)
                        .background(Color(0xfff5f8ff))

                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ScreenSetup(modifier: Modifier){

    val viewModel = LabResultsViewModel()
    val context = LocalContext.current

    viewModel.setEMRList(context)

    MainScreen(modifier, viewModel)
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(modifier: Modifier, viewModel: LabResultsViewModel) {

    LabResults(modifier, viewModel)


}



