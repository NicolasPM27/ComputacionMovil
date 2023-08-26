package com.example.taller1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.taller1.databinding.ActivityMainBinding
import android.R
import android.content.Intent


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var continentSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        continentSpinner = binding.spinner
        val continentsArray = arrayOf("Africa", "Americas", "Asia", "Europe", "Oceania")

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, continentsArray)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        continentSpinner.adapter = adapter

        binding.button.setOnClickListener{
         val selectedItem = binding.spinner.selectedItem.toString()
         val intent = Intent(applicationContext, CountryXContActivity::class.java)
            intent.putExtra("continent", selectedItem)
            startActivity(intent)
        }
    }
}