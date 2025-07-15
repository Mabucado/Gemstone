package com.example.part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.part2.ui.login.ListAdapterer

class Items : AppCompatActivity() {
    lateinit var goAddItem:Button
    lateinit var displayItemList:ListView
    lateinit var goHome:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        goAddItem=findViewById(R.id.btnAddItem)
        displayItemList=findViewById(R.id.listViewItems)
        goHome=findViewById(R.id.btnHome2)

        goAddItem.setOnClickListener {
            var intent= Intent(this,itemsForm::class.java)
            startActivity(intent)
        }
      val model =viewModel.model

        if(model.gemstoneName!=null) {
            val gemstoneNames: MutableList<String> = model.gemstoneName.keys.toMutableList()

            val adapterV = ListAdapterer(this, gemstoneNames)

            displayItemList.adapter = adapterV
            adapterV.notifyDataSetChanged()
            Log.i("info","There is data")
            android.util.Log.i("info",model.gemstoneName.toString())


        }
        else{
            Log.i("info","List empty")
            android.util.Log.i("info",model.gemstoneName.size.toString())
        }
        goHome.setOnClickListener {
            var intent=Intent(this,dashboardActivity2::class.java)
            startActivity(intent)
        }

    }
}