package com.tatvasoft.urvishdemo.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tatvasoft.urvishdemo.R
import com.tatvasoft.urvishdemo.databinding.ItemUserImagesBinding
import com.tatvasoft.urvishdemo.ui.activity.UserListActivity

class UserItemAdapter(
    private val mContext: UserListActivity,
    private val data: ArrayList<String>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mBinding =
            ItemUserImagesBinding.inflate(mContext.layoutInflater, parent, false)
        return UserViewHolder(mBinding)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as UserViewHolder

        Glide.with(mContext)
            .load(data?.get(position))
            .error(R.mipmap.ic_launcher)
            .fallback(R.mipmap.ic_launcher)
            .into(holder.mBinding.imgUserItem)
    }

    class UserViewHolder(var mBinding: ItemUserImagesBinding) :
        RecyclerView.ViewHolder(mBinding.root)
}