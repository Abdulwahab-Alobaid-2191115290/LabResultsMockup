package com.example.labresultsmockup.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.labresultsmockup.domain.LabResultsViewModel
import com.example.labresultsmockup.ui.composables.filtersmodalalertdialogues.PlaceFilter
import com.example.labresultsmockup.ui.composables.filtersmodalalertdialogues.TestFilter
import com.example.labresultsmockup.ui.composables.filtersmodalalertdialogues.YearFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersModal(
    modifier: Modifier,
    openBottomSheet: Boolean,
    skipPartiallyExpanded: Boolean,
    setOpenBottomSheet: (Boolean) -> Unit,
    viewModel: LabResultsViewModel
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)


    // Sheet content
    if (openBottomSheet) {

        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = { setOpenBottomSheet(false) },
            sheetState = bottomSheetState,
            dragHandle = null,
//            containerColor = Color.Transparent,
            shape = RoundedCornerShape(
                topStart = 13.dp,
                topEnd = 13.dp
            )
        ) {

//            Box(Modifier.fillMaxHeight(0.936f)) {
                ModalContent(Modifier, viewModel)
//            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalContent(
    modifier: Modifier,
    viewModel: LabResultsViewModel
) {

    var yearFilter = viewModel.yearFilter.collectAsStateWithLifecycle()
    val yearFilterChange: (String) -> Unit = {viewModel.updateYearFilter(it)}

    var testFilter = viewModel.testFilter.collectAsStateWithLifecycle()
    val testFilterChange: (String) -> Unit = {viewModel.updateTestFilter(it)}

    var placeFilter = viewModel.placeFilter.collectAsStateWithLifecycle()
    val placeFilterChange: (String) -> Unit = {viewModel.updatePlaceFilter(it)}

    var showYearFilter by rememberSaveable { mutableStateOf(false) }
    var showTestFilter by rememberSaveable { mutableStateOf(false) }
    var showPlaceFilter by rememberSaveable { mutableStateOf(false) }

    val uniqueTestNames = viewModel.uniqueTestNames.collectAsStateWithLifecycle()
    val uniqueHospitalNames = viewModel.uniqueHospitalNames.collectAsStateWithLifecycle()


    Box(
        modifier.fillMaxSize().padding(30.dp)
    ){

        Column(modifier,
            verticalArrangement = Arrangement.spacedBy(23.dp)
        ) {

            when{
                showYearFilter  -> YearFilter(modifier, {showYearFilter = false}, yearFilter.value, yearFilterChange, viewModel)
                showTestFilter  -> TestFilter(modifier, {showTestFilter = false}, testFilter.value, testFilterChange, viewModel)
                showPlaceFilter -> PlaceFilter(modifier, {showPlaceFilter = false}, placeFilter.value, placeFilterChange, viewModel)
            }

                OutlinedTextField(
                    modifier = modifier.clickable{showYearFilter = true},
                    value = if (yearFilter.value == "all") "All" else yearFilter.value,
                    onValueChange = { viewModel.updateYearFilter(it) },
                    placeholder = { Text(yearFilter.value) },
                    label = { Text("Year") },
                    readOnly = true,
                    enabled = false,
                    leadingIcon = {Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Year")},
                    colors = OutlinedTextFieldDefaults.colors().copy(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        //For Icons
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

            OutlinedTextField(
                modifier = modifier.clickable{showTestFilter = true},
                value = if (testFilter.value == "all") "All" else testFilter.value,
                onValueChange = {viewModel.updateTestFilter(it)},
                placeholder = {Text(testFilter.value)},
                label = {Text("Test")},
                readOnly = true,
                enabled = false,
                leadingIcon =  {Icon(imageVector = Icons.Default.Search, contentDescription = "Test")},
                colors = OutlinedTextFieldDefaults.colors().copy(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    //For Icons
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )


            )

            OutlinedTextField(
                modifier = modifier.clickable{showPlaceFilter = true},
                value = if (placeFilter.value == "all") "All" else placeFilter.value,
                onValueChange = {viewModel.updatePlaceFilter(it)},
                placeholder = {Text(placeFilter.value)},
                label = {Text("Place")},
                readOnly = true,
                leadingIcon =  {Icon(imageVector = Icons.Default.Warehouse, contentDescription = "Place")},
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors().copy(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    //For Icons
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )

            )

        }
    }
}


