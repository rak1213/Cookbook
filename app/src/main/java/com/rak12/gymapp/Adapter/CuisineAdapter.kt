package com.rak12.gymapp.Adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.rak12.gymapp.Activity.CuisineDetailsActivity
import com.rak12.gymapp.Database.CuisineEntity
import com.rak12.gymapp.Database.Database
import com.rak12.gymapp.Model.Cuisine
import com.rak12.gymapp.R
import com.squareup.picasso.Picasso

class CuisineAdapter( val context: Context,val arrayList: ArrayList<Cuisine>,var isloggedin:Boolean):RecyclerView.Adapter<CuisineAdapter.Vh> () {
    class Vh(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.cardcui)
        var cuiname: TextView = view.findViewById(R.id.txtCuiName)

        var imgfav: ImageView = view.findViewById(R.id.imgIsFav)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuisineAdapter.Vh {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_cui, parent, false)
        return Vh(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: CuisineAdapter.Vh, position: Int) {
        holder.imgfav.visibility=View.GONE
        var info: Cuisine = arrayList[position]
        holder.cuiname.text = info.name
        var id = info.id

        holder.card.setOnClickListener {
            val i = Intent(context, CuisineDetailsActivity::class.java)
            i.putExtra("cid", info.id)
            i.putExtra("name", info.name)
            context.startActivity(i)
        }
         if(!isloggedin){
             holder.imgfav.visibility=View.GONE

         }
        else{
             holder.imgfav.visibility=View.VISIBLE
        val cuisineEntity =
            CuisineEntity(info.id, info.name, info.img)

        val checkfav = DBAsynctask(context, cuisineEntity, 1).execute().get()
        if (checkfav) {
            holder.imgfav.setBackgroundResource(R.drawable.ic_baseline_fiiledfav)
        } else {
            holder.imgfav.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
        }
        holder.imgfav.setOnClickListener {
            if (!DBAsynctask(context, cuisineEntity, 1).execute().get()) {
                val addtofav = DBAsynctask(context, cuisineEntity, 2).execute().get()
                if (addtofav) {
                    Toast.makeText(context, "ADDED TO FAVS", Toast.LENGTH_SHORT).show()

                    holder.imgfav.setBackgroundResource(R.drawable.ic_baseline_fiiledfav)
                } else {
                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show()

                }
            } else {
                val removefav = DBAsynctask(context, cuisineEntity, 3).execute().get()
                if (removefav) {
                    Toast.makeText(context, "REMOVED FROM FAVS", Toast.LENGTH_SHORT).show()

                    holder.imgfav.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
                } else {
                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show()

                }


            }
        }

    }}

    class DBAsynctask(val context: Context, val cuiEntity: CuisineEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        val db =
            Room.databaseBuilder(context, Database::class.java, "db")
                .build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    val restaurantEntity1: CuisineEntity =
                        db.cusdao().getbyid(cuiEntity.id)
                    db.close()
                    return restaurantEntity1 != null
                }
                2 -> {
                    db.cusdao().insert(cuiEntity)
                    db.close()
                    return true
                }
                3 -> {
                    db.cusdao().delete(cuiEntity)
                    db.close()
                    return true
                }
            }
            return false
        }


    }
}



