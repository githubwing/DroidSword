package net.androidwing.droidsword.utils

import android.util.Log
import de.robv.android.xposed.XposedBridge
import net.androidwing.droidsword.BuildConfig

/**
 * Created  on 28/10/2017.
 */
class LogUtils {

  companion object {
    private var debug = BuildConfig.DEBUG
    private val TAG = "DROID_SWORD"
    fun e(text: String) {
      if (debug) {
        Log.e(TAG, text)
      }
    }
  }
}