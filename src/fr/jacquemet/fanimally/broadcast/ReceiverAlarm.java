package fr.jacquemet.fanimally.broadcast;

import fr.jacquemet.fanimally.service.AlarmService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
public class ReceiverAlarm extends BroadcastReceiver
{
      
    @Override
    public void onReceive(Context context, Intent intent)
    {
       Intent service1 = new Intent(context, AlarmService.class);
       context.startService(service1);
        
    }   
}


/*
 * private AlarmManager alarmMgr;
private PendingIntent alarmIntent;
...
alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
Intent intent = new Intent(context, AlarmReceiver.class);
alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

// Set the alarm to start at 8:30 a.m.
Calendar calendar = Calendar.getInstance();
calendar.setTimeInMillis(System.currentTimeMillis());
calendar.set(Calendar.HOUR_OF_DAY, 8);
calendar.set(Calendar.MINUTE, 30);

// setRepeating() lets you specify a precise custom interval--in this case,
// 20 minutes.
alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        1000 * 60 * 20, alarmIntent);
        
        ou 
        
        http://stackoverflow.com/questions/13909040/set-android-alarm-clock-programmatically?lq=1
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */



