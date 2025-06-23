package com.example.labresultsmockup.ui.composables

import android.app.appsearch.SearchResults
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labresultsmockup.domain.LabResultsViewModel


@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(modifier: Modifier, viewModel: LabResultsViewModel) {
    var textState by remember { mutableStateOf("") }
    var activeState by remember { mutableStateOf(false) }
    var searchHistoryList = remember { mutableStateListOf<String>() }

    SearchBar(
        modifier = modifier.width(70.dp),
        query = textState,
        onQueryChange = {textState = it},
        onSearch = {
            activeState = false
            searchHistoryList.add(textState)
            viewModel.setFilteredEMRList(textState)
            textState = ""
        },
        active = activeState,
        onActiveChange = {
//            activeState = it
        },
        placeholder = { Text("Search for a lab result", fontSize = 15.sp) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        trailingIcon = {
            if(activeState){
                Icon(
                    modifier = Modifier.clickable{
                        if(textState.isEmpty()){
                            activeState = false
                        }
                        textState = ""
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Icon"
                )
            }
        },
        shape = RoundedCornerShape(13.dp)
    ){
        searchHistoryList.forEach {
            Row(Modifier.padding(13.dp)){
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.History,
                    contentDescription = "History Icon"
                )
                Text(it)
            }
        }

    }

}