package net.androidwing.droidsword

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import net.androidwing.hotxposed.HotXposed

/**
 * Created  on 28/10/2017.
 */
class Init : IXposedHookLoadPackage {
  override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
    HotXposed.hook(HookerDispatcher::class.java, lpparam)
  }

}


