package com.junseo.postboy.util.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONException

object PrefsUtil {

    private const val TAG = "PreferenceUtil"

    lateinit var prefs: SharedPreferences

    fun createPrefs(context: Context) {
        prefs = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)
    }

    private val editor by lazy { prefs.edit() }

    /* DEFAULT SAVE LOAD FUNCTIONS START */
    fun save(key: String, value: String) = editor.putString(key, value).apply()

    fun save(key: String, value: Boolean) = editor.putBoolean(key, value).apply()

    fun save(key: String, value: Float) = editor.putFloat(key, value).apply()

    fun save(key: String, value: Int) = editor.putInt(key, value).apply()

    fun save(key: String, value: Long) = editor.putLong(key, value).apply()

    fun save(key: String, value: Set<String>) = editor.putStringSet(key, value).apply()

    fun load(key: String, defValue: String) = prefs.getString(key, defValue).toString()

    fun load(key: String, defValue: Boolean) = prefs.getBoolean(key, defValue)

    fun load(key: String, defValue: Float) = try {
        prefs.getFloat(key, defValue)
    } catch (ex: ClassCastException) {
        prefs.getString(key, defValue.toString())!!.toFloat()
    }

    fun load(key: String, defValue: Int) = try {
        prefs.getInt(key, defValue)
    } catch (ex: ClassCastException) {
        prefs.getString(key, defValue.toString())!!.toInt()
    }

    fun load(key: String, defValue: Long) = try {
        prefs.getLong(key, defValue)
    } catch (ex: ClassCastException) {
        prefs.getString(key, defValue.toString())!!.toLong()
    }

    fun load(key: String, defValue: Set<String>): Set<String>? = prefs.getStringSet(key, defValue)

    fun remove(key: String) = run { editor.remove(key).commit(); }


    fun getAll(): MutableMap<String, *>? = prefs.all

    fun clear() = editor.clear().apply()
    /* DEFAULT SAVE LOAD FUNCTIONS END */

}


