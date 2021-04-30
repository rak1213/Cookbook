package com.rak12.gymapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rak12.gymapp.Activity.CuisineDetailsActivity
import com.rak12.gymapp.Activity.RecipieDetailsActivity
import com.rak12.gymapp.Model.Cuisine
import com.rak12.gymapp.R
import com.squareup.picasso.Picasso

class RecipieAdapter(val context: Context, val arrayList: ArrayList<Cuisine>): RecyclerView.Adapter<RecipieAdapter.Vh> (){
    class Vh(view: View): RecyclerView.ViewHolder(view){
        val card: CardView = view.findViewById(R.id.cardrec)
        var reciname: TextView = view.findViewById(R.id.txtReciName)
        val img:ImageView=view.findViewById(R.id.imgThumbnail)

        var imgfav: ImageView = view.findViewById(R.id.imgIsFav)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecipieAdapter.Vh {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.singlerecipie, parent, false)
        return Vh(view)
    }



    override fun getItemCount(): Int {
        return  arrayList.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        var info: Cuisine =arrayList[position]
        holder.reciname.text=info.name
        var id=info.id
        Picasso.get().load(info.img).error(R.drawable.food).into(holder.img)
        holder.card.setOnClickListener {
            val i = Intent(context, RecipieDetailsActivity::class.java)
            i.putExtra("cid", info.id)
            i.putExtra("name",info.name.toString())
            i.putExtra("img",info.img)

            context.startActivity(i)
        }
    }
}
