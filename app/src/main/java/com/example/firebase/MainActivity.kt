package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding
    private lateinit var identificador: String
    private lateinit var nuevapersona: Persona

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val ref = FirebaseDatabase.getInstance().reference
        ref.child("Hola").setValue("Caracola")



        bind.crear.setOnClickListener {
            bind.texto.text = "Datos creados"
            identificador = ref.child("personas").push().key!!
            nuevapersona = Persona("Miguel", "Mata", "Ramos", "mataramosmiguel@gmail.com", 21)
            ref.child("personas").child(identificador).setValue(nuevapersona)

        }

        bind.borrar.setOnClickListener {
            bind.texto.text = "Datos creados han sido borrados"
            ref.child("personas").removeValue()
        }

        bind.mod.setOnClickListener {
            bind.texto.text = "Datos modificados"

            //Primera forma
            ref.child("personas").child(identificador).child("nombre").setValue("yoese")
            ref.child("personas").child(identificador).child("apellido1").setValue("apell1")

            //Segunda forma
//            nuevapersona.nombre="yoese"
//            nuevapersona.apellido1="apell1"
//            nuevapersona.apellido1="apell2"
//            ref.child("personas").child(identificador).setValue(nuevapersona)

            //Tercera forma
//            var otrapersona= Persona("name","ap","ap2","@",1)
//            ref.child("personas").child(identificador).setValue(otrapersona)
        }

        bind.ver.setOnClickListener {
            var res = ""

            ref.child("personas")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach { hijo: DataSnapshot? ->
                            val pojo = hijo?.getValue(Persona::class.java)
                            res += "Nombre -> "+pojo?.nombre + "\n" +
                                    "Apellido 1 -> "+pojo?.apellido1 + "\n" +
                                    "Apellido 2 -> "+pojo?.apellido2 + "\n" +
                                    "Email -> "+pojo?.email + "\n" +
                                    "Edad -> "+pojo?.edad + "\n\n"
                        }


                        bind.texto.text = res

                    }

                    override fun onCancelled(error: DatabaseError) {
                        print(error.message)
                    }

                })
        }


    }
}