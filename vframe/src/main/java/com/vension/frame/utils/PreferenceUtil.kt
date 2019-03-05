package com.vension.frame.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.vension.frame.VFrame
import java.io.*
import kotlin.reflect.KProperty

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/10/31 10:06
 * 描  述：kotlin委托属性+SharedPreference实例
 *        commit方法属于属于阻塞性质API，建议使用apply。
 * ========================================================
 */

class PreferenceUtil<T>(private val name: String, private val default: T){

    companion object {

        private const val fileName = "SP.do"

        private val mSharedPreferences: SharedPreferences by lazy {
            VFrame.getContext().getSharedPreferences(fileName, Context.MODE_PRIVATE)
        }

        /**
         * 查询某个key是否已经存在
         *
         * @param key
         * @return
         */
        fun contains(key: String): Boolean {
            return mSharedPreferences.contains(key)
        }

        /**
         * 删除全部数据
         */
        fun clear() {
            mSharedPreferences.edit().clear().apply()
        }

        /**
         * 根据key删除存储数据
         */
        fun remove(key: String) {
            mSharedPreferences.edit().remove(key).apply()
        }

        /**
         * 返回所有的键值对
         *
         * @param context
         * @return
         */
        fun getAll(): Map<String, *> {
            return mSharedPreferences.all
        }

    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = getSharedPreferences(name, default)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = putSharedPreferences(name, value)

    @SuppressLint("CommitPrefEdits")
    private fun <T> putSharedPreferences(name: String, value: T) = with(mSharedPreferences.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }.apply()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getSharedPreferences(name: String, default: T): T = with(mSharedPreferences) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> deSerialization(getString(name, serialize(default)))
        }
        return res as T
    }

    /**
     * 序列化对象

     * @param person
     * *
     * @return
     * *
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun <A> serialize(obj: A): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(
            byteArrayOutputStream)
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return serStr
    }

    /**
     * 反序列化对象

     * @param str
     * *
     * @return
     * *
     * @throws IOException
     * *
     * @throws ClassNotFoundException
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun <A> deSerialization(str: String): A {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1")))
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream)
        val obj = objectInputStream.readObject() as A
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }

}