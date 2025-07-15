package com.example.part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    lateinit var Login: Button
    lateinit var CreateAccount:Button
    lateinit var email:TextView
    lateinit var password:TextView
    private var backPressedOnce = false
    private val exitHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)
        val model =viewModel.model
        viewModel.fetchUsersFromFirestore()

        Login = findViewById(R.id.LoginBtn)
        CreateAccount = findViewById(R.id.CreateAccountBtn)
        email=findViewById(R.id.EmailEditText)
        password=findViewById(R.id.edtPassword)
        Login.setOnClickListener {

            var emailVar=email.text.toString()
            var pass=password.text.toString()
            if(emailVar.equals("mathebulasibusiso@gmail.com")||model.users.values.any{it.email==emailVar} ){
                if(pass.equals("s!bus!so")||model.users.values.any{it.password==pass})
                {

                    var intentdashboardActivity2 = Intent(this, dashboardActivity2::class.java)
                    startActivity(intentdashboardActivity2)
                }else{
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Incorrect email!", Toast.LENGTH_SHORT).show()
            }
        }

        CreateAccount.setOnClickListener {
            var intentRegisterActivity = Intent( this, RegisterActivity::class.java)
            startActivity(intentRegisterActivity)
        }



    }
    override fun onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed()
            return
        }

        this.backPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        exitHandler.postDelayed({
            backPressedOnce = false
        }, 2000)  // Reset backPressedOnce after 2 seconds
    }
}