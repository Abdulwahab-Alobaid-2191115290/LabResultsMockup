package com.example.labresultsmockup.ui.composables.filtersmodalalertdialogues
// https://github.com/zj565061763/compose-wheel-picker/tree/master
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.labresultsmockup.ui.composables.wheelpickerpackage.FVerticalWheelPicker
import com.example.labresultsmockup.ui.composables.wheelpickerpackage.rememberFWheelPickerState
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.labresultsmockup.domain.LabResultsViewModel


@Composable
fun YearFilter(modifier: Modifier, onDismiss: () -> Unit, yearFilter: String, yearFilterChange: (String)-> Unit, viewModel: LabResultsViewModel) {

    // Specified initial index
//    val wheelState = rememberFWheelPickerState(2023)

//    LaunchedEffect(wheelState) {
//        delay(1000)
        // Scroll to index.
//        wheelState.animateScrollToIndex(2023)
//    }

    val years = listOf("all") + (viewModel.minYear.collectAsStateWithLifecycle().value  .. viewModel.maxYear.collectAsStateWithLifecycle().value).toList()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Box(Modifier.width(250.dp), contentAlignment = Alignment.Center) {
                Button(modifier = modifier, onClick = {
//                    yearFilterChange(wheelState.currentIndex.toString())
                    onDismiss()
                }) {
                    Text("Confirm")
                }
            }
        },
        dismissButton = {},
        modifier = modifier,
        text = {
            Box(
                Modifier.width(250.dp).height(190.dp),
                contentAlignment = Alignment.Center
            ) {

                LazyColumn(){
                    items(years){ item ->
                        Text(text = item.toString(), fontSize = 19.sp, modifier = Modifier
                            .padding(bottom = 13.dp)
//                            .fillMaxWidth()
                            .clickable{
                                yearFilterChange(item.toString())
                            }
                        )
                    }
                }


//                FVerticalWheelPicker(
//                    modifier = Modifier.width(60.dp),
//                    // Specified item count.
//                    count = 2026,
//                    state = wheelState
//
//                ) { index ->
//                    Text(index.toString())
//                }
            } // this one for the box
        }
    )

}