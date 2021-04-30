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
import com.rak12.gymapp.Database.CuisineEntity
import com.rak12.gymapp.R
import com.squareup.picasso.Picasso

class FavAdapter(val context: Context, val list: List<CuisineEntity>) :
    RecyclerView.Adapter<FavAdapter.FavViewholde>() {
    class FavViewholde(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.cardcui)
        var cuiname: TextView = view.findViewById(R.id.txtCuiName)

        var imgfav: ImageView = view.findViewById(R.id.imgIsFav)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewholde {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_cui, parent, false)
        return FavViewholde(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FavViewholde, position: Int) {
        var info: CuisineEntity = list[position]
        holder.cuiname.text = info.name


        val cuisineEntity =
            CuisineEntity(info.id, info.name, info.img)
        val checkfav = CuisineAdapter.DBAsynctask(context, cuisineEntity, 1).execute().get()
        if (checkfav) {
            holder.imgfav.setBackgroundResource(R.drawable.ic_baseline_fiiledfav)
        } else {
            holder.imgfav.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
        }
        holder.card.setOnClickListener {
            val i = Intent(context, CuisineDetailsActivity::class.java)
            i.putExtra("id", info.id)
            i.putExtra("name", info.name)
            context.startActivity(i)
        }


    }


}
