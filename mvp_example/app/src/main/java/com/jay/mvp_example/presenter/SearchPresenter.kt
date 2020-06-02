package com.jay.mvp_example.presenter

import android.os.Handler
import com.jay.mvp_example.model.DogList

class SearchPresenter : SearchContract.Presenter{

    private var searchView : SearchContract.View? = null

    override fun takeView(view: SearchContract.View) {
        searchView = view
    }

    override fun getDogList() {
        searchView?.showLoading()

        Handler().postDelayed({
            val dogList = DogList.getDoglistData()
            searchView?.showDogList(dogList)
            searchView?.hideLoading()
        }, 1000)
    }

    override fun dropView() {
        searchView = null
    }

}