package net.androidwing.droidsword

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Switch
import android.widget.Toast

class MainActivity : AppCompatActivity() {

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
        Toast.makeText(MainActivity@this, getString(R.string.no_xposed_installer), Toast.LENGTH_SHORT).show()
      }
    }

    val sp = getSharedPreferences(Config.SP_NAME, Context.MODE_WORLD_READABLE)
    if (sp.getBoolean(Config.KEY_INIT, true)) {
      sp.edit()
          .putBoolean(Config.KEY_INIT, false)
          .putBoolean(Config.KEY_ENABLE, true)
          .putBoolean(Config.KEY_DARK_COLOR, false)
          .putBoolean(Config.KEY_TEXT_CHANGER, false)
          .putBoolean(Config.KEY_VIEW_ENABLE, false)
          .putBoolean(Config.KEY_APP_DEBUG,false)
          .apply()
    }

    val darkColorSwitch = findViewById(R.id.sw_dark_color) as Switch
    darkColorSwitch.isChecked = sp.getBoolean(Config.KEY_DARK_COLOR, false)
    darkColorSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      sp.edit().putBoolean(Config.KEY_DARK_COLOR, isChecked).apply()

      //call for refresh state
      onResume()
    }


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
