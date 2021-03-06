package com.khtn.ratevid.adminScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khtn.ratevid.FirebaseUtil
import com.khtn.ratevid.R
import com.khtn.ratevid.adapter.UserAdapter
import com.khtn.ratevid.model.userItem
import kotlinx.android.synthetic.main.activity_list_user.*

class ListUserActivity : AppCompatActivity() {
    lateinit var userList : ArrayList<userItem>
    lateinit var adapter: UserAdapter
    lateinit var customListView: RecyclerView
    var tempUser=ArrayList<userItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        userList=ArrayList<userItem>()
        loadUser()
        setupLayout()
        searchChange()
    }
    private fun searchChange() {
        searchName.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true

            }
            override fun onQueryTextChange(p0: String?): Boolean {
                userList.clear()
                var text=p0.toString()
                if (text.isEmpty()) {
                    userList.addAll(tempUser)
                    adapter.notifyDataSetChanged()

                } else {
                    text = text.toLowerCase()
                    for (user in tempUser) {
                        if (user.UserName?.toLowerCase()?.contains(text) == true || user.status?.toLowerCase()?.contains(text) == true) {
                            userList.add(user)
                        }
                    }
                    adapter.notifyDataSetChanged()

                }
                return false
            }

        })    }

    private fun setupLayout() {
        adapter = UserAdapter(this, userList)
        customListView = findViewById<RecyclerView>(R.id.userRV)
        customListView?.adapter = adapter
        customListView?.layoutManager = LinearLayoutManager(this)    }

    private fun loadUser() {
        FirebaseUtil.readUserList(object: FirebaseUtil.FirebaseCallbackUserList{
            override fun onCallback(arrayUserList: ArrayList<userItem>) {
                userList.clear()
                userList.addAll(arrayUserList)
                tempUser.clear()
                for (user in userList) {
                    tempUser.add(user)
                }
                adapter.notifyDataSetChanged()
            }
        })
    }
}