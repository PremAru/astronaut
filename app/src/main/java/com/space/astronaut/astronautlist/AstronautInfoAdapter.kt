package com.space.astronaut.astronautlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.space.astronaut.R
import com.space.astronaut.model.Results
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_astronaunt_list.view.*
import javax.inject.Inject


class AstronautInfoAdapter @Inject constructor(private val picasso: Picasso) :
    RecyclerView.Adapter<AstronautInfoAdapter.RepositoryViewHolder>() {
    private var astronautInfoList: MutableList<Results> = mutableListOf()

    var userInfoListClickListener: AstronautInfoClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_astronaunt_list, parent,
            false
        )
        return RepositoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return astronautInfoList.size
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        astronautInfoList[position]?.let { holder.bindFilter(it) }
    }

    inner class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindFilter(results: Results) {
            itemView.astronautNameTextView.text =
                results.name
            itemView.nationalityTextView.text =
                results.nationality

            picasso.load(results.profile_image_thumbnail)
                .placeholder(R.drawable.loading_image)
                .into(itemView.astronautImageView)

            itemView.adapterConstraintLayout.setOnClickListener {
                val userInfo = astronautInfoList[adapterPosition]
                userInfoListClickListener?.userInfoListClicked(userInfo)
            }
        }

    }

    fun setUserInfoList(astronautInfoListData: List<Results>) {
        astronautInfoList.clear()
        astronautInfoList.addAll(astronautInfoListData)
        notifyDataSetChanged()
    }


    fun setClickListener(listener: AstronautInfoClickListener) {
        userInfoListClickListener = listener
    }

}