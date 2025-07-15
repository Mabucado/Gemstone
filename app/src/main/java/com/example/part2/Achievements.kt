package com.example.part2

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent

import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast


class Achievements : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var model: Model
    lateinit var circularProgressBar: CircularProgressBar
    lateinit var spinner:Spinner
    lateinit var setButton: Button
    lateinit var goalValue: TextView
    lateinit var homeBtn:Button
     var setTotal:Int = 0
    var selectedItem=""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)


        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)
        model = viewModel.model
        setButton=findViewById(R.id.btnSetValue)
        goalValue=findViewById(R.id.edtGoalValue)
        spinner=findViewById(R.id.spinner)
        homeBtn = findViewById(R.id.homeBtn)

        circularProgressBar = findViewById(R.id.circularProgressBar)


        val category: List<String> = model.gemstoneCategory.keys.toList()
        val name:List<String> = model.gemstoneName.keys.toList()
        Log.i("spinner", category.size.toString())
        Log.i("name", name.size.toString())
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        setButton.setOnClickListener {
            setTotal=goalValue.text.toString().toInt()
            val count = model.gemstoneName.values.count { it.category==selectedItem }
            val totalItems = setTotal // Total number of items representing 100%
            val itemCount = count
            val progress = ((itemCount.toFloat() / totalItems.toFloat()) * 100).toInt()
            Log.i("count",count.toString())
            Log.i("totalItems",totalItems.toString())
            circularProgressBar.setProgress(progress)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle item selection
                 selectedItem = parent?.getItemAtPosition(position).toString()
                Toast.makeText(applicationContext, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()


            }



            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        homeBtn.setOnClickListener {
            val intent= Intent(this,dashboardActivity2::class.java)
            startActivity(intent)
        }

    }

}

class CircularProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var progress = 0 // Current progress
    private val strokeWidth = 30f // Stroke width of the progress bar
    private val startAngle = -90f // Start angle of the progress bar
    private val rectF = RectF() // RectF to define the bounds of the circle
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = this@CircularProgressBar.strokeWidth
        color =  Color.parseColor("#60caf7")// Change the color as needed
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Calculate the center and radius of the circle
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width / 4f - strokeWidth / 2

        // Set the bounds of the circle using RectF
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        // Draw the progress bar as an arc
        canvas.drawArc(rectF, startAngle, 360f * progress / 100, false, paint)

        // Draw the percentage text in the middle
        paint.textSize = 150f // Set the text size
        paint.textAlign = Paint.Align.CENTER // Align text to the center
        val text = "$progress%" // Display progress percentage
        canvas.drawText(text, centerX, centerY - (paint.descent() + paint.ascent()) / 2, paint)
    }

    // Method to update the progress
    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate() // Invalidate the view to trigger onDraw()
    }
}