package ITI.covid19_tracker.Network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class newtwork() {
    fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .state == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .state == NetworkInfo.State.CONNECTED
        ) {
            true
        } else {
            false
        }
    }
}