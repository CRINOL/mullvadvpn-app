package net.mullvad.mullvadvpn.service

import android.content.Context
import java.io.File
import kotlin.properties.Delegates.observable

// The spelling of the shared preferences location can't be changed to American English without
// either having users lose their preferences on update or implementing some migration code.
private const val SHARED_PREFERENCES = "split_tunnelling"
private const val KEY_ENABLED = "enabled"

class SplitTunneling(context: Context) {
    // The spelling of the app list file name can't be changed to American English without either
    // having users lose their preferences on update or implementing some migration code.
    private val appListFile = File(context.filesDir, "split-tunnelling.txt")
    private val excludedApps = HashSet<String>()
    private val preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

    val excludedAppList
        get() = if (enabled) {
            excludedApps.toList()
        } else {
            emptyList()
        }

    var enabled by observable(preferences.getBoolean(KEY_ENABLED, false)) { _, _, _ ->
        enabledChanged()
    }

    var onChange: ((List<String>) -> Unit)? = null

    init {
        if (appListFile.exists()) {
            excludedApps.addAll(appListFile.readLines())
        }
    }

    fun isAppExcluded(appPackageName: String) = excludedApps.contains(appPackageName)

    fun excludeApp(appPackageName: String) {
        excludedApps.add(appPackageName)
        update()
    }

    fun includeApp(appPackageName: String) {
        excludedApps.remove(appPackageName)
        update()
    }

    fun persist() {
        appListFile.writeText(excludedApps.joinToString(separator = "\n"))
    }

    private fun enabledChanged() {
        preferences.edit().apply {
            putBoolean(KEY_ENABLED, enabled)
            apply()
        }

        update()
    }

    private fun update() {
        onChange?.invoke(excludedAppList)
    }
}
