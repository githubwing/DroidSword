package net.androidwing.droidsword.hooker

import android.app.AlertDialog
import android.app.AndroidAppHelper
import android.app.Dialog
import android.content.DialogInterface
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import net.androidwing.droidsword.utils.LogUtils

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

          ActivityHooker.setActionInfoToMenu("", "${view.javaClass.name} ${view.id} ")

          antiDisable(view)


        }
      }

    })


    XposedHelpers.findAndHookMethod(View::class.java,
        "onTouchEvent",
        MotionEvent::class.java, object : XC_MethodHook() {
      override fun afterHookedMethod(param: MethodHookParam?) {
        super.afterHookedMethod(param)
        val targetView = param?.thisObject as View

        //有响应事件的不可修改
        if(targetView.isClickable){
          LogUtils.e("this view is clickable, pass")
          return
        }

        val event = param.args!![0] as MotionEvent

        if (event.action == MotionEvent.ACTION_DOWN) {

          if (targetView is TextView) {
            LogUtils.e("find textView target")
            if(targetView is EditText){

              LogUtils.e("this view is editText ,pass")
              return
            }
            val textView = targetView as TextView

            val context = textView.context
            val editText = EditText(context)
            AlertDialog.Builder(context).setView(editText).setNegativeButton("取消"
            ) { p0, p1 -> }.setPositiveButton("确定") { p0, p1 ->
              textView.text = editText.text.toString()
            }.show()
          }

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