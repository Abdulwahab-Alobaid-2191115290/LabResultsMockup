package com.example.labresultsmockup.ui.composables.filtersmodalalertdialogues

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.labresultsmockup.domain.LabResultsViewModel

@Composable
fun PlaceFilter(modifier: Modifier, onDismiss: () -> Unit, placeFilter: String, placeFilterChange: (String)-> Unit, viewModel: LabResultsViewModel ){
    val places = viewModel.uniqueHospitalNames.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        modifier = modifier,
        text = {
            Column(
                modifier,
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ){
                places.value?.forEach {
                    Text(it, modifier = Modifier.clickable{
                        placeFilterChange(it)
                        onDismiss()
                    })
                }

            }
        }
    )
}