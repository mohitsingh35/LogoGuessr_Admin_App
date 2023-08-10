package com.ncs.logoguessradmin.firebaseDB.ui

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncs.logoguessradmin.firebaseDB.RealTimeModelResponse
import com.ncs.logoguessradmin.repository.RealtimeRepository
import com.ncs.logoguessradmin.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealtimeViewModel @Inject constructor
    (private val repo: RealtimeRepository):ViewModel(){
    private val _res:MutableState<ItemState> = mutableStateOf(ItemState())
    val res:State<ItemState> = _res
    fun insert(items:RealTimeModelResponse.RealTimeItems,childName:String,uri: Uri)=repo.insert(items,childName,uri)

    private val _updateRes:MutableState<RealTimeModelResponse> = mutableStateOf(
        RealTimeModelResponse(item = RealTimeModelResponse.RealTimeItems(),
            )
    )
    val updateRes:State<RealTimeModelResponse> = _updateRes


    fun setData(data:RealTimeModelResponse){
        _updateRes.value=data
    }
    var childName:String="initialChildName"
    init {
        viewModelScope.launch {
            repo.getItems(childName).collect{
                when(it){
                    is ResultState.Success->{
                        _res.value=ItemState(
                            item = it.data
                        )
                    }
                    is ResultState.Failure->{
                        _res.value=ItemState(
                            error = it.msg.toString()
                        )
                    }
                    ResultState.Loading->{
                        _res.value=ItemState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
    fun delete(key:String,childName:String)=repo.delete(key,childName)
    fun update(item:RealTimeModelResponse,childName:String)=repo.update(item,childName)

}

data class ItemState(
    val item:List<RealTimeModelResponse> = emptyList(),
    val error:String = "",
    val isLoading:Boolean=false
)