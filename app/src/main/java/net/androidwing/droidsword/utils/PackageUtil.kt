package net.androidwing.droidsword.utils

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

/**
 * Created  on 2018/12/26.
 */
object PackageUtil {
  fun getAppList(pm: PackageManager): List<AppInfo> {
    //已安装程序列表

    val appList = ArrayList<AppInfo>()
    val mainIntent = Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

    val packageList = pm.queryIntentActivities(mainIntent,PackageManager.MATCH_ALL)

    //用户安装应用
    packageList
        .mapTo(appList) {

          AppInfo(it.activityInfo.loadLabel(pm) as String,
              it.activityInfo.packageName, it.activityInfo.name, it.activityInfo.loadIcon(pm),
              it.activityInfo.enabled)
        }

    return appList
  }

  fun getAppInfo(packageName: String, pm: PackageManager): AppInfo? {

    val appList = ArrayList<AppInfo>()
    val mainIntent = Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

    val packageList = pm.queryIntentActivities(mainIntent,PackageManager.MATCH_ALL)


    packageList.filter {
      it.activityInfo.packageName == packageName
    }.map {

      return AppInfo(it.activityInfo.loadLabel(pm) as String,
          it.activityInfo.packageName, it.activityInfo.name, it.activityInfo.loadIcon(pm),
          it.activityInfo.enabled)
    }
    return null
  }


  data class AppInfo(
      val appName: String,
      val packageName: String,
      val mainClass: String,
      val appIcon: Drawable,
      var checked: Boolean
  )
}


