package com.example.part2

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Model(val gemstoneName:MutableMap<String,Gemstone> = mutableMapOf(),
            val gemstoneCategory:MutableMap<String, Uri?> = mutableMapOf(),
    val users:MutableMap<String,User> = mutableMapOf()


            ) {
}
data class Gemstone(var description:String="",var category:String="",var place:String="", var value:Int=0, var date:String, var image: Uri?=null,var video:Uri?=null)
data class User( var surname:String="", var email:String="", var password:String="")


data class Image(
    val id: Int,
    val imagePath: String
)
val db = Firebase.firestore
val globalModel = Model()

class DataViewModel : ViewModel() {
    val model = globalModel

    fun fetchUsersFromFirestore() {
        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val user = document.toObject(User::class.java)
                        // Do something with the user object, such as adding it to your users map
                        model.users[document.id] = user
                    Log.d(TAG, "${document.id} => ${document.data}")
                    Log.d(TAG, "${user}")
                }
                // Now you have fetched all users from Firestore and added them to your users map
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur during the fetch
                Log.w(TAG, "Error getting documents: ", exception)
            }
        db.collection("gemstoneName")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val gemstoneData = document.data
                    val gemstone = Gemstone(
                        description = gemstoneData["description"] as String,
                        category = gemstoneData["category"] as String,
                        place = gemstoneData["place"] as String,
                        value = (gemstoneData["value"] as Long).toInt(),
                        date = gemstoneData["date"] as String,
                        image = gemstoneData["image"]?.let { Uri.parse(it as String) },
                        video = gemstoneData["video"]?.let{Uri.parse(it as String)}
                    )

                        // Use the gemstone object with the Uri
                        model.gemstoneName[document.id] =gemstone

                    // Do something with the user object, such as adding it to your users map

                    Log.d(TAG, "${document.id} => ${document.data}")
                    Log.d(TAG, "${gemstone}")
                }
                // Now you have fetched all users from Firestore and added them to your users map
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur during the fetch
                Log.w(TAG, "Error getting documents: ", exception)
            }
        val collectionRef = db.collection("gemstoneCategory")

        collectionRef.get()
            .addOnSuccessListener { documents ->
                val fetchedGemstoneCategory: MutableMap<String, Uri?> = mutableMapOf()

                for (document in documents) {
                    val key = document.id
                    val uriString = document.getString("uri")
                    val uri = uriString?.let { Uri.parse(it) }
                    model.gemstoneCategory[key] = uri
                }

                // Now fetchedGemstoneCategory is your map with Uri values
                Log.d("Firestore", "Fetched GemstoneCategory: $fetchedGemstoneCategory")
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
            }
    }
}