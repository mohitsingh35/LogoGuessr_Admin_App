package com.ncs.logoguessradmin.repository

import com.ncs.logoguessradmin.firebaseDB.RealTimeModelResponse
import com.ncs.logoguessradmin.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface RealtimeRepository {
    fun insert(
        item:RealTimeModelResponse.RealTimeItems,childName:String
    ): Flow<ResultState<String>>

    fun getItems(childName: String):Flow<ResultState<List<RealTimeModelResponse>>>

    fun delete(
        key:String,childName:String
    ):Flow<ResultState<String>>

    fun update(
        res:RealTimeModelResponse,childName:String
    ):Flow<ResultState<String>>
}