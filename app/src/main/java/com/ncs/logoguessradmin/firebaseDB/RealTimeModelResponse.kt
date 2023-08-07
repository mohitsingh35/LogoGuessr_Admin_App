package com.ncs.logoguessradmin.firebaseDB

data class RealTimeModelResponse(
    val item:RealTimeItems?,
    val key:String?=""
){

    data class RealTimeItems(
        val options: List<String?> = emptyList(),
        val answer:String?=""
    )
}

