package net.androidwing.droidsword.hooker

import android.app.Dialog
import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import net.androidwing.droidsword.utils.LogUtils

/**
 * Created  on 2018/12/11.
 */
class DialogHooker {
  fun hookDialog() {
    XposedHelpers.findAndHookMethod(Dialog::class.java, "show", object : XC_MethodHook() {
      override fun afterHookedMethod(param: MethodHookParam?) {
        val dialog = param?.thisObject as Dialog
        val decor = XposedHelpers.getObjectField(dialog, "mDecor")
        LogUtils.e("in the show" + decor?.javaClass?.name)
        if (decor is FrameLayout) {
          if (sTextView == null) {
            sTextView = TextView(dialog.context)
            with(ActivityHooker.sTextView!!) {
              textSize = 8f
              y = 48 * 2f
              setTextColor(Color.BLACK)
              layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                  FrameLayout.LayoutParams.WRAP_CONTENT)
            }
          }

            if (sTextView?.parent != null) {
              val parent = sTextView?.parent
              if (parent is ViewGroup) {
                parent.removeView(sTextView)
              }
            }
            decor.addView(sTextView)
            sTextView?.text = dialog.javaClass.name
            sTextView?.bringToFront()
            LogUtils.e("${dialog.javaClass.name}")
          }
        }
    })
  }

  companion object {
    var sTextView: TextView? = null
  }
}