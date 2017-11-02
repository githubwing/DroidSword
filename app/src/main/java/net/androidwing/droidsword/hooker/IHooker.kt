package net.androidwing.droidsword.hooker

import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created  on 28/10/2017.
 */
interface IHooker {
  fun hook(lp : XC_LoadPackage.LoadPackageParam)
}