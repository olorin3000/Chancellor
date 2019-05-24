package com.chancellor.api

class DataBase {

    private val mUsersList = mutableListOf<User>()

    init {
        val firstUser = User(
            0, "+79530000953",
            "semeniukartem@gmail.com",
            "Artem", "Semeniuk",
            "ul.Pirohova 45 a1", "123456"
        )
        mUsersList.add(firstUser)
    }

    fun addUser(user: User) : Boolean{

        mUsersList.forEach {
            if (it.email == user.email || it.phone == user.phone)
                return false
        }

        mUsersList.add(user)

        return true
    }

    fun getUserById(index: Int) : User{
        return mUsersList[index]
    }

    fun getAllUsers(): MutableList<User>{
        return mUsersList
    }

    fun getUserId(user: User) : Int{
        return mUsersList.indexOf(user)
    }

}