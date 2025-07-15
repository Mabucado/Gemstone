package com.example.part2.ui.login

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.part2.R

class HistoryAdapter(private val gemstoneList: MutableList<HistoryList>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.imageView)
            val textViewName: TextView = itemView.findViewById(R.id.textViewName)
            val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
            val textViewArea: TextView = itemView.findViewById(R.id.textViewArea)
            val textViewValue: TextView = itemView.findViewById(R.id.textViewValue)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val gemstone = gemstoneList[position]
            holder.textViewName.text = gemstone.name
            holder.textViewDate.text = gemstone.date
            holder.textViewArea.text = gemstone.place
            holder.textViewValue.text = gemstone.value.toString()

            gemstone.image?.let {
                Glide.with(holder.itemView.context)
                    .load(it)
                    .into(holder.imageView)
            }
        }

        override fun getItemCount(): Int {
            return gemstoneList.size
        }
    }

data class HistoryList(val name:String, val date:String, val place:String, val value:String, val image: Uri)