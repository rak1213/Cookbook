package com.rak12.gymapp.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rak12.gymapp.Adapter.CuisineAdapter
import com.rak12.gymapp.Model.Cuisine
import com.rak12.gymapp.R


class HomeFragment : Fragment() {
    lateinit var rv: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter: CuisineAdapter
    lateinit var sp:SharedPreferences

    val cuilist = arrayListOf<Cuisine>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home, container, false)
        rv = view.findViewById(R.id.allcuirecycler)
       sp= context!!.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        layoutManager = LinearLayoutManager(activity)
          var isloggedin=sp.getBoolean("Isloggedin",false)
        cuilist.add((Cuisine(1,"Indian","s")))
        cuilist.add((Cuisine(2,"Chinese","s")))
        cuilist.add((Cuisine(3,"Italian","s")))
        cuilist.add((Cuisine(4,"French","s")))
        adapter= CuisineAdapter(activity as Context,cuilist,isloggedin)
        rv.adapter = adapter
        rv.layoutManager = layoutManager

        return view
    }


    }
