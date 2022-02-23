package com.example.appgallery.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.appgallery.MainActivity
import com.example.appgallery.R

class Notifications(val body : String) {
    fun runNotification(context: Context) {
        var intent = Intent(context, MainActivity::class.java)

        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(),
            intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        // if Android Version above 8 then we need to create a chanel in the system.
        makeChanel(context)
        val builder = NotificationCompat.Builder(context, default_notification_channel_id)
        // int badgeCount = 1;
      //  ShortcutBadger.applyCount(this, currentBadgeNumber + 1) //for 1.1.4+
       // setCurrentBadge(applicationContext) // apply
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //    builder.setSmallIcon(R.drawable.test_nn);
            //   builder.setColor(getResources().getColor(R.color.app_color));
            builder.setSmallIcon(R.drawable.ic_notification)
            builder.setContentTitle("title notification")
            builder.setContentText(body)
            builder.setSound(soundUri)
            builder.setAutoCancel(true)
            builder.setContentIntent(pendingIntent).priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) NotificationManager.IMPORTANCE_HIGH else Notification.PRIORITY_MAX
            builder.setStyle(NotificationCompat.BigTextStyle().bigText("body"))
            builder.priority = NotificationCompat.PRIORITY_HIGH
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            //            notificationManagerCompat.notify(/*Notification_base_id + 1*/(int) System.currentTimeMillis(), builder.build());
            notificationManagerCompat.notify( /*Notification_base_id + 1*/System.currentTimeMillis().toInt(), builder.build())
        } else { // notification.setSmallIcon(R.drawable.icon);
            builder.setSmallIcon(R.drawable.ic_notification)
            builder.setContentTitle("new title")
            builder.setContentText(body)
            builder.setSound(soundUri)
            builder.setAutoCancel(true)
            builder.setContentIntent(pendingIntent).priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                NotificationManager.IMPORTANCE_HIGH else Notification.PRIORITY_MAX
            builder.setStyle(NotificationCompat.BigTextStyle().bigText("new body"))
            builder.priority = NotificationCompat.PRIORITY_HIGH
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
    fun makeChanel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = channel_name
            val description = channel_description
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(default_notification_channel_id, name, importance)
            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.setShowBadge(true)
            channel.vibrationPattern = null
            channel.enableVibration(false)
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    companion object {
        val channel_name = "channel"
        val channel_description = "channel_description"
        val notificationType = "TYPE"
        val default_notification_channel_id = "300"
        val MY_TRIGGER = "trigger"

    }
}
