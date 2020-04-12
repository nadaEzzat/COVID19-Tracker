package ITI.covid19_tracker.Network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import java.io.IOException


class checkconniction (private val _context: Context) {
    private var network: Network? = null
    fun hasInternetConnection(): Boolean {
        val connectivity =
            _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var capabilities: NetworkCapabilities? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            network = connectivity.activeNetwork
            capabilities = connectivity.getNetworkCapabilities(network)
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (connectivity != null) {
                val command = "ping -c 1 google.com"
                try {
                    return Runtime.getRuntime().exec(command).waitFor() == 0
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return false
    }

}
