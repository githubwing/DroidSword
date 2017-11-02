package net.androidwing.droidsword.hooker

import android.view.MotionEvent
import android.view.View
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created  on 28/10/2017.
 */
class ViewClickedHooker : IHooker {
  override fun hook(lp: XC_LoadPackage.LoadPackageParam) {
    XposedHelpers.findAndHookMethod(View::class.java,
        "onTouchEvent",
        MotionEvent::class.java, object : XC_MethodHook() {
      override fun afterHookedMethod(param: MethodHookParam?) {
        super.afterHookedMethod(param)
        val view = param?.thisObject as View
        val event = param?.args!![0] as MotionEvent
        if (event.action == MotionEvent.ACTION_UP) {

          ActivityHooker.setActionInfoToMenu("", "${view.javaClass.name} ${view.id} ")

          antiDisable(view)


        }
      }

    })

  }

  /**
   * 灰色按钮克星，
   * TODO moren 默认开启待添加配置文件
   */
  private fun antiDisable(view: View) {
    if (true) {
      if (view.isEnabled.not()) {
        view.isEnabled = true
      }
    }
  }

}