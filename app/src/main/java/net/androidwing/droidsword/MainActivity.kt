package net.androidwing.droidsword

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import net.androidwing.droidsword.utils.PackageUtil
import android.R.attr.key
import android.net.Uri


class MainActivity : AppCompatActivity() {
  val sp: SharedPreferences by lazy {
    getSharedPreferences(Config.SP_NAME, Context.MODE_WORLD_READABLE)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (isHook()) {
      findViewById(R.id.ll_not_active)?.visibility = View.GONE
    }

    findViewById(R.id.tv_start_xi)?.setOnClickListener {
      val intent = Intent()
      val componentName = ComponentName("de.robv.android.xposed.installer",
          "de.robv.android.xposed.installer.WelcomeActivity")
      intent.component = componentName
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

      try {
        startActivity(intent)
      } catch (e: Exception) {
        Toast.makeText(MainActivity@ this, getString(R.string.no_xposed_installer),
            Toast.LENGTH_SHORT).show()
      }
    }

    if (sp.getBoolean(Config.KEY_INIT, true)) {
      sp.edit()
          .putBoolean(Config.KEY_INIT, false)
          .putBoolean(Config.KEY_ENABLE, true)
          .putBoolean(Config.KEY_DARK_COLOR, false)
          .putBoolean(Config.KEY_TEXT_CHANGER, false)
          .putBoolean(Config.KEY_VIEW_ENABLE, false)
          .putBoolean(Config.KEY_APP_DEBUG, false)
          .putString(Config.ENABLE_PACKAGE_NAME, "")
          .apply()
    }


    val darkColorSwitch = findViewById(R.id.sw_dark_color) as Switch
    darkColorSwitch.isChecked = sp.getBoolean(Config.KEY_DARK_COLOR, false)
    darkColorSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      sp.edit().putBoolean(Config.KEY_DARK_COLOR, isChecked).apply()

      //call for refresh state
      onResume()
    }


    initCheckBox(sp)


    val changeTxt = findViewById(R.id.tv_change) as TextView
    changeTxt.setOnClickListener {
      initAppList()
    }

    findViewById(R.id.tv_qq_group)!!.setOnClickListener {
      val key = "rFx1zaa0qRXHR-WY_MGlO1bkNUF-VXKg"
      val intent = Intent()
      intent.data = Uri.parse(
          "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
      try {
        startActivity(intent)
      } catch (e: Exception) {
        // 未安装手Q或安装的版本不支持
        Toast.makeText(MainActivity@this,getString(R.string.no_qq),Toast.LENGTH_SHORT).show()
      }

    }

    findViewById(R.id.ll_target_help)?.setOnClickListener {
      AlertDialog.Builder(this)
          .setMessage(getString(R.string.target_help))
          .setNegativeButton("ok",null).create().show()
    }

  }

  private fun initAppList() {
    val dialog = AlertDialog.Builder(this).create()
    val scrollView = ScrollView(this)
    val container = LinearLayout(this)
    scrollView.addView(container)
    container.orientation = LinearLayout.VERTICAL

    val appList = PackageUtil.getAppList(packageManager)
    val emptyView = LayoutInflater.from(this).inflate(R.layout.item_app, null)
    (emptyView.findViewById(R.id.tv_app_name) as TextView).text = "无"
    emptyView.setOnClickListener {
      sp.edit().putString(Config.ENABLE_PACKAGE_NAME,"").apply()
      dialog.dismiss()
      updateTargetAppUI()
      Toast.makeText(MainActivity@this,getString(R.string.restart_target),Toast.LENGTH_LONG).show()
    }
    container.addView(emptyView)

    appList.forEach {
      val appInfo = it
      val view = LayoutInflater.from(this).inflate(R.layout.item_app, null)
      (view.findViewById(R.id.iv_app) as ImageView).setImageDrawable(it.appIcon)
      (view.findViewById(R.id.tv_app_name) as TextView).text = it.appName
      view.setOnClickListener {
        sp.edit().putString(Config.ENABLE_PACKAGE_NAME,appInfo.packageName).apply()
        dialog.dismiss()
        updateTargetAppUI()
        Toast.makeText(MainActivity@this,getString(R.string.restart_target),Toast.LENGTH_LONG).show()
      }
      container.addView(view)
    }

    dialog.setView(scrollView)
    dialog.show()

  }

  override fun onResume() {
    super.onResume()
    updateTargetAppUI()
  }

  private fun updateTargetAppUI() {
    val enablePackageName = sp.getString(Config.ENABLE_PACKAGE_NAME, "")
    val packageInfo = PackageUtil.getAppInfo(enablePackageName, packageManager)
    val iconImg = findViewById(R.id.iv_module) as ImageView
    val appNameTxt = findViewById(R.id.tv_module_name) as TextView
    val changeTxt = findViewById(R.id.tv_change) as TextView
    if (packageInfo != null) {
      iconImg.setImageDrawable(packageInfo.appIcon)
      appNameTxt.text = packageInfo.appName
      iconImg.visibility = View.VISIBLE
      changeTxt.text = getString(R.string.change)
    } else {
      iconImg.visibility = View.GONE
      appNameTxt.text = "无"
      changeTxt.text = getString(R.string.select)
    }
  }

  private fun initCheckBox(sp: SharedPreferences) {
    val enableSwitch = findViewById(R.id.sw_enable) as Switch
    enableSwitch.isChecked = !sp.getBoolean(Config.KEY_ENABLE, false)
    enableSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      sp.edit().putBoolean(Config.KEY_ENABLE, !isChecked).apply()

      //call for refresh state
      onResume()
    }

    val textChangerSwitch = findViewById(R.id.sw_text_changer) as Switch
    textChangerSwitch.isChecked = sp.getBoolean(Config.KEY_TEXT_CHANGER, false)
    textChangerSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      if(isChecked){
        Toast.makeText(MainActivity@this,"开启文字修改后，会关闭dialog显示,并且可能影响交互，请谨慎",Toast.LENGTH_LONG).show()
      }
      sp.edit().putBoolean(Config.KEY_TEXT_CHANGER, isChecked).apply()
    }

    val viewEnableSwitch = findViewById(R.id.sw_view_enabler) as Switch
    viewEnableSwitch.isChecked = sp.getBoolean(Config.KEY_VIEW_ENABLE, false)
    viewEnableSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      sp.edit().putBoolean(Config.KEY_VIEW_ENABLE, isChecked).apply()
    }


    val appDebugSwitch = findViewById(R.id.sw_app_debug) as Switch
    appDebugSwitch.isChecked = sp.getBoolean(Config.KEY_APP_DEBUG, false)
    appDebugSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      sp.edit().putBoolean(Config.KEY_APP_DEBUG, isChecked).apply()
    }
  }

  //this method is for hook
  fun isHook(): Boolean {
    return false
  }


}
