package net.androidwing.droidsword.utils

import de.robv.android.xposed.XSharedPreferences
import net.androidwing.droidsword.Config

/**
 * Created  on 2018/12/26.
 */
object XSP {

  fun getBoolean(key: String, defaultValue: Boolean):Boolean {
    //防止缓存 每次都new
    val xsp = XSharedPreferences(Config.PACKAGE_NAME, Config.SP_NAME)
    xsp.makeWorldReadable()
    return xsp.getBoolean(key,defaultValue)
  }

  fun getString(key: String, defaultValue: String):String {
    //防止缓存 每次都new
    val xsp = XSharedPreferences(Config.PACKAGE_NAME, Config.SP_NAME)
    xsp.makeWorldReadable()
    return xsp.getString(key,defaultValue)
  }
}