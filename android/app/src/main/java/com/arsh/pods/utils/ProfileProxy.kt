package com.arsh.pods.utils

/**
 * Runs [use] on a Bluetooth profile proxy and always releases it afterwards.
 * Hidden profile methods can throw on unprivileged installs, so errors go to [onError].
 */
fun useProfileProxy(close: () -> Unit, onError: (Exception) -> Unit = {}, use: () -> Unit) {
    try {
        use()
    } catch (e: Exception) {
        onError(e)
    } finally {
        close()
    }
}
