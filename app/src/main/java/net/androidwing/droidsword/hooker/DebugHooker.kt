package net.androidwing.droidsword.hooker

import android.os.Process
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage
import net.androidwing.droidsword.Config
import net.androidwing.droidsword.utils.LogUtils
import net.androidwing.droidsword.utils.XSP


/**
 * Created  on 2018/12/26.
 */
class DebugHooker : IHooker {
  override fun hook(lp: XC_LoadPackage.LoadPackageParam) {

    XposedBridge.hookAllMethods(Process::class.java, "start", object : XC_MethodHook() {
      override fun beforeHookedMethod(param: MethodHookParam?) {

        val spEnable = XSP.getBoolean(Config.KEY_APP_DEBUG, true)

        if (!spEnable) {
          return
        }

        var enable = (param!!.args[5] as Int).toInt()
        if (enable and 0x1 === 0) {
          enable = enable or 0x1
        }

        param!!.args[5] = Integer.valueOf(enable)

      }

    })
  }

}