package com.jay.mvp_example.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jay.mvp_example.R
import com.jay.mvp_example.base.BaseActivity
import com.jay.mvp_example.model.Dog
import com.jay.mvp_example.presenter.SearchContract
import com.jay.mvp_example.presenter.SearchPresenter
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(), SearchContract.View {

    private lateinit var searchPresenter: SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchPresenter.takeView(this)

        setButton()
    }
    private fun setButton(){
        getDogListButton.setOnClickListener{
            searchPresenter.getDogList()
        }
    }

    override fun showDogList(dogList : List<Dog>) {
        firstDogText.text = "Name : ${dogList[0].name}, Age : ${dogList[0].age}"
        secondDogText.text = "Name : ${dogList[1].name}, Age : ${dogList[1].age}"
        thirdDogText.text = "Name : ${dogList[2].name}, Age : ${dogList[2].age}"
    }


    override fun onDestroy() {
        super.onDestroy()
        searchPresenter.dropView()
    }

    override fun initPresenter() {
        searchPresenter = SearchPresenter()
    }

    override fun showLoading() {
        searchRefresh.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        searchRefresh.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(this,error, Toast.LENGTH_LONG)
    }
}
