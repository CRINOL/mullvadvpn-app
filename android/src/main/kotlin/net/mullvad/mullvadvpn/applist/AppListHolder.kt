package net.mullvad.mullvadvpn.applist

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlin.properties.Delegates.observable
import net.mullvad.mullvadvpn.R
import net.mullvad.mullvadvpn.ui.CellSwitch
import net.mullvad.mullvadvpn.util.JobTracker

class AppListHolder(
    private val packageManager: PackageManager,
    private val jobTracker: JobTracker,
    view: View
) : ViewHolder(view) {
    private val loading: View = view.findViewById(R.id.loading)
    private val icon: ImageView = view.findViewById(R.id.icon)
    private val name: TextView = view.findViewById(R.id.name)
    private val excluded: CellSwitch = view.findViewById(R.id.excluded)

    var appInfo by observable<AppInfo?>(null) { _, _, info ->
        if (info != null) {
            val iconImage = info.icon

            name.text = info.label

            if (iconImage != null) {
                showIcon(iconImage)
            } else {
                hideIcon()
                loadIcon(info)
            }
        } else {
            name.text = ""
            hideIcon()
        }
    }

    private fun hideIcon() {
        icon.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    private fun showIcon(iconImage: Drawable) {
        loading.visibility = View.GONE
        icon.setImageDrawable(iconImage)
        icon.visibility = View.VISIBLE
    }

    private fun loadIcon(info: AppInfo) {
        jobTracker.newUiJob("load icon for ${info.info.packageName}") {
            val iconImage = jobTracker.runOnBackground {
                packageManager.getApplicationIcon(info.info)
            }

            info.icon = iconImage

            showIcon(iconImage)
        }
    }
}
