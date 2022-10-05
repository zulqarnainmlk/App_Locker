package helper

import android.content.Context

object Sharepref {
    var sharedPrefKey="Notes"
    @JvmStatic
    fun setBoolean(context: Context, key: String, value: Boolean) {
        val editor = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    @JvmStatic
    fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
        val sharedPreference = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE)
        return sharedPreference.getBoolean(key, defaultValue)
    }

    @JvmStatic
    fun setString(context: Context,key: String, value: String)
    {
        val editor = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE).edit()
        editor.putString(key,value)
        editor.apply()
    }
    @JvmStatic
    fun getString(context: Context, key: String, defaultValue: String): String? {
        val sharedPreference = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE)
        return sharedPreference.getString(key,defaultValue)
    }

    fun setInt(context: Context, key: String, value: Int) {
        val editor = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE).edit()
        editor.putInt(key, value)
        editor.apply()
    }
    fun clearDB(context: Context)
    {
        val editor = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
}