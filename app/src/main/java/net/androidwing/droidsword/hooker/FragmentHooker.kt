package net.androidwing.droidsword.hooker

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import net.androidwing.droidsword.utils.LogUtils


/**
 * Created  on 30/10/2017.
 */
class FragmentHooker : IHooker {
  override fun hook(lp: XC_LoadPackage.LoadPackageParam) {


  }

  fun hookFragment(param: XC_MethodHook.MethodHookParam?) {
    try {
      param?.thisObject?.javaClass?.classLoader?.loadClass("android.support.v4.app.Fragment")
    } catch (e: Exception) {
      return
    }
    XposedHelpers.findAndHookMethod(
        param?.thisObject?.javaClass?.classLoader?.loadClass("android.support.v4.app.Fragment"),
        "onResume",
        object : XC_MethodHook() {
          override fun afterHookedMethod(param: MethodHookParam?) {
            super.afterHookedMethod(param)
            ActivityHooker.sFragmentName = (param?.thisObject?.javaClass?.name!!)
            ActivityHooker.setActionInfoToMenu("", "")

          }

          override fun beforeHookedMethod(param: MethodHookParam?) {
            super.beforeHookedMethod(param)
          }
        })

    XposedHelpers.findAndHookMethod(
        param?.thisObject?.javaClass?.classLoader?.loadClass("android.support.v4.app.Fragment"),
        "setUserVisibleHint", Boolean::class.java,
        object : XC_MethodHook() {
          override fun afterHookedMethod(param: MethodHookParam?) {
            super.afterHookedMethod(param)
            if (param?.args!![0] == true) {
              LogUtils.e("fragment showing:")
              LogUtils.e("fragment ${param?.thisObject?.javaClass?.name}")
              ActivityHooker.sFragmentName = (param?.thisObject?.javaClass?.name!!)
              ActivityHooker.setActionInfoToMenu("", "")
            }
          }

          override fun beforeHookedMethod(param: MethodHookParam?) {
            super.beforeHookedMethod(param)
          }
        })
  }

}