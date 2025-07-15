package com.example.part2


import com.example.part2.R
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.net.URI


class ImageTextAdapter(private val context: Context,  val mapList:MutableMap<String,Uri?>) : BaseAdapter() {

    private val dataList: List<Map.Entry<String, Uri?>> = mapList.entries.toList()
    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        Log.i("data list",dataList.toString())
        val view: View
        if (convertView == null) {
            view = View.inflate(context, R.layout.grid_item, null)
        } else {
            view = convertView
        }

        val item = dataList[position]
        val textView: TextView = view.findViewById(R.id.item_name)
        val imageView: ImageView = view.findViewById(R.id.grid_image)

        textView.text = item.key
        if (item.value != null) {
            Glide.with(imageView.context).load(item.value).into(imageView)

            imageView.visibility = View.VISIBLE
        } else {
            imageView.setImageURI(null)
            imageView.visibility = View.INVISIBLE // or View.GONE if you want to remove it completely
        }
        return view!!
    }

    private class ViewHolder {
        lateinit var imageView: ImageView
        lateinit var textView: TextView

    }
}

class ImageTextModel(val text: String, val imageResId: Uri?) {


}



