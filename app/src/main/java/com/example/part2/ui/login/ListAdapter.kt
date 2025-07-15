package com.example.part2.ui.login

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.part2.R

class ListAdapterer(context: Context, private val gemstoneNames: List<String>) :
    ArrayAdapter<String>(context, 0, gemstoneNames) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val nameTextView: TextView = itemView!!.findViewById(R.id.nameTextView)

        // Get the name at this position
        val name = gemstoneNames[position]

        // Set the name to the view
        nameTextView.text = name


        return itemView
    }
}
