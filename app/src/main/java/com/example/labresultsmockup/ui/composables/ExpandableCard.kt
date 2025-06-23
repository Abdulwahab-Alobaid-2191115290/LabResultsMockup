package com.example.labresultsmockup.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.labresultsmockup.domain.LabResultsViewModel
import androidx.compose.material3.Icon
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import com.example.labresultsmockup.models.EMRItem


@Composable
fun ExpandableCard(modifier: Modifier, hospitalNameDT: Pair<String, String>, viewModel: LabResultsViewModel,
                   openWebView: Boolean,
                   webViewUrl: String,
                   setOpenWebView: (Boolean)->Unit,
                   setWebViewUrl: (String)->Unit

) {

    var expanded by remember { mutableStateOf (false) }
    val rotationAngle by animateFloatAsState(targetValue = if (expanded) 0f else -90f)


    val localUriHandler = LocalUriHandler.current

    val matchingFilteredEMRs = viewModel.filteredEMRList.collectAsStateWithLifecycle().value?.filter {
        it.HealthCareFacilityNameEN == hospitalNameDT.first && it.RequestDT == hospitalNameDT.second
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .clickable(
                onClick ={
                    expanded = !expanded
                }
            )
            .animateContentSize()
//            .background(Color.White)
//            .clip(RoundedCornerShape(8.dp)),


    ) {
        Column(
            Modifier.padding(start = 13.dp, end = 13.dp)
        ) {
            Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = hospitalNameDT.first,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.width(23.dp))

                Box(
                    modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = hospitalNameDT.second.slice(0..9),
                            modifier = Modifier.padding(8.dp),
                            fontSize = 13.sp,
                            color = Color.Gray
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand arrow",
                            modifier = Modifier.rotate(rotationAngle),
                            tint = Color.LightGray
                        )
                    }
                }

            }
            if (expanded) {
                    if (matchingFilteredEMRs != null) {
                        Column() {
                            Divider(color = Color.LightGray, modifier = Modifier.padding(bottom = 30.dp))
                            matchingFilteredEMRs.forEachIndexed {idx, item ->
                                Row(
                                    modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier.weight(0.5f)) {
                                        Text(
                                            item.ProcedureName,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 0.dp)
                                        )
                                        Text(item.RequestDT, fontSize = 13.sp, color = Color.Gray)
                                    }
                                    if (item.ProcedureStatus == "R") {
                                        Row(modifier, verticalAlignment = Alignment.CenterVertically){
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                tint = Color(0xFF3bb143),
                                                modifier = Modifier.size(17.dp).padding(end = 3.dp),
                                                contentDescription = null
                                            )
                                            Text(color = Color(0xFF3bb143), text = "Ready", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Text("Not Ready")
                                    }
                                }

                                Spacer(Modifier.height(15.dp))
                                if(idx != matchingFilteredEMRs.size - 1) {
                                    Divider(color = Color.LightGray)
                                }
                                Spacer(Modifier.height(15.dp))
                            }

                            Button(
                                modifier = modifier.fillMaxWidth().height(51.dp),
                                shape = RoundedCornerShape(13.dp),
                                onClick = {
                                    // redirect to chrome
//                                    localUriHandler.openUri(matchingFilteredEMRs[0].ResultURL)

                                    // webView
                                    setOpenWebView(true)
                                    setWebViewUrl(matchingFilteredEMRs[0].ResultURL)

                                }
                            ) {
                                Text(text = "View Report", fontSize = 17.sp)
                            }

                        }
                    }
                }
            }
        }
    }