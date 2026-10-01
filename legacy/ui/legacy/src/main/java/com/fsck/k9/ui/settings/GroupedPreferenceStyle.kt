package com.fsck.k9.ui.settings

import android.annotation.SuppressLint
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroupAdapter
import app.k9mail.core.ui.legacy.designsystem.atom.container.GroupedListDecoration

/**
 * Displays the preferences of each category as a group of rounded containers, like the Android system settings.
 */
@SuppressLint("RestrictedApi")
fun PreferenceFragmentCompat.applyGroupedPreferenceStyle() {
    setDivider(null)

    val listView = listView
    listView.addItemDecoration(
        GroupedListDecoration(requireContext()) { position ->
            // PreferenceGroupAdapter.getItem() is the only way to map an adapter position to its preference
            (listView.adapter as? PreferenceGroupAdapter)?.getItem(position) is PreferenceCategory
        },
    )
}
