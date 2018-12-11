package net.androidwing.droidsword

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val button = findViewById(R.id.btn_startInstaller)
    if (isHook()) {
      (findViewById(R.id.tv_is_hook) as TextView).text = "插件已激活,请放心使用"
      button.visibility = View.GONE
    }

    button.setOnClickListener {
      val intent = Intent()
      val componentName = ComponentName("de.robv.android.xposed.installer",
          "de.robv.android.xposed.installer.WelcomeActivity")
      intent.component = componentName
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

      try {
        startActivity(intent)
      } catch (e: Exception) {
        Toast.makeText(MainActivity@ this, "啊哦，你好像没有安装XposedInstaller", Toast.LENGTH_SHORT).show()
      }
    }

    val sp = getSharedPreferences(Config.SP_NAME, Context.MODE_WORLD_READABLE)
    if (sp.getBoolean(Config.KEY_INIT, true)) {
      sp.edit().putBoolean(Config.KEY_INIT, false).putBoolean(Config.KEY_ENABLE, true).putBoolean(
          Config.KEY_DARK_COLOR, false).apply()
    }

    val darkColorSwitch = findViewById(R.id.sw_dark_color) as Switch
    darkColorSwitch.isChecked = sp.getBoolean(Config.KEY_DARK_COLOR, false)
    darkColorSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      sp.edit().putBoolean(Config.KEY_DARK_COLOR, isChecked).apply()

      //call for refresh state
      onResume()
    }


    val enableSwitch = findViewById(R.id.sw_enable) as Switch
    enableSwitch.isChecked = sp.getBoolean(Config.KEY_ENABLE, false)
    enableSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      sp.edit().putBoolean(Config.KEY_ENABLE, isChecked).apply()

      //call for refresh state
      onResume()
    }
  }

  //this method is for hook
  fun isHook(): Boolean {
    return false
  }


}
