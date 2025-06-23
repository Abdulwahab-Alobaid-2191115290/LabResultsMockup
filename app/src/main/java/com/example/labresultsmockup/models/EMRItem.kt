package com.example.labresultsmockup.models

import kotlinx.serialization.Serializable

@Serializable
data class EMRItem(
    val AccessionNo: String,
    val CivilID: String,
    val GroupCode: String,
    val HealthCareFacility: String,
    val HealthCareFacilityNameAR: String,
    val HealthCareFacilityNameEN: String,
    val OperatingCompanyNameAR: String,
    val OperatingCompanyNameEN: String,
    val ProcedureName: String,
    val ProcedureNo: String,
    val ProcedureStatus: String,
    val ProcedureTypeNameAR: String,
    val ProcedureTypeNameEN: String,
    val RequestDT: String,
    val ResultDT: String,
    val ResultURL: String,
    val SampleCollectionDT: String,
    val TestCode: String
)