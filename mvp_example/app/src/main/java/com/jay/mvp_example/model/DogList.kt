package com.jay.mvp_example.model

object DogList {
    fun getDoglistData() : List<Dog>{
        return listOf(Dog("장동민", 26),
        Dog("이재석", 26), Dog("덕커피", 22))
    }
}