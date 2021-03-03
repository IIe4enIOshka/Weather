package com.example.weather.List

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.weather.CityAdapter
import com.example.weather.Detail.DetailActivity
import com.example.weather.Entity.City
import com.example.weather.R
import com.example.weather.Service.WeatherService
import com.example.weather.WeatherApplication
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ListActivity : AppCompatActivity(), ListView {

    private lateinit var weatherService: WeatherService
    private lateinit var dateText: TextView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var cityList: RecyclerView

    private val presenter by lazy {
        ListPresenter((application as WeatherApplication).weatherService)
    }

    private val adapter = CityAdapter {
        presenter.onCityClicked(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.attachView(this)

        weatherService = (application as WeatherApplication).weatherService
        setContentView(R.layout.activity_main)

        dateText = findViewById(R.id.dateText)

        cityList = findViewById(R.id.cityList)
        cityList.adapter = adapter

        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh)
        mSwipeRefreshLayout.setColorSchemeColors(
            Color.RED, Color.GREEN, Color.BLUE, Color.CYAN
        )

        //  слушатель свайпа вниз для обновления данных
        mSwipeRefreshLayout.setOnRefreshListener(OnRefreshListener {
            mSwipeRefreshLayout.isRefreshing = true
            presenter.onViewUpdate()
            mSwipeRefreshLayout.isRefreshing = false
        })
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewResumed()
    }

    override fun bindCityList(list: List<City>) {
        adapter.citys = list
    }

    override fun openCityDetailsScreen(cityId: Int) {
        DetailActivity.start(this, cityId)
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    override fun saveData(list: List<City>) {
        val sharedPreferences = getSharedPreferences("shared pref", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val mass = JSONArray()
        for (i in list.indices) {
            val eachData = JSONObject()
            mass.put(i, eachData.put("id", list[i].id))
            mass.put(i, eachData.put("name", list[i].name))
            mass.put(i, eachData.put("temp", list[i].temp))
            mass.put(i, eachData.put("temp2", list[i].temp2))
            mass.put(i, eachData.put("temp3", list[i].temp3))
        }
        editor.putString("Data", mass.toString())
        editor.apply()
        val dateFormat: DateFormat = SimpleDateFormat("HH:mm dd.MM.yy")
        val date = Date()
        editor.putString("date", dateFormat.format(date))
        editor.apply()
    }

    override fun getDate(): String? {
        val sharedPreferences = getSharedPreferences("shared pref", AppCompatActivity.MODE_PRIVATE)
        val date = sharedPreferences.getString("date", null)
        dateText.text = getString(R.string.dateText, date)
        return date
    }

    override fun loadData(): List<City> {
        val listCitys = mutableListOf<City>()
        val sharedPreferences = getSharedPreferences("shared pref", AppCompatActivity.MODE_PRIVATE)
        val data = sharedPreferences.getString("Data", null)
        val json = JSONArray(data)
        for (i in 0 until json.length()) {
            val id = json.getJSONObject(i).getString("id")
            val name = json.getJSONObject(i).getString("name")
            val temp = json.getJSONObject(i).getString("temp").toInt()
            val temp2 = json.getJSONObject(i).getString("temp2").toInt()
            val temp3 = json.getJSONObject(i).getString("temp3").toInt()
            listCitys.add(City(i, name, temp, temp2, temp3))
        }
        return listCitys
    }
}