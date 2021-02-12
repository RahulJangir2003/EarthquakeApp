package com.example.earthquake

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list.*
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    lateinit var mAdapter:quakeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter = quakeAdapter(this)
        recyclerView.layoutManager  = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
        fatchData()
    }
    fun fatchData() {
        progressBar.visibility = View.VISIBLE
        val url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=15"
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener { response ->
                    val reports = getReport(response)
                    //                    val newsJsonArray = response.getJSONArray("articles")
//                    val newsArray = ArrayList<news>()
//                    for(i in 0 until newsJsonArray.length()){
//                        val newsJsonObject = newsJsonArray.getJSONObject(i)
//                        val newNews = news(
//                                newsJsonObject.getString("title"),
//                                newsJsonObject.getString("author"),
//                                newsJsonObject.getString("url"),
//                                newsJsonObject.getString("urlToImage")
//                        )
//                        newsArray.add(newNews)
//                    }
                    if (reports != null) {
                        Toast("report updated")
                        mAdapter.updateReports(reports)
                    }else{

                        empty.visibility = View.VISIBLE
                    }
                    progressBar.visibility = View.GONE
                },
                Response.ErrorListener {
                    empty.text = it.localizedMessage
                    empty.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
        )

        MySingletonClass.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
fun Toast(msg:String){
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}
    fun getReport(SAMPLE_JSON_RESPONSE: JSONObject): java.util.ArrayList<report>? {

        // Create an empty ArrayList that we can start adding earthquakes to
        val earthquakes: java.util.ArrayList<report> = java.util.ArrayList<report>()

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            val root = SAMPLE_JSON_RESPONSE
            val array = root.getJSONArray("features")
            for (i in 0 until array.length()) {
                val curr = array.getJSONObject(i)
                val prop = curr.getJSONObject("properties")
                val url = prop.getString("url")
                val mag = prop.getDouble("mag")
                val decimalFormat = DecimalFormat("0.0")
                decimalFormat.format(mag)
                val place = prop.getString("place")
                val time = prop.getString("time")
                val t = time.toLong()
                //                Date date = new Date(t);
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM DD, yyyy");
//                String dateToD = simpleDateFormat.format(date);
                val formatter = SimpleDateFormat("d MMM yyyy")
                val dateToD = formatter.format(t)
                val report = report(mag, place, dateToD, url)
                earthquakes.add(report)
            }
        } catch (e: JSONException) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e)
        }

        // Return the list of earthquakes
        return earthquakes
    }
}