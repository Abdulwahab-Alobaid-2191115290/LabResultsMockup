package com.example.labresultsmockup.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.labresultsmockup.domain.LabResultsViewModel
import com.example.labresultsmockup.ui.composables.ExpandableCard
import com.example.labresultsmockup.ui.composables.SimpleSearchBar
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import com.example.labresultsmockup.ui.composables.FiltersModal
import com.example.labresultsmockup.ui.composables.WebViewComposable


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun LabResults(modifier: Modifier, viewModel: LabResultsViewModel){

    // modal stuff
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
    val setOpenBottomSheet: (Boolean)-> Unit = {openBottomSheet = it}


    Scaffold(
        topBar = {TopBarLabResults(
            modifier
                .padding(vertical = 23.dp, horizontal = 13.dp),
            viewModel,
            openBottomSheet,
            skipPartiallyExpanded,
            setOpenBottomSheet
        )},
        content = {it ->
            ContentLabResults(
                Modifier.background(Color(0xfff5f8ff)).padding(
                    start = it.calculateStartPadding(LocalLayoutDirection.current),
                    end = it.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = it.calculateBottomPadding(),
                    top = it.calculateTopPadding() - 45.dp
                    ),
                viewModel,
                openBottomSheet,
                skipPartiallyExpanded,
                setOpenBottomSheet
            )

        },
        bottomBar = {}
    )

}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun TopBarLabResults(modifier: Modifier, viewModel: LabResultsViewModel, openBottomSheet: Boolean, skipPartiallyExpanded: Boolean, setOpenBottomSheet: (Boolean)-> Unit){

    Column(
        modifier.padding(top = 0.dp),
    ){
        Text(
            text = "Lab Results",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp),
            color = Color.Black
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.Top
        ){
            SimpleSearchBar(Modifier.weight(0.5f), viewModel)
            Spacer(Modifier.width(13.dp))
            Box(
                Modifier.wrapContentSize()
                    .padding(top = 43.dp)
                    .background(color = Color(0xffd0e7fe), shape = RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(modifier = Modifier.size(54.dp), onClick = {setOpenBottomSheet(true)}) {
                    Icon( imageVector =  Icons.Filled.Cable, contentDescription = null, tint = Color(0xff1a5da0))
                }
            }
        }

    }
}

@Composable
fun ContentLabResults(modifier: Modifier, viewModel: LabResultsViewModel, openBottomSheet: Boolean, skipPartiallyExpanded: Boolean, setOpenBottomSheet: (Boolean)-> Unit){
    val uniqueHospitalNameDT = viewModel.uniqueHospitalNameDT.collectAsStateWithLifecycle()

    var openWebView by rememberSaveable { mutableStateOf(false) }
    var webViewUrl by rememberSaveable { mutableStateOf("") }

    val setOpenWebView: (Boolean)->Unit = {openWebView = it}
    val setWebViewUrl: (String)->Unit = {webViewUrl = it}

    FiltersModal(Modifier, openBottomSheet, skipPartiallyExpanded, setOpenBottomSheet, viewModel)

    if(openWebView){
        Box(modifier.padding(top = 0.dp).fillMaxWidth().height(600.dp).border(width = 3.dp, color = Color.Black )){
            WebViewComposable(webViewUrl)
//            WebViewComposable("https://httpbin.org/get?param=test/with/slash")
        }
    }
    else {

        LazyColumn(modifier.padding(top = 0.dp)) {
//        stickyHeader {
//            Button(
//                modifier = Modifier,
//                onClick = { setOpenBottomSheet(!openBottomSheet) },
//            ) {
//                Text(text = "Show Bottom Sheet")
//            }
//
//        }
            items(uniqueHospitalNameDT.value ?: emptyList<String>()) { item ->

                ExpandableCard(Modifier, item as Pair<String, String>, viewModel, openWebView, webViewUrl, setOpenWebView, setWebViewUrl )

            }
        }
    }
}
