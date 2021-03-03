package com.example.weather.Detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.Entity.City
import com.example.weather.R
import com.example.weather.Service.WeatherService
import com.example.weather.WeatherApplication
import org.json.JSONArray

class DetailActivity : AppCompatActivity(), DetailView{

    companion object {

        private const val EXTRA_ID = "EXTRA_ID"

        fun start(context: Context, id: Int) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_ID, id)
            context.startActivity(intent)
        }
    }

    private val presenter by lazy {
        DetailPresenter(
            service = (application as WeatherApplication).weatherService,
            cityId = intent.getIntExtra(EXTRA_ID, 0),
            citys = loadData()
        )
    }

    private lateinit var weatherService: WeatherService

    private lateinit var nameText: TextView
    private lateinit var tempText: TextView
    private lateinit var temp2Text: TextView
    private lateinit var temp3Text: TextView

    private lateinit var tempText_lb: TextView
    private lateinit var temp2Text_lb: TextView
    private lateinit var temp3Text_lb: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initViews()

        presenter.attachView(this)
    }

    fun loadData(): List<City> {
        val listCitys = mutableListOf<City>()
        val sharedPreferences = getSharedPreferences("shared pref", MODE_PRIVATE)
        val data = sharedPreferences.getString("Data", null)
        val json = JSONArray(data)
        for (i in 0 until json.length())
        {
            val id = json.getJSONObject(i).getString("id")
            val name = json.getJSONObject(i).getString("name")
            val temp = json.getJSONObject(i).getString("temp").toInt()
            val temp2 = json.getJSONObject(i).getString("temp2").toInt()
            val temp3 = json.getJSONObject(i).getString("temp3").toInt()
            listCitys.add(City(i, name, temp, temp2, temp3))
        }
        return listCitys
    }

    private fun initViews() {
        nameText = findViewById(R.id.nameText)
        tempText = findViewById(R.id.tempText)
        temp2Text = findViewById(R.id.temp2Text)
        temp3Text = findViewById(R.id.temp3Text)

        tempText_lb = findViewById(R.id.tempText_lb)
        temp2Text_lb = findViewById(R.id.temp2Text_lb)
        temp3Text_lb = findViewById(R.id.temp3Text_lb)
        tempText_lb.text = "Сегодня"
        temp2Text_lb.text = "Завтра"
        temp3Text_lb.text = "Послезавтра"
    }

    override fun bindCity(city: City) {
        nameText.text = city.name
        tempText.text =  getString(R.string.tempText, city.temp)
        temp2Text.text = getString(R.string.tempText, city.temp2)
        temp3Text.text = getString(R.string.tempText, city.temp3)
    }

    override fun closeScreen() {
        finish()
    }
}