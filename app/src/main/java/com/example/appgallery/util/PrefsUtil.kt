package com.seven.util

import android.content.Context
import android.content.SharedPreferences
import com.example.appgallery.ui.auth.model.ResponseLoginPhone
import com.seven.util.PrefsModel.TOKEN
import com.seven.util.PrefsModel.shareredPrefName
import com.seven.util.PrefsModel.userModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seven.util.PrefsModel.BRIDECOUNT
import com.seven.util.PrefsModel.localLanguage

class PrefsUtil {

    fun getUserToken(ctx: Context): String? {
        val sp = ctx.getSharedPreferences(
            shareredPrefName, Context.MODE_PRIVATE)
        var value = sp.getString(TOKEN, "")
       /* try {
            value = AESUtils.decrypt(value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
        return value
    }

    fun removeKey(ctx: Context, key: String?) {
        val sp = ctx.getSharedPreferences(
            shareredPrefName,
            Context.MODE_PRIVATE
        )
        sp.edit().remove(key).apply()
    }
    fun getSharedPrefs(ctx: Context) : SharedPreferences {
      return ctx.getSharedPreferences(
            shareredPrefName,
            Context.MODE_PRIVATE
        )
    }
    fun setLoginState(ctx: Context, isLoggedIn: Boolean) {
        val sp = ctx.getSharedPreferences(
            shareredPrefName, Context.MODE_PRIVATE)
        sp.edit().putBoolean(PrefsModel.isLoggedIn, isLoggedIn).apply()
    }


    fun setUserToken(ctx: Context, token: String?) {
        val sp = ctx.getSharedPreferences(
            shareredPrefName, Context.MODE_PRIVATE)
        val encrypted = token
       /* if (BuildConfig.DEBUG) {
            sp.edit().putString("$TOKEN-DEBUG", PrefsModel.BEARER + encrypted).apply()
        }*/
      /*  try {
            encrypted = AESUtils.encrypt(PrefsModel.BEARER + encrypted!!)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }*/
        sp.edit().putString(TOKEN,/* PrefsModel.BEARER +*/ encrypted).apply()
    }

    fun setLoginModel(context: Context, responseLogin: Any?) {
        val gson = Gson()
        getSharedPrefs(context)
            .edit().putString(userModel, gson.toJson(responseLogin ?: Any())
        ).apply()
    }

  fun getUserModel(ctx: Context): ResponseLoginPhone? { // this should return the object
        val jso = getSharedPrefs(ctx).getString(userModel, "") // get the overall object please
        val gson = Gson()
        val typeToken = object : TypeToken<ResponseLoginPhone?>() {}.type
        val obj = gson.fromJson<ResponseLoginPhone>(jso, typeToken) ?:null //ResponseLogin(Data("", null))
        return obj

    }
  /*  fun setOrderModel(context: Context, orderResponse: RequestOrderBody?) {
        val sp = context.getSharedPreferences(
            PrefsModel.shareredPrefName,
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        sp.edit().putString(NameUtils.orderData, gson.toJson(orderResponse ?: RequestOrderBody()))
            .apply()
    }*/





    fun isLoggedIn(ctx: Context): Boolean {
        val sp = ctx.getSharedPreferences(
            shareredPrefName, Context.MODE_PRIVATE)
        return sp.getBoolean(PrefsModel.isLoggedIn, false)
    }


    fun setLanguage(ctx: Context, value: String?) {
        val sp = ctx.getSharedPreferences(
            shareredPrefName,
            Context.MODE_PRIVATE
        )
        var encrypted = value
     /*   try {
            encrypted = AESUtils.encrypt(value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
        sp.edit().putString(localLanguage, value).apply()
    }

    fun getLanguage(ctx: Context): String? {
        val sp = ctx.getSharedPreferences(
            shareredPrefName,
            Context.MODE_PRIVATE
        )
        var value = sp.getString(localLanguage, "en")
      /*  try {
            value = AESUtils.decrypt(value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
        return value
    }

    fun setString(
        ctx: Context,
        key: String?,
        value: String?
    ) {
//        val sp = ctx.getSharedPreferences(
//            PREF_NAME,
//            Context.MODE_PRIVATE
//        )
        var encrypted = value
    /*    try {
            encrypted = AESUtils.encrypt(value!!)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }*/
        getSharedPrefs(ctx).edit().putString(key, value).apply()
    }

    fun getCurrentBadgeNumber(applicationContext: Context?): Int {
        val sp = applicationContext?.getSharedPreferences(
                shareredPrefName,
                Context.MODE_PRIVATE
        )
        val value = sp?.getInt(BRIDECOUNT,0)?:0
        /*  try {
              value = AESUtils.decrypt(value!!)
          } catch (e: Exception) {
              e.printStackTrace()
          }*/
        return value
    }
    fun setCurrentBadge(applicationContext: Context?) {
        val sp = applicationContext?.getSharedPreferences(
                shareredPrefName,
                Context.MODE_PRIVATE
        )
   sp?.edit()?.putInt(BRIDECOUNT,1)?.apply()
        /*  try {
              value = AESUtils.decrypt(value!!)
          } catch (e: Exception) {
              e.printStackTrace()
          }*/
    }


}