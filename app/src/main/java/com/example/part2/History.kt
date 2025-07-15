package com.example.part2

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.part2.ui.login.HistoryAdapter
import com.example.part2.ui.login.HistoryList

class History : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var home: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)
        val model = viewModel.model


        recyclerView = findViewById(R.id.recyclerView)
        home = findViewById(R.id.btnHistHome)

        // Set up the RecyclerView with a GridLayoutManager
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val historyList = mutableListOf<HistoryList>()
        for (gemstone in model.gemstoneName) {
            historyList.add(
                HistoryList(
                    name = "Name: ${gemstone.key}",
                    date = "Date: ${gemstone.value.date}",
                    place = "Place Found: ${gemstone.value.place}",
                    value = "Value: R${gemstone.value.value.toString()}",
                    image = gemstone.value.image ?: Uri.EMPTY // You need to handle null Uri here
                )
            )
        }

        // Set up the adapter
        val adapter = HistoryAdapter(historyList)

        recyclerView.adapter = adapter

        home.setOnClickListener {
            val intent= Intent(this,dashboardActivity2::class.java)
            startActivity(intent)

        }
    }
}