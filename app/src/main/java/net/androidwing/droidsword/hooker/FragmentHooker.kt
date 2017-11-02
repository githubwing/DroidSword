package net.androidwing.droidsword.hooker

import android.app.Activity
import android.app.AndroidAppHelper
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import net.androidwing.droidsword.utils.LogUtils
import android.content.IntentFilter
import de.robv.android.xposed.XposedHelpers.findAndHookMethod


/**
 * Created  on 30/10/2017.
 */
class FragmentHooker : IHooker {
  override fun hook(lp: XC_LoadPackage.LoadPackageParam) {
    findAndHookMethod(Fragment::class.java, "onCreate", Bundle::class.java,
        object : XC_MethodHook() {

          override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {

            val fragment = param!!.thisObject as Fragment
            LogUtils.e("hook the fragment ${fragment.javaClass.name}")
          }
        })

  }

}