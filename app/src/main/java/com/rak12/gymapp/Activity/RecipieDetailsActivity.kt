package com.rak12.gymapp.Activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rak12.gymapp.Adapter.FeedbackRecyclerAdapter
import com.rak12.gymapp.Adapter.RecipieAdapter
import com.rak12.gymapp.Model.Cuisine
import com.rak12.gymapp.R
import com.rak12.gymapp.Util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class RecipieDetailsActivity : AppCompatActivity() {
    lateinit var name:TextView
    lateinit var time:TextView
    lateinit var img:ImageView
    lateinit var toolbar: Toolbar
    lateinit var pl: RelativeLayout
    lateinit var ta:TextView



    lateinit var submitButton : Button
    lateinit var feedbackText : EditText
    lateinit var feedbackRecycler : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var adapter : FeedbackRecyclerAdapter
    lateinit var previousFeedback : TextView
    lateinit var sp: SharedPreferences
    val feedbacks = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipie_details)
     name=findViewById(R.id.reciname)
        time=findViewById(R.id.time)
        img=findViewById(R.id.reciimg)
        toolbar = findViewById(R.id.toolbar)
        title=intent.getStringExtra("name")
        ta=findViewById(R.id.ta)
        pl = findViewById(R.id.pl)
        name.text=intent.getStringExtra("name")

        sp = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        val isloggedin=sp.getBoolean("Isloggedin",false)
        layoutManager = LinearLayoutManager(this)
        feedbackRecycler = findViewById(R.id.feedbackRecycler)
        feedbackText = findViewById(R.id.feedbackText)
        submitButton = findViewById(R.id.submitFeedback)
        previousFeedback = findViewById(R.id.previousFeedback)
        Picasso.get().load(intent.getStringExtra("img")).error(R.drawable.food).into(img)
        setuptoolbar()

        val que = Volley.newRequestQueue(this)
        val url = "https://young-stream-54945.herokuapp.com/singlereciname"
        val jsonParams = JSONObject()
        jsonParams.put("name", intent.getStringExtra("name"))
        if (ConnectionManager().checkconnectivity( this)) {

            val jsonObjectRequest = object : JsonObjectRequest(Method.POST,url,jsonParams, Response.Listener {
                pl.visibility = View.GONE
                Log.d("hell",it.toString())
                val time1=it.getString("time")
                val recipie=it.getString("recipie")
                time.text=": $time1"
                ta.text=recipie.toString()




            },
                    Response.ErrorListener {  }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    return headers
                }

            }
            que.add(jsonObjectRequest)



            val queue = Volley.newRequestQueue(this)
            val url = "https://young-stream-54945.herokuapp.com/feedback"
            val jsonParams = JSONObject()
            val userId = sp.getString("user_id", null).toString()
            val jsonObjectRequest1 = object : JsonObjectRequest(Method.GET,url,null,Response.Listener {
                val data = it.getJSONArray("data")
                if(data.length()!=0)
                {
                    previousFeedback.visibility = View.VISIBLE
                    for (i in 0 until data.length()) {
                        val jsonObject = data.getJSONObject(i)
                        println(jsonObject)
                        val feedback = jsonObject.getString("feedback")
                        feedbacks.add(feedback)
                    }
                    adapter = FeedbackRecyclerAdapter(this, feedbacks)
                    feedbackRecycler.adapter = adapter
                    feedbackRecycler.layoutManager = layoutManager
                }
            },Response.ErrorListener {
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["user_id"] = userId
                    return headers
                }
            }
            queue.add(jsonObjectRequest1)

            if(!isloggedin){
                submitButton.text="LOG IN to Enter a Comment!"
                submitButton.setOnClickListener {



                    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                    builder.setTitle("Confirmation")
                            .setMessage("LOG IN first to comment")
                            .setPositiveButton("Log in") { _, _ ->
                                sp.edit().clear().apply()
                                startActivity(Intent(this, LoginActivity::class.java))
                                Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
                                ActivityCompat.finishAffinity(this)

                            }
                            .setNegativeButton("No") { _, _ ->


                            }
                            .create()
                            .show()









                }
            }
            else {

                submitButton.setOnClickListener {
                    if (feedbackText.text.isNotEmpty()) {
                        jsonParams.put("feedback", feedbackText.text.toString())
                        val jObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                                feedbackText.setText("")
                                println(it)
                                Toast.makeText(this, "Feedback Submitted Successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                            }
                        }, Response.ErrorListener { }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["user_id"] = userId
                                return headers
                            }
                        }
                        queue.add(jObjectRequest)
                    } else {
                        Toast.makeText(this, "Feedback Field is left blank", Toast.LENGTH_SHORT).show()
                    }
                    feedbackText.clearFocus()
                }


            }





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
    }


    fun setuptoolbar() {
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}