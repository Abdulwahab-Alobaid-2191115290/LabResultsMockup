package com.example.labresultsmockup.models

data class StructuredEMRItem(
    var hospitalName: String,
    var EMRItemRequestDT: String,
    var hospitalEMRItem: EMRItem,
)