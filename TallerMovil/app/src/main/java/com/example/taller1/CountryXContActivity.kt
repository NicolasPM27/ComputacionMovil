package com.example.taller1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.taller1.databinding.ActivityCountryXcontBinding
import com.example.taller1.databinding.AdapterCountryInfoBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class CountryXContActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCountryXcontBinding

    companion object {
        private const val COUNTRIES_FILE = "paises.json"
    }
    private lateinit var countryList: ListView//List view to show the countries
    private lateinit var countries: JSONArray//Json array to store the countries

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_country_xcont)
        binding = ActivityCountryXcontBinding.inflate(layoutInflater)
        setContentView(binding.root)

        countryList = binding.countryListView
        //Data from avtivity main
        val continent = intent.getStringExtra("continent")

        //Mutable list to store the countries
        val countriesFiltered = mutableListOf<JSONObject>()
        try {
            val jsonFile = loadCountriesByJson()
            countries = jsonFile.getJSONArray("Countries")
            //Add the countries from the json file to the mutable list
            for (i in 0 until countries.length()) {
                val countryJson = countries.getJSONObject(i)
                //Filter the countries by continent
                if (countryJson.getString("Region").equals(continent)) {
                    countriesFiltered.add(countryJson)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        //Create the adapter modified
        val adapterListView = object :
            ArrayAdapter<JSONObject>(this, android.R.layout.simple_list_item_1, countriesFiltered) {
            @SuppressLint("ViewHolder")//Ignore the warning

            //Override the getView method to show the name and the code of the country
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                //Get the json object
                val jsonObject = getItem(position)
                //Create the binding to access the views of the card view
                val binding2 =
                    AdapterCountryInfoBinding.inflate(LayoutInflater.from(context), parent, false)

                //Country image
                val countryFlag: ImageView = binding2.countryFlag
                //Asign the image to the image view using picasso
                Picasso.get().load(jsonObject!!.getString("FlagPng")).into(countryFlag)

                //Country name
                val countryNameText: TextView = binding2.countryName
                countryNameText.text = jsonObject!!.getString("Name")

                //Country native name
                val countryNativeNameText: TextView = binding2.countryNativeName
                countryNativeNameText.text = jsonObject.getString("NativeName")

                // Country code
                val country3CodeText: TextView = binding2.countryCode
                country3CodeText.text = jsonObject.getString("Alpha3Code")

                //Country currency name and symbol
                val countryCurrencyNameText: TextView = binding2.countryCurrencyNameAndSymbol
                countryCurrencyNameText.text =
                    jsonObject.getString("CurrencyName") + " (" + jsonObject.getString("CurrencySymbol") + ")"

                //Listener to show the details of the country
                binding2.root.setOnClickListener{
                    val intent = Intent(applicationContext, DetailsCountryActivity::class.java)
                    try {
                        intent.putExtra(
                            "selectedCountry",
                            countriesFiltered.get(position).toString()
                        )
                    } catch (e: JSONException) {
                        throw RuntimeException(e)
                    }
                    startActivity(intent)
                }
                //Listener to call the country
                binding2.floatingActionButton3.setOnClickListener{
                    when{
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)//Check if the permission is granted
                                == PackageManager.PERMISSION_GRANTED -> {
                        //Create the intent to call the country
                            val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:" + jsonObject.getString("NumericCode"))
                            startActivity(intent)
                    }
                        shouldShowRequestPermissionRationale(android.Manifest.permission.CALL_PHONE) -> {//Check if the user has denied the permission
                            Snackbar.make(binding.root, "El permiso es requerido para usar la funcionalidad de llamadas", Snackbar.LENGTH_LONG).show()
                        ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.CALL_PHONE), 1)
                        }
                        else -> {
                            //Request the permission
                            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.CALL_PHONE), 1)
                        }
                    }
                }
                return binding2.root
            }
        }
        countryList.adapter = adapterListView
    }

    private fun loadJSONFromAsset(assetName: String): String {
        val json: String = try {
            val inputStream = this.assets.open(assetName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            ""
        }
        return json
    }

    @Throws(JSONException::class)
    fun loadCountriesByJson(): JSONObject {
        return JSONObject(loadJSONFromAsset(COUNTRIES_FILE))
    }

}
