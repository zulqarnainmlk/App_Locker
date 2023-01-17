package helper

import android.content.Context

object Sharepref { var sharedPrefKey="Notes"
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
    @JvmStatic
    fun setInt(context: Context, key: String, value: Int) {
        val editor = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE).edit()
        editor.putInt(key, value)
        editor.apply()
    }
    @JvmStatic
    fun getInt(context: Context, key: String, defaultValue: Int): Int {
        val sharedPreference = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE)
        return sharedPreference.getInt(key,defaultValue)
    }
    fun setFloat(context: Context, key: String, value: Float) {
        val editor = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE).edit()
        editor.putFloat(key, value)
        editor.apply()
    }
    @JvmStatic
    fun getFloat(context: Context, key: String, defaultValue: Float): Float {
        val sharedPreference = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE)
        return sharedPreference.getFloat(key,defaultValue)
    }
    fun clearDB(context: Context)
    {
        val editor = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
}