package com.ncs.logoguessradmin.firebaseDB.ui

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
    (private val repo:RealtimeRepository):ViewModel(){
    private val _res:MutableState<ItemState> = mutableStateOf(ItemState())
    val res:State<ItemState> = _res
    fun insert(items:RealTimeModelResponse.RealTimeItems)=repo.insert(items)

    private val _updateRes:MutableState<RealTimeModelResponse> = mutableStateOf(
        RealTimeModelResponse(item = RealTimeModelResponse.RealTimeItems(),
            )
    )
    val updateRes:State<RealTimeModelResponse> = _updateRes

    fun setData(data:RealTimeModelResponse){
        _updateRes.value=data
    }
    init {

        viewModelScope.launch {
            repo.getItems().collect{
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
    fun delete(key:String)=repo.delete(key)
    fun update(item:RealTimeModelResponse)=repo.update(item)

}

data class ItemState(
    val item:List<RealTimeModelResponse> = emptyList(),
    val error:String = "",
    val isLoading:Boolean=false
)