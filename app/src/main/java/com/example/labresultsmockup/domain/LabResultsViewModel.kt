package com.example.labresultsmockup.domain

import android.content.Context
import android.util.Log
import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import com.example.labresultsmockup.R
import com.example.labresultsmockup.models.EMRItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer
import java.nio.charset.StandardCharsets

class LabResultsViewModel: ViewModel() {

    private val TAG = "MYLOG"
    private val _EMRList = MutableStateFlow<List<EMRItem>?>(null)

    private val _filteredEMRList = MutableStateFlow<List<EMRItem>?>(null)
    val filteredEMRList: StateFlow<List<EMRItem>?> = _filteredEMRList

    private val _uniqueHospitalNames = MutableStateFlow<MutableList<String>?>(null)
    val uniqueHospitalNames: MutableStateFlow<MutableList<String>?> =  _uniqueHospitalNames

    private val _uniqueHospitalNameDT = MutableStateFlow<MutableList<Pair<String, String>>?>(null)
    val uniqueHospitalNameDT: MutableStateFlow<MutableList<Pair<String, String>>?> = _uniqueHospitalNameDT

    private val _uniqueTestNames = MutableStateFlow<MutableList<String>?>(null)
    val uniqueTestNames: MutableStateFlow<MutableList<String>?> = _uniqueTestNames

    private val _yearFilter = MutableStateFlow<String>("all")
    val yearFilter: MutableStateFlow<String> = _yearFilter

    private val _testFilter = MutableStateFlow<String>("all")
    val testFilter: MutableStateFlow<String> = _testFilter

    private val _placeFilter = MutableStateFlow<String>("all")
    val placeFilter: MutableStateFlow<String> = _placeFilter

    private val _minYear = MutableStateFlow<Int>(0)
    val minYear: MutableStateFlow<Int> = _minYear

    private val _maxYear = MutableStateFlow<Int>(0)
    val maxYear: MutableStateFlow<Int> = _maxYear


    fun setEMRList(context: Context){
        var jsonEMR = readJSONFromRaw(context, R.raw.emr)

        val jsonHospitalEMR: List<EMRItem> = Json.decodeFromString<List<EMRItem>>(jsonEMR as String)
        val hospitalEMR = getUniqueHospital(jsonHospitalEMR)
        val hospitalEMRDT = getUniqueHospitalDT(jsonHospitalEMR)
        val testNames = getUniqueTestsString(jsonHospitalEMR)

        _EMRList.value = jsonHospitalEMR
        _filteredEMRList.value = _EMRList.value
        _uniqueHospitalNames.value = hospitalEMR
        _uniqueHospitalNameDT.value = hospitalEMRDT
        _uniqueTestNames.value = testNames

        setYearMinMax()

    }

    fun setFilteredEMRList(filter: String = ""){

        _filteredEMRList.value = _EMRList.value?.filter { item ->
            (yearFilter.value == "all" || item.RequestDT.contains(yearFilter.value)) &&
            (testFilter.value == "all" || item.ProcedureName == testFilter.value) &&
            (placeFilter.value == "all" || item.HealthCareFacilityNameEN == placeFilter.value)
        }


        _uniqueHospitalNames.value = getUniqueHospital(_filteredEMRList.value)
        _uniqueHospitalNameDT.value = getUniqueHospitalDT(_filteredEMRList.value)

        if(filter != "") {
            _uniqueHospitalNameDT.value = getFilteredHospitalNameDT(filter)
//            _uniqueHospitalNames.value = _uniqueHospitalNameDT.value?.map { it.first }?.toMutableList()
        }



    }

    fun updateYearFilter(yearFilterArg: String){
        _yearFilter.value = yearFilterArg
        setFilteredEMRList()
    }

    fun updateTestFilter(testFilterArg: String){
        _testFilter.value = testFilterArg
        setFilteredEMRList()

    }

    fun updatePlaceFilter(placeFilterArg: String){
        _placeFilter.value = placeFilterArg
        setFilteredEMRList()
    }

    fun getFilteredHospitalNameDT(filterString: String): MutableList<Pair<String, String>>{

        val matches = mutableListOf<Pair<String, String>>()

        _uniqueHospitalNameDT.value?.forEach {
            if(it.first.contains(filterString, ignoreCase = true) || ifTestInHospitalDT(filterString, it)){
                matches.add(it)
            }
        }
        Log.e("MYLOG", matches.size.toString())
        return matches

    }

    fun ifTestInHospitalDT(test: String, hospitalDT: Pair<String, String>): Boolean{
        val matches = _filteredEMRList.value?.filter {
            it.HealthCareFacilityNameEN == hospitalDT.first &&
            it.RequestDT == hospitalDT.second &&
            it.ProcedureName.contains(test, ignoreCase = true)
        }

//        matches?.forEach {
//            if(it.ProcedureName.contains(test)){
//                Log.e("MYLOG", "${it.ProcedureName} matches $test")
//            }
//        }

        if(matches == null){
            return false
        }
       return matches.isNotEmpty();

    }

    fun setYearMinMax() {
        var min = _EMRList.value?.get(0)?.RequestDT?.substring(6, 10)?.toInt()
        var max = _EMRList.value?.get(0)?.RequestDT?.substring(6, 10)?.toInt()
        var year = 0

        _EMRList.value?.forEach {
            year = it.RequestDT.substring(6, 10).toInt()

            if (year >= (max as Int)) {
                max = year
            }

            if (year <= (min as Int)) {
                min = year
            }
        }

        _minYear.value = min as Int
        _maxYear.value = max as Int

    }


    // auxilary functions

    fun getUniqueTestsString(allEMRs: List<EMRItem>?): MutableList<String>{
        var uniqueList: MutableList<String> = mutableListOf("all")
        if (allEMRs != null) {
            for(entry in allEMRs){
                if(!uniqueList.contains(entry.ProcedureName)){
                    uniqueList.add(entry.ProcedureName)
                }
            }
        }
        return uniqueList
    }

    fun readJSONFromRaw(context: Context, @RawRes resourceId: Int): String? {
        val `is` = context.getResources().openRawResource(resourceId)
        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        try {
            val reader: Reader = BufferedReader(InputStreamReader(`is`, StandardCharsets.UTF_8))
            var n: Int
            while ((reader.read(buffer).also { n = it }) != -1) {
                writer.write(buffer, 0, n)
            }
            `is`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return writer.toString()
    }


    fun getUniqueHospital(allEMRs: List<EMRItem>?): MutableList<String>{
        var uniqueList: MutableList<String> = mutableListOf("all")
        if (allEMRs != null) {
            for(entry in allEMRs){
                if(!uniqueList.contains(entry.HealthCareFacilityNameEN)){
                    uniqueList.add(entry.HealthCareFacilityNameEN)
                }
            }
        }
        return uniqueList
    }

    fun getUniqueHospitalDT(allEMRs: List<EMRItem>?): MutableList<Pair<String, String>>{
        var uniqueList: MutableList<Pair<String, String>> = mutableListOf()
        if (allEMRs != null) {
            for(entry in allEMRs){
                if(uniqueList.size == 0){
                    uniqueList.add(Pair(entry.HealthCareFacilityNameEN, entry.RequestDT))
                    continue;
                }
                if(!uniqueList.contains(Pair(entry.HealthCareFacilityNameEN, entry.RequestDT))){
                    uniqueList.add(Pair(entry.HealthCareFacilityNameEN, entry.RequestDT))
                }
            }
        }
        return uniqueList
    }

}



