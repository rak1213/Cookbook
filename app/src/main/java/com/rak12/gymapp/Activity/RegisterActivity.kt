package com.rak12.gymapp.Activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rak12.gymapp.R
import com.rak12.gymapp.Util.ConnectionManager
import com.rak12.gymapp.Util.Validations
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    lateinit var etname: EditText
    lateinit var etmobile: EditText
    lateinit var etemail: EditText
    lateinit var register: Button
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sp = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)

        etname = findViewById(R.id.name)
        etmobile = findViewById(R.id.mobile)
        etemail = findViewById(R.id.email)
        register = findViewById(R.id.register)

        register.setOnClickListener {

            val queue = Volley.newRequestQueue(this)

            val url = " "
            val jsonParams = JSONObject()
            val name = etname.text.toString()
            val mobile = etmobile.text.toString()

            val email = etemail.text.toString()
            jsonParams.put("mobile_number", mobile)
            jsonParams.put("name", name)

            jsonParams.put("email", email)

            if (Validations.validateNameLength(name)) {
                if (Validations.validateEmail(email)) {
                    if (Validations.validateMobile(mobile)) {

                                if (ConnectionManager().checkconnectivity(this)) {

                                    val jsonRequest =
                                            object : JsonObjectRequest(
                                                    Method.POST,
                                                    url,
                                                    jsonParams,
                                                    Response.Listener {
                                                        try {
                                                            val data = it.getJSONObject("data")
                                                            val success =
                                                                    data.getBoolean("success")

                                                            if (success) {

                                                                val data1 =
                                                                        data.getJSONObject("data")
                                                                sp.edit().putString(
                                                                        "user_id",
                                                                        data1.getString("_id")
                                                                ).apply()
                                                                sp.edit().putString(
                                                                        "name",
                                                                        data1.getString("name")
                                                                ).apply()
                                                                sp.edit().putString(
                                                                        "email",
                                                                        data1.getString("email")
                                                                ).apply()

                                                                sp.edit().putString(
                                                                        "mobile_number",
                                                                        data1.getString("mobile_number")
                                                                ).apply()
                                                                saveprefrences()
                                                                Toast.makeText(
                                                                        this,
                                                                        "REGISTRATION SUCCESS",
                                                                        Toast.LENGTH_SHORT
                                                                ).show()
                                                                val i = Intent(
                                                                        this,
                                                                        OtpVerificationActivity::class.java
                                                                )
                                                                startActivity(i)
                                                                finish()
                                                            } else {
                                                                Toast.makeText(
                                                                        this,
                                                                        "SOMETHING WENT WRONG",
                                                                        Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        } catch (e: JSONException) {
                                                            Toast.makeText(
                                                                    this,
                                                                    "Error1212",
                                                                    Toast.LENGTH_SHORT
                                                            ).show()

                                                        }
                                                    },
                                                    Response.ErrorListener { }) {
                                                override fun getHeaders(): MutableMap<String, String> {
                                                    val headers = HashMap<String, String>()
                                                    headers["Content-Type"] = "application/json"
                                                    return headers
                                                }
                                            }
                                    queue.add(jsonRequest)
                                } else {

                                    val alert = AlertDialog.Builder(this)
                                    alert.setTitle("Error")
                                    alert.setMessage("INTERNET connection not found")
                                    alert.setPositiveButton("open settings") { text, listener ->
                                        val i = Intent(Settings.ACTION_WIFI_SETTINGS)
                                        startActivity(i)
                                        this.finish()


                                    }
                                    alert.setNegativeButton("exit") { text, listener ->
                                        ActivityCompat.finishAffinity(this)

                                    }
                                    alert.create().show()


                                }

                    } else {
                        etmobile.error = "Invalid mobile number"

                    }
                } else {
                    etemail.error = "Invalid email-id"
                }

            } else {
                etname.error = "Name should be of atleast 4 characters"
            }
        }
    }

    fun saveprefrences() {
        sp.edit().putBoolean("Isloggedin", true).apply()
    }

}