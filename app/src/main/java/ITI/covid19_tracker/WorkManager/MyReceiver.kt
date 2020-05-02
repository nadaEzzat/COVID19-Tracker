package ITI.covid19_tracker.WorkManager

import ITI.covid19_tracker.NotificationHelper
import ITI.covid19_tracker.SplashScreen
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.security.SecureRandom
import java.util.*

class MyReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        //Toast.makeText(MainActivity.context,"Hi From Another Class",Toast.LENGTH_SHORT).show()

        Notif()

    }

}

fun Notif() {

    val notificationHelper =
        NotificationHelper(

            SplashScreen.mContext,
            "Nada",
            "NADA EZZAT"
        )
    val nb: NotificationCompat.Builder? =
        notificationHelper.getChannelNotification()

    val notifyIntent: Intent = Intent()
    notifyIntent.setClassName(
        "ITI.covid19_tracker",
        "ITI.covid19_tracker.view.Tab.ActivityTab"
    )
    notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    val notifyPendingIntent = PendingIntent.getActivity(
        SplashScreen.mContext,
        notifyIntent.getIntExtra("request", 0),
        notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    if (nb != null) {
        nb.setContentIntent(notifyPendingIntent)
    }
    notificationHelper.getManager()
        .notify(Intent().getIntExtra("request", createRandomCode(7)), nb?.build())


}
fun createRandomCode(codeLength: Int): Int {
    val chars = "1234567890".toCharArray()
    val sb = StringBuilder()
    val random: Random = SecureRandom()
    for (i in 0 until codeLength) {
        val c = chars[random.nextInt(chars.size)]
        sb.append(c)
    }
    return sb.toString().toInt()
}
