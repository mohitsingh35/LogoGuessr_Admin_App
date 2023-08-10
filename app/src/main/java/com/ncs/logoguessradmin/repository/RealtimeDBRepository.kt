package com.ncs.logoguessradmin.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ncs.logoguessradmin.firebaseDB.RealTimeModelResponse
import com.ncs.logoguessradmin.utils.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealtimeDBRepository @Inject constructor(
    private val db:DatabaseReference
):RealtimeRepository {
    private var storageReference=Firebase.storage
    override fun insert(item: RealTimeModelResponse.RealTimeItems, childName: String, uri: Uri): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            storageReference = FirebaseStorage.getInstance()
            val imageRef = storageReference.getReference("images").child(childName).child(System.currentTimeMillis().toString())
            imageRef.putFile(uri).addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { imageUrl ->
                    val mapImage = mapOf("url" to imageUrl.toString())
                    val itemRef = db.child("questions").child(childName).push()
                    itemRef.setValue(item)
                    itemRef.child("image").setValue(mapImage)
                    trySend(ResultState.Success("Inserted Successfully"))
                }
            }
            awaitClose {
                close()
            }
        }


    override fun getItems(childName: String): Flow<ResultState<List<RealTimeModelResponse>>> = callbackFlow{
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
                Log.d("pari",items.toString())
            }


            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }

        }
        db.child("questions").child(childName).addValueEventListener(valueEvent)
        awaitClose{
            db.child("questions").child(childName).removeEventListener(valueEvent)
            close()
        }
    }





    override fun delete(key: String,childName:String): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        db.child("questions").child(childName).child(key).removeValue()
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

    override fun update(res: RealTimeModelResponse,childName:String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val map=HashMap<String,Any>()
        map["options"]=res.item?.options!!
        map["answer"]=res.item.answer!!

        db.child("questions").child(childName).child(res.key!!).updateChildren(
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