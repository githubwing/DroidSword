package net.androidwing.droidsword.hooker

import android.app.AlertDialog
import android.app.AndroidAppHelper
import android.app.Dialog
import android.content.DialogInterface
import android.view.MotionEvent
import android.view.View
import android.widget.*
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import net.androidwing.droidsword.Config
import net.androidwing.droidsword.func.TextViewChanger
import net.androidwing.droidsword.func.ViewEnabler
import net.androidwing.droidsword.utils.LogUtils
import net.androidwing.droidsword.utils.XSP

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
        val event = param.args!![0] as MotionEvent
        if (event.action == MotionEvent.ACTION_UP) {
          try {
            val listener = XposedHelpers.getObjectField(
                XposedHelpers.getObjectField(view, "mListenerInfo"),
                "mOnClickListener").javaClass.name

            ActivityHooker.setActionInfoToMenu("",
                "${view.javaClass.name} ${view.id} \nListener: $listener")

            antiDisable(view)
          } catch (e: Exception) {

          }

        }
      }

    })

    XposedHelpers.findAndHookMethod(View::class.java,
        "dispatchTouchEvent",
        MotionEvent::class.java, object : XC_MethodHook() {
      override fun afterHookedMethod(param: MethodHookParam?) {
        super.afterHookedMethod(param)
        val view = param?.thisObject as View
        val event = param.args!![0] as MotionEvent
        if (event.action == MotionEvent.ACTION_DOWN) {
          if (view is AdapterView<*>) {

            val listener = XposedHelpers.getObjectField(view, "mOnItemClickListener").javaClass.name

            ActivityHooker.setActionInfoToMenu("",
                "${view.javaClass.name} ${view.id} \nListener: $listener")
          }

        }
      }

    })

    XposedHelpers.findAndHookMethod(View::class.java,
        "onTouchEvent",
        MotionEvent::class.java, object : XC_MethodHook() {
      override fun afterHookedMethod(param: MethodHookParam?) {
        super.afterHookedMethod(param)
        val targetView = param?.thisObject as View
        if (true) {
          showChangeTextDialog(targetView, param)
        }
      }

    })

  }

  private fun antiDisable(view: View) {

    if (XSP.getBoolean(Config.KEY_VIEW_ENABLE,false)) {
      ViewEnabler.antiDisable(view)
    }
  }

  /**
   * 文本修改神器功能
   */
  private fun showChangeTextDialog(targetView: View,
      param: XC_MethodHook.MethodHookParam) {
    val event = param.args!![0] as MotionEvent

    if (XSP.getBoolean(Config.KEY_TEXT_CHANGER,false)) {
      TextViewChanger.showChangeDialog(targetView, event)
    }
  }


}