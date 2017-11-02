package net.androidwing.droidsword

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import net.androidwing.droidsword.hooker.ActivityHooker
import net.androidwing.droidsword.hooker.FragmentHooker
import net.androidwing.droidsword.hooker.ViewClickedHooker

/**
 * Created  on 28/10/2017.
 */
class Init : IXposedHookLoadPackage{
  override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
    ViewClickedHooker().hook(lpparam!!)
    ActivityHooker().hook(lpparam)
    FragmentHooker().hook(lpparam)
  }

}