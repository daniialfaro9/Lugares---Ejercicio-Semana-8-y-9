package com.lugares.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
//import androidx.room.*
import com.lugares.model.Lugar
import org.w3c.dom.Document

//@Dao
class LugarDao {

    private lateinit var codigoUsuario: String
    private lateinit var firestore:FirebaseFirestore

    init {
        val usuario = Firebase.outh.currentUser?.email
        codigoUsuario="$usuario"
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings=FirebaseFirestoreSettings.Builder().build()

    }

    //para obtener la lista de lugares
    fun getLugares():MatableLiveData<List<Lugar>>{
        val listaLugares= MatableLiveData<List<Lugar>>()
                firestore.collection("lugareApp")
                .document(codigoUsuario)
                    .collection("misLugares")
                    .addSnapshotListener{snapshot,e->
                        if(e != null){
                            return@addSnapshotListener
                        }
                        if(snapshot != null){
                            val lista = ArrayList<List<Lugar>>()
                            val lugares= snapshot.documents
                            lugares.forEach{
                                val lugar =it.toObject(Lugar::class.java)
                                if (lugar!=null){
                                    lista.add(lugar)
                                }
                                listaLugares.value= lista
                            }
                        }
                        return listaLugares
                    }
        }
//salvar Lugar
    fun saveLugar(lugar:Lugar){
        val document:DocumentReference
        if(lugar.id.isEmpty()) {
            ducument= firestore
                .collection("lugaresApp")
                .document(codigoUsuario)
                .collection("misLugares")
                .document()
            lugar.id= document.id

        }else{
            document= firestore
                .collection("lugaresApp")
                .document(codigoUsuario)
                .collection("mislugares")
                .document(lugar.id)
        }
    val set = document.set(lugar)
    set.addOnSuccessListener {
        Log.d("Add Lugar", "Luagr Agregado")
    }
        .addOnCanceledListener {  }
            Log.e ("Add Lugar", "Lugar No Agregado")
        }
    }

fun deleteLugar(lugar: Lugar){
    if (lugar.id.isNotEmpty()){
        firebase
            .collection("LugaresApp")
            .document(codigoUsuario)
            .collection("misLugares")
            .document(lugar.id)
            .delete()
            .addOnSuccessListener{
                Log.d("Add Lugar" "Lugar Eliminado")
            }
            .addOnSuccessListener{
                Log.e("Add Lugar" "Lugar NO Eliminado")
    }
}



//Función para obtener la lista de lugares
    //@Query("select * from LUGAR")
    //fun getAllData() : LiveData<List<Lugar>>

    //@Insert(onConflict = OnConflictStrategy.IGNORE)
    //suspend fun addLugar(lugar: Lugar)

  //  @Update(onConflict = OnConflictStrategy.IGNORE)
   // suspend fun updateLugar(lugar: Lugar)

  //  @Delete
   // suspend fun deleteLugar(lugar: Lugar)

}