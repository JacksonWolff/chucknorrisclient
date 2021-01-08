package com.jacksonwolff.chucknorris.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jacksonwolff.chucknorris.R
import com.jacksonwolff.chucknorris.data.model.ChuckNorrisFact
import kotlinx.android.synthetic.main.fact_item_layout.view.*

class SearchAdapter(private var factsList: MutableList<ChuckNorrisFact>) :
    RecyclerView.Adapter<SearchAdapter.DataViewHolder>() {

    private var factsFilterList = mutableListOf<ChuckNorrisFact>()

    init {
        factsFilterList = factsList
    }


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(fact: ChuckNorrisFact) {
            itemView.apply {
                info_text.text = fact.value
                Glide.with(iconImageView.context)
                    .load(fact.iconUrl)
                    .into(iconImageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fact_item_layout, parent, false))



    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(factsList[position])
    }


    fun addFacts(newItems: MutableList<ChuckNorrisFact>) {
        factsList.clear()
        factsList.addAll(newItems)
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int = factsList.size

}