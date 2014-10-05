package fr.jacquemet.fanimally;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jacquemet.fanimally.log.ConfigureLogging;
import fr.jacquemet.fanimally.widget.WidgetWiew;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;


/**
 * ECOUTE le d�marrage de l'Android
 * et d�marre le Gestionnaire de service si besoin
 * @author JulienJ
 *
 */

public class OnBoot extends BroadcastReceiver {
 
	private static SharedPreferences prefs;
	private static boolean isBoot=false;
	private static long time;
	public static boolean demarrageActivite=false;
 
    @Override
    public void onReceive(final Context context, Intent intent) 
    { 
    	//Ecoute du d�marrage de l'OS
    	if(intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED"))
    	{
    		isBoot=true;
    		time=System.currentTimeMillis();
    	}
    	
    	//Ecoute le mount de la sdcard
    	if(intent.getAction().equalsIgnoreCase("android.intent.action.MEDIA_MOUNTED"))
    	{
    		//pour ecrire sur la SD
    		Logger log = LoggerFactory.getLogger(OnBoot.class);
    		
    		ConfigureLogging.getInstance();
    		
    		//log.info("Montage de la carte, etat ANDREAS "+Widget.enFonctionnement);     
    		
    		//Ne passe ici que si on a boot� il y a moins de 5 minutes
    		//il ne faut pas relancer l'application � chaque montage/demontage de carte SD
    		if(isBoot && ((System.currentTimeMillis()-time)<(1000*60*5)))
    		{
    			time=System.currentTimeMillis();
    		    			
    			//Cr�e un Alarm Manager
    			/*AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); 
    			Intent i = new Intent(context, GestionnaireService.class); 
    			PendingIntent pi = PendingIntent.getService(context, 0, i, 0); 
    			am.cancel(pi); */    			    			
    			
    			//recup�re les prefs
    			prefs = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
    			
    			//recup�re l'�tat de la veille
    			if(prefs.getBoolean("EtatWidgetAndreas", false))
    			{
    				//relance le widget
    				WidgetWiew.redemarrageParBroadcastReceiver=true; 
    				context.startService(new Intent(context, WidgetWiew.UpdateService.class));    				
    				//am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5*60*1000, 5*60*1000, pi); 
				}
    			
    			isBoot=false; 
    			
    			log.info("Etat sauvegard�e au red�marrage du tel "+prefs.getBoolean("EtatWidgetAndreas", false));     			
    			
    			
    		}
    	}

    }
}