package net.androidwing.droidsword.hooker

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import net.androidwing.droidsword.Config
import net.androidwing.droidsword.utils.XSP

/**
 * Created  on 30/10/2017.
 */
class ActivityHooker : IHooker {
  override fun hook(lp: XC_LoadPackage.LoadPackageParam) {
    XposedHelpers.findAndHookMethod(Activity::class.java, "onResume", object : XC_MethodHook() {
      override fun afterHookedMethod(param: MethodHookParam?) {
        super.afterHookedMethod(param)
        val activity = param?.thisObject as Activity

        addTextView(activity)

        FragmentHooker().hookFragment(param)

        //TODO hook dialog 会和问文字修改冲突
//        DialogHooker().hookDialog()
      }
    })
  }


  private fun addTextView(activity: Activity) {
    val className = activity.javaClass.name.toString()

    if (sTextView == null) {
      genTextView(activity)
    }
    if (sTextView?.parent != null) {
      val parent = sTextView?.parent
      if (parent is ViewGroup) {
        parent.removeView(sTextView)
      }
    }
    (activity.window.decorView as FrameLayout).addView(sTextView)
    setActionInfoToMenu(className, "")
    sTextView?.bringToFront()
  }


  private fun genTextView(activity: Activity) {
    sTextView = TextView(activity)
    with(sTextView!!) {
      textSize = 8f
      y = 48 * 2f
      setBackgroundColor(Color.parseColor("#cc888888"))
      setTextColor(Color.WHITE)
      layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
          FrameLayout.LayoutParams.WRAP_CONTENT)
    }

  }

  companion object {
    var sTextView: TextView? = null
    private var sActivityName = ""
    private var sViewName = ""


    fun setActionInfoToMenu(activityName: String, viewName: String) {

      sTextView?.text = getActionInfo(activityName, viewName)
      updateState()
    }

    private fun updateState() {

      val enable = XSP.getBoolean(Config.KEY_ENABLE, true)
      val darkColor = XSP.getBoolean(Config.KEY_DARK_COLOR, false)
      sTextView?.visibility = if (enable) View.VISIBLE else View.INVISIBLE
      val backgroundColor = if (darkColor) "#000000" else "#cc888888"
      sTextView?.setBackgroundColor(Color.parseColor(backgroundColor))
    }

    public var sFragmentName = ""

    private fun getActionInfo(activityName: String, viewName: String): CharSequence? {
      if (activityName.isEmpty().not()) {
        sActivityName = activityName
      }

      if (viewName.isEmpty().not()) {
        sViewName = viewName
      }

      val pid = android.os.Process.myPid()

      return "Activity: $sActivityName \nPid: $pid \nClick: $sViewName \nFragment:$sFragmentName"

    }
  }
}