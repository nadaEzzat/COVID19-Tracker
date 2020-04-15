
package ITI.covid19_tracker

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat


class NotificationHelper(base: Context?, title: String, body: String) : ContextWrapper(base) {
    val channelID = "channelID"
    val channelName = "Channel Name"
    private var mManager: NotificationManager? = null

    var title: String? = null
    var body: String? = null

    init {
        this.title = title
        this.body = body

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            channelID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        getManager().createNotificationChannel(channel)
    }

    fun getManager(): NotificationManager {
        if (mManager == null) {
            mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return mManager as NotificationManager
    }

    fun getChannelNotification(): NotificationCompat.Builder? {
        return NotificationCompat.Builder(
            applicationContext,
            channelID
        )
            .setContentTitle(title)
            .setContentText(body)
            .setColor(resources.getColor(R.color.Red))
            .setOngoing(false)
            .setAutoCancel(false)
            .setSmallIcon(R.drawable.ic_patient)
    }
}