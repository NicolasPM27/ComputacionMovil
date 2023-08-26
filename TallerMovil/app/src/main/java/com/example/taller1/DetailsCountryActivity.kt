package com.example.taller1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taller1.databinding.ActivityDetailsCountryBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DetailsCountryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsCountryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_details_country)

        binding = ActivityDetailsCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Recieve the string with the details of the country selected
        val selectedCountry = intent.getStringExtra("selectedCountry")

        //Create a json object to store the details of the country selected
        val country: JSONObject = loadJSONFromString(selectedCountry!!)

        //Load the details of the country in the view
        //Country flag
        Picasso.get().load(country!!.getString("FlagPng")).into(binding.countryFlag2)
        //Country name
        binding.countryName2.text = country.getString("Name")
        //Country codes
        binding.countryCodes.text = country.getString("Alpha2Code") + " - " + country.getString("Alpha3Code")
        //Country currency
        binding.countryCurrency.text = country.getString("CurrencyName") + "-"+country.getString("CurrencyCode")+" (" + country.getString("CurrencySymbol") + ")"
        //Country ubication
        binding.countryUbication.text = country.getString("Region") + "/" + country.getString("SubRegion") +"("+country.getString("Latitude")+","+country.getString("Longitude")+")"
        //Country Area
        binding.countryArea.text = "Área: "+country.getString("Area") + " km2"
        //Country language
        binding.countryLanguage.text = "Lenguaje: "+country.getString("NativeLanguage")
    }

    private fun loadJSONFromString(jsonString: String): JSONObject {
        return try {
            JSONObject(jsonString)
        } catch (ex: org.json.JSONException) {
            ex.printStackTrace()
            JSONObject()
        }
    }
}