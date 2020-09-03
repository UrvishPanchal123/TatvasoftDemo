package com.tatvasoft.urvishdemo.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tatvasoft.urvishdemo.R
import com.tatvasoft.urvishdemo.databinding.ActivityUserListBinding
import com.tatvasoft.urvishdemo.model.UserModel
import com.tatvasoft.urvishdemo.ui.BaseActivity
import com.tatvasoft.urvishdemo.ui.adapter.UserListAdapter
import com.tatvasoft.urvishdemo.utility.PaginationScrollListener
import com.tatvasoft.urvishdemo.viewmodel.UserListViewModel

class UserListActivity : BaseActivity<ActivityUserListBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var userViewModel: UserListViewModel

    private var userListAdapter: UserListAdapter? = null

    private val PAGE_START = 10
    private var currentOffset = PAGE_START

    private var isLastPage = false
    private var isLoading = false

    val itemCount = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_user_list)
        init()
    }

    private fun init() {
        userViewModel = ViewModelProviders.of(this).get(UserListViewModel::class.java)

        mBinding.swipeRefreshLayout.setOnRefreshListener(this)

        mBinding.recyclerUserList.addOnScrollListener(object :
            PaginationScrollListener(mBinding.recyclerUserList.layoutManager as LinearLayoutManager?) {

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                currentOffset += itemCount

                // make API call to get user list from server
                webCallGetUserList()
            }
        })

        // make API call to get user list from server
        webCallGetUserList()
    }

    /**
     * API call to get user list from server
     */
    private fun webCallGetUserList() {

        if (!mBinding.swipeRefreshLayout.isRefreshing)
            showLoader(this)

        userViewModel.getUserList(currentOffset, itemCount)
            .observe(this, Observer { resultModel ->

                dismissLoader()

                mBinding.swipeRefreshLayout.isRefreshing = false

                if (resultModel == null) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    return@Observer
                }

                if (resultModel.status) {
                    bindUserList(resultModel.data)
//                    mBinding.recyclerUserList.adapter =
//                        UserListAdapter(
//                            this,
//                            resultModel.data?.userList as MutableList<UserModel.User>?
//                        )
                }
            })
    }

    private fun bindUserList(data: UserModel.Data?) {
        isLoading = false

        if (currentOffset == PAGE_START) {
            val mutableList: MutableList<UserModel.User> = ArrayList()
            userListAdapter = UserListAdapter(this, mutableList)
            mBinding.recyclerUserList.adapter = userListAdapter
        }

        if (currentOffset != PAGE_START)
            userListAdapter?.removeLoading()

        if (!data?.userList.isNullOrEmpty())
            userListAdapter?.addAll(data?.userList!!)

        if (data?.hasMore!!)
            userListAdapter?.addLoading()
        else
            isLastPage = true

        if (!data.userList.isNullOrEmpty() && data.userList!!.size < 10)
            userListAdapter?.removeLoading()
    }

    override fun onRefresh() {
        mBinding.swipeRefreshLayout.isRefreshing = true

        currentOffset = PAGE_START

        // make API call to get user list from server
        webCallGetUserList()
    }
}