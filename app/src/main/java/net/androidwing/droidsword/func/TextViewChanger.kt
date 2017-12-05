package net.androidwing.droidsword.func

import android.app.AlertDialog
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import net.androidwing.droidsword.utils.LogUtils

/**
 * Created  on 03/11/2017.
 */
object TextViewChanger{

  fun showChangeDialog(targetView: View,event: MotionEvent){
    //有响应事件的不可修改
    if (targetView.isClickable) {
      LogUtils.e("this view is clickable, pass")
      return
    }


    if (event.action == MotionEvent.ACTION_DOWN) {

      if (targetView is TextView) {
        LogUtils.e("find textView target")


        if (targetView is EditText) {

          LogUtils.e("this view is editText ,pass")
          return
        }
        val textView = targetView as TextView

        val context = textView.context
        val editText = EditText(context)
        editText.setText(textView.text)
        AlertDialog.Builder(context).setView(editText).setNegativeButton("取消"
        ) { p0, p1 -> }.setPositiveButton("确定") { p0, p1 ->
          textView.text = editText.text.toString()
        }.show()
      }

    }
  }
}
