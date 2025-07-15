package com.example.part2

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

class RegisterActivity : AppCompatActivity() {
    lateinit var name: EditText
    lateinit var surname :EditText
    lateinit var email: EditText
    lateinit var password:EditText
    lateinit var confirmPasswordAuthentication:EditText
    lateinit var registerBtn:Button
    lateinit var backBtn:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)
        val model =viewModel.model

        name = findViewById(R.id.edtNameItem)
        surname = findViewById(R.id.edtSurnameItem)
        email = findViewById(R.id.edtEmailItem)
        password = findViewById(R.id.editTextTextPassword)
        confirmPasswordAuthentication = findViewById(R.id.confirmpassEditTex)
        registerBtn = findViewById(R.id.RegisterBtn)
        backBtn = findViewById(R.id.backBtn)

        registerBtn.setOnClickListener{
            validateInputs()
            val username = name.text.toString()
            val surnames = surname.text.toString()
            val emails = email.text.toString()
            val passwords = password.text.toString()
            if(emails!=null && passwords!=null) {
                model.users[username]=User(surnames,emails,passwords)
                val collectionRef = db.collection("users")
                for ((userId, user) in model.users) {
                    // Add each user to the Firestore collection
                  collectionRef.document(userId).set(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d("Firestore", "User $userId added successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }
            }

        }

        backBtn.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun validateInputs() {
        val username = name.text.toString().trim()
        val surnames = surname.text.toString().trim()
        val emails = email.text.toString().trim()
        val passwords = password.text.toString().trim()

        if (username.isEmpty()) {
            name.error = "Username required"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emails).matches()) {
            email.error = "Enter a valid email address"
            return
        }

        if (passwords.length < 8) {
            password.error = "Password must be at least 8 characters long"
            return
        }

        // If all inputs are valid, proceed with registration process
        registerUser(username,surnames, emails, passwords)
    }

    private fun registerUser(username: String, surnames:String, email: String, password: String) {
        // Implement registration logic here
        // This method will be called when all inputs are valid
        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
        // You can navigate to the next screen or perform other actions after registration
    }
}