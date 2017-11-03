package net.androidwing.droidsword.func

import android.view.View

/**
 * Created  on 03/11/2017.
 */
object ViewEnabler{
  /**
   * 灰色按钮克星，
   * TODO moren 默认开启待添加配置文件
   */
   fun antiDisable(view: View) {
      if (view.isEnabled.not()) {
        view.isEnabled = true
      }
  }
}