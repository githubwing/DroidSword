package net.androidwing.droidsword.utils

import android.util.Log
import de.robv.android.xposed.XposedBridge

/**
 * Created  on 28/10/2017.
 */
class LogUtils {

  companion object {
    private val TAG = "DROID_SWORD"
    fun e(text: String) {
      Log.e(TAG, text)
    }
  }
}