package com.example.fierce.sharedPref

import android.content.Context

class PrefManager private constructor(context: Context){
    private val sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    companion object{
        private const val PREFS_FILENAME = "AuthAppRef"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_ADDRESS = "address"
        private const val KEY_PHONE = "phone"
        private const val KEY_PROVINCE = "province"
        private const val KEY_EMAIL = "email"


        // Hardcoded admin credentials
        private const val ADMIN_USERNAME = "admin"
        private const val ADMIN_PASSWORD = "admin123"



        @Volatile
        private var instance:PrefManager? = null
        fun getInstance(context: Context): PrefManager{
            return instance ?: synchronized(this){
                instance ?: PrefManager(context.applicationContext).also { pref ->
                    instance = pref
                }
            }
        }
    }

    fun saveUser(username: String, password: String, email: String, phone: String){
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PASSWORD, password)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_PHONE, phone)
        editor.apply()
    }

    fun saveAddress(address: String, province: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ADDRESS, address)
        editor.putString(KEY_PROVINCE, province)
        editor.apply()
    }

    fun getUsername(): String?{
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    fun saveUsername(username: String){
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    fun getPassword(): String?{
        return sharedPreferences.getString(KEY_PASSWORD, null)
    }

    fun getProvince(): String?{
        return sharedPreferences.getString(KEY_PROVINCE,null)
    }

    fun getAddress(): String?{
        return sharedPreferences.getString(KEY_ADDRESS,null)
    }


    fun getEmail(): String?{
        return sharedPreferences.getString(KEY_EMAIL, null)
    }

    fun clearUser(){
        sharedPreferences.edit().also {
            it.clear()
            it.apply()
        }
    }

    fun isAdmin(username: String, password: String): Boolean {
        return username == ADMIN_USERNAME && password == ADMIN_PASSWORD
    }

}