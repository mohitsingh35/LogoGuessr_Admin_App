package com.ncs.logoguessradmin.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.ncs.logoguessradmin.firebaseDB.RealTimeModelResponse
import com.ncs.logoguessradmin.utils.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealtimeDBRepository @Inject constructor(
    private val db:DatabaseReference
):RealtimeRepository {
    override fun insert(item: RealTimeModelResponse.RealTimeItems): Flow<ResultState<String>> =
        callbackFlow{
            trySend(ResultState.Loading)
            db.push().setValue(
                item
            ).addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(ResultState.Success("Inserted Successfully"))
            }.addOnFailureListener{
                trySend(ResultState.Failure(it))
            }
            awaitClose{
                close()
            }

    }

    override fun getItems(): Flow<ResultState<List<RealTimeModelResponse>>> = callbackFlow{
        trySend(ResultState.Loading)

        val valueEvent=object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val items=snapshot.children.map {
                    RealTimeModelResponse(
                        it.getValue(RealTimeModelResponse.RealTimeItems::class.java),
                        key = it.key
                    )
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }

        }
        db.addValueEventListener(valueEvent)
        awaitClose{
            db.removeEventListener(valueEvent)
            close()
        }
    }

    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        db.child(key).removeValue()
            .addOnCompleteListener{
                trySend(ResultState.Success("item Deleted"))
            }
            .addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun update(res: RealTimeModelResponse): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val map=HashMap<String,Any>()
        map["options"]=res.item?.options!!
        map["answer"]=res.item.answer!!

        db.child(res.key!!).updateChildren(
            map
        ).addOnCompleteListener{
            trySend(ResultState.Success("Updated Successfully"))
        }
            .addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

}