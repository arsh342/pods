package com.arsh.pods.data

import androidx.core.content.edit
import com.arsh.pods.utils.XposedServiceHolder

class XposedRemotePrefImpl: XposedRemotePref {
    override fun isAvailable(): Boolean {
        return XposedServiceHolder.service != null
    }

    override fun getBoolean(key: String, def: Boolean): Boolean {
        val s = XposedServiceHolder.service ?: return def
        return s.getRemotePreferences("com.arsh.pods").getBoolean(key, def)
    }

    override fun putBoolean(key: String, value: Boolean) {
        val s = XposedServiceHolder.service ?: return
        s.getRemotePreferences("com.arsh.pods")
            .edit { putBoolean(key, value) }
    }
}
