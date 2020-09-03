package com.tatvasoft.urvishdemo.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tatvasoft.urvishdemo.databinding.ItemUserListBinding
import com.tatvasoft.urvishdemo.databinding.LayoutPaginationBottomLoaderBinding
import com.tatvasoft.urvishdemo.model.UserModel
import com.tatvasoft.urvishdemo.ui.activity.UserListActivity

class UserListAdapter(
    private val mContext: UserListActivity,
    private val data: MutableList<UserModel.User>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private var isLoaderVisible = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            VIEW_TYPE_NORMAL -> {
                val mBinding =
                    ItemUserListBinding.inflate(mContext.layoutInflater, parent, false)
                UserViewHolder(mBinding)
            }

            else -> {
                val mBinding =
                    LayoutPaginationBottomLoaderBinding.inflate(
                        mContext.layoutInflater,
                        parent,
                        false
                    )
                LoadingViewHolder(mBinding)
            }
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == data!!.size - 1)
                VIEW_TYPE_LOADING
            else
                VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    fun add(response: UserModel.User) {
        data!!.add(response)
        notifyItemInserted(data.size - 1)
    }

    fun addAll(postItems: List<UserModel.User>) {
        for (response in postItems) {
            add(response)
        }
    }

    private fun remove(postItems: UserModel.User?) {
        val position = data!!.indexOf(postItems)
        if (position > -1) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addLoading() {
        isLoaderVisible = true
        add(UserModel.User())
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position = data!!.size - 1
        val item = getItem(position)
        if (item != null) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    private fun getItem(position: Int): UserModel.User? {
        return data!![position]
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is UserViewHolder) {
            val holder = viewHolder as UserViewHolder
            holder.bindData(data?.get(holder.adapterPosition))

            val layoutManager = GridLayoutManager(mContext, 2)

            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (data?.get(position)?.items?.size!! % 2 == 0) {
                        2
                    } else {
                        1
                    }
                }
            }

            holder.mBinding.recyclerUserItemList.layoutManager = layoutManager

            val adapter = UserItemAdapter(mContext, data?.get(holder.adapterPosition)?.items)
            holder.mBinding.recyclerUserItemList.adapter = adapter
        }
    }

    class UserViewHolder(var mBinding: ItemUserListBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bindData(data: UserModel.User?) {
            mBinding.user = data
            mBinding.executePendingBindings()
        }
    }

    class LoadingViewHolder(mBinding: LayoutPaginationBottomLoaderBinding) :
        RecyclerView.ViewHolder(mBinding.root)
}