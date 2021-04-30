package com.rak12.gymapp.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rak12.gymapp.Adapter.CuisineAdapter
import com.rak12.gymapp.Adapter.RecipieAdapter
import com.rak12.gymapp.Model.Cuisine
import com.rak12.gymapp.R
import com.rak12.gymapp.Util.ConnectionManager
import org.json.JSONObject

class CuisineDetailsActivity : AppCompatActivity() {
    lateinit var rv: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter: RecipieAdapter
    lateinit var toolbar: Toolbar
    lateinit var pl: RelativeLayout
    val recilist = arrayListOf<Cuisine>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuisine_details)
        rv = findViewById(R.id.allrecipierecycler)
        toolbar = findViewById(R.id.toolbar)
        pl = findViewById(R.id.pl)
        setuptoolbar()
       title=intent.getStringExtra("name")



        layoutManager = LinearLayoutManager(this)


        val que = Volley.newRequestQueue(this)
        val url = "https://young-stream-54945.herokuapp.com/recipies"
        val jsonParams = JSONObject()
        jsonParams.put("name", intent.getStringExtra("name"))
        if (ConnectionManager().checkconnectivity( this)) {

            val jsonObjectRequest = object : JsonObjectRequest(Method.POST,url,jsonParams, Response.Listener {
                pl.visibility = View.GONE
                Log.d("hell",it.toString())
                val data = it.getJSONArray("data")
                for (i in 0 until data.length()) {
                    val jsonObject = data.getJSONObject(i)
                    val Recipie = Cuisine(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("img"),

                    )
                   recilist.add(Recipie)
                    adapter= RecipieAdapter(this,recilist)
                    rv.adapter = adapter
                    rv.layoutManager = layoutManager
                }
            },
                    Response.ErrorListener {  }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    return headers
                }

            }
            que.add(jsonObjectRequest)
        } else {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Error")
            alert.setMessage("INTERNET connection not found")
            alert.setPositiveButton("open settings") { text, listener ->
                val i = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(i)
                this?.finish()


            }
            alert.setNegativeButton("exit") { text, listener ->
                ActivityCompat.finishAffinity(this)

            }
            alert.create().show()


        }




//        recilist.add((Cuisine(1,"kaju","ss")))
//        recilist.add((Cuisine(2,"noodle","s")))
//        recilist.add((Cuisine(3,"pasta","s")))
//        recilist.add((Cuisine(4,"bread","x")))
//        adapter= RecipieAdapter(this,recilist)
//        rv.adapter = adapter
//        rv.layoutManager = layoutManager


    }
    fun setuptoolbar() {
        setSupportActionBar(toolbar)


        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onSupportNavigateUp(): Boolean {
        val i = Intent(this, DashboardActivity::class.java)
        startActivity(i)

        return true
    }
}