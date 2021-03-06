package com.dzakyhdr.moviedb.data.local.auth

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository (private val userDao: UserDao) {

    suspend fun save(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
    }

    suspend fun update(user: User) {
        withContext(Dispatchers.IO) {
            userDao.update(user)
        }
    }

    suspend fun getUser(id: Int): User {
        return userDao.getUser(id)
    }

    suspend fun verifyLogin(email: String, password: String): User {
        return userDao.readLogin(email, password)
    }
}