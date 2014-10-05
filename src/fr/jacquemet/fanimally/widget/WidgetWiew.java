package fr.jacquemet.fanimally.widget;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.widget.RemoteViews;
import fr.jacquemet.fanimally.FanimallyActivity;
import fr.jacquemet.fanimally.R;
import fr.jacquemet.fanimally.TakePhotoActivity;
import fr.jacquemet.fanimally.model.ModeleDonnees;

 


/**
 *Classe implémentant la fenêtre principale avec les différentes actions
 */
public class WidgetWiew extends AppWidgetProvider {
   
	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
	public AppWidgetManager appWidgetManager;
	public int[] appWidgetIds;
	public static RemoteViews updateViews;
	public static LocationManager mLocation;
	public static boolean enFonctionnement=false;
	public static BroadcastReceiver broadcastReceiver;
	public static String sens="";
	public static String pr=""; 
	public static String Auto="";
	public static IntentFilter intention;
	private static Timer timer;
	private static boolean etatBT=false;
	private static boolean aEnregistrerBroadcast=false;
	private static boolean peutCliquerSurBoutonArretMarche=true;
	private static boolean modePersistanceApplication=false;
	public static PowerManager.WakeLock wl;
	public static PowerManager pm;
	public static boolean etatReseauGPRS=false;
	public static DecimalFormat df = new DecimalFormat("0.0");
	public static SharedPreferences.Editor editor;
	public static SharedPreferences prefs; 
	public static boolean estEnCoursDeTraitement=false;
	public static boolean redemarrageParBroadcastReceiver=false;
	public static Timer timerOnUpdate;
	public static boolean estPasserEnEcranInActif=false;
	public static boolean demarreTimer=false;
	public static long tempsDernierEtatGPS=0;  
	public static boolean faitDisparaitreLogo=false;       
	 
	//Pour émettre une tonalité 
	//public final static ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
	public static Logger log = LoggerFactory.getLogger(WidgetWiew.class);	
	 
				  
	 @Override  
	    public void onUpdate(final Context context, AppWidgetManager appWidgetManager,
	            int[] appWidgetIds) {
		  
		 	// To prevent any ANR timeouts, we perform the update in a service
	        context.startService(new Intent(context, UpdateService.class));	
	        
	 } 
	 
	 @Override
	 public void onDeleted(Context context, int[] appWidgetIds)
	 {
		 Intent serviceIntent = new Intent(context, UpdateService.class);
	 	 context.stopService(serviceIntent);
	 	 super.onDeleted(context, appWidgetIds);
	 }
	 
	  
	    public static class UpdateService extends Service { 
	    	
	    	@Override
	    	public void onConfigurationChanged(Configuration newConfig)
	        {
	            int oldOrientation = this.getResources().getConfiguration().orientation;

	            if(newConfig.orientation != oldOrientation)
	            {
	           
	            }
	        } 
	        
	    
			/**
	    	 * Methode appelant lors de la dépose du widget sur le bureau    		    	
	    	 */ 
	    	public int onStartCommand(Intent intent, int flags, int startId) 
	    	{  
	    		
	    		/*ConfigureLogging.getInstance();
				
				log.info("Demarrage Timer Widget");
			
				//Permet de mettre des alarmes pour la réactivation du widget si possible
		        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE); 
				Intent i = new Intent(getApplicationContext(), GestionnaireService.class); 
				PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0, i, 0); 
				am.cancel(pi); 
				am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5*60*1000, 5*60*1000, pi); 
									
				
				setForeground(true);*/
	    		
				//Supprime toutes les données passées en paramètres lors du rafraichissement du widget
				if(intent!=null)
					intent.replaceExtras((Bundle) null);
				
				// Build the widget update for today
				RemoteViews updateViews = buildUpdate(this);
            
	            // Push update for this widget to the home screen
	            ComponentName thisWidget = new ComponentName(this, WidgetWiew.class);
	            AppWidgetManager manager = AppWidgetManager.getInstance(this);
	            manager.updateAppWidget(thisWidget, updateViews);
	              
	            
	            //indique de redémarrer le widget en cas de plantage ou d'arret du système
	    		return START_NOT_STICKY; 
	    	}
	    	 
			@Override
	        public void onStart(Intent intent, int startId) 
			{
				
	           
	        }
			
			/**
			 * Démarre l'ensemble des services
			 */
			private void demarrageWidget()
			{
				RemoteViews updateViews = buildUpdate(getApplicationContext());
	            
	            // Push update for this widget to the home screen
	            ComponentName thisWidget = new ComponentName(getApplicationContext(), WidgetWiew.class);
	            AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
	            manager.updateAppWidget(thisWidget, updateViews); 
				
				//pour faire une vibration
			     ((Vibrator) (getSystemService(Context.VIBRATOR_SERVICE))).vibrate(2000);
				
				int tid=android.os.Process.myTid(); 
				
		    	android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
	            Thread.currentThread().setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);	
			}  			
			
			
			
			private void arretWidget()
			{
				
						peutCliquerSurBoutonArretMarche=true;
						RemoteViews updateViews = buildUpdate(getApplicationContext());
			            
			            // Push update for this widget to the home screen
			            ComponentName thisWidget = new ComponentName(getApplicationContext(), WidgetWiew.class);
			            AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
			            manager.updateAppWidget(thisWidget, updateViews);	
			            
			            //A enlever
			           // timer.cancel();
			         			
				
			}
			
			
			
			/**
			 * Permet de mettre à jour l'affichage
			 */
			private void updateScreen()
			{
				PowerManager pm = (PowerManager)
				getSystemService(Context.POWER_SERVICE);
				boolean isScreenOn = pm.isScreenOn();
				
				//seulement si l'ecran n'est pas en veille
				if(prefs.getBoolean("EtatWidgetAndreas", false) && enFonctionnement && isScreenOn)
    			{
    				RemoteViews updateViews = buildUpdate(getApplicationContext());
		            
		            // Push update for this widget to the home screen
		            ComponentName thisWidget = new ComponentName(getApplicationContext(), WidgetWiew.class);
		            AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
		            manager.updateAppWidget(thisWidget, updateViews);											 
				}
			}
			
			
	        /**
	         * Build a widget update to show the current Wiktionary
	         * "Word of the day." Will block until the online API returns.
	         */
	        public RemoteViews buildUpdate(Context context)
	        {
	        	   
	            //Chargement de la vue
	        	RemoteViews updateViews = null;
	            updateViews = new RemoteViews(context.getPackageName(), R.layout.homescreen);
	            
	            Intent fanimallyAct = new Intent(getApplicationContext(), FanimallyActivity.class);
            	PendingIntent actionPendingIntentfanimallyAct = PendingIntent.getActivity(getApplicationContext(), 1, fanimallyAct,  Intent.FLAG_ACTIVITY_NEW_TASK);
            	updateViews.setOnClickPendingIntent(R.id.btn_mafanimally_widg, actionPendingIntentfanimallyAct);
            	
            	Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+ModeleDonnees.getInstance().numUrgence));
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, callIntent, 0);
                updateViews.setOnClickPendingIntent(R.id.btn_urgence_widg, pendingIntent);
                
                Intent fanimallyPhoto = new Intent(getApplicationContext(), TakePhotoActivity.class);
            	PendingIntent actionPendingIntentfanimallyPhoto = PendingIntent.getActivity(getApplicationContext(), 2, fanimallyPhoto,  Intent.FLAG_ACTIVITY_NEW_TASK);
            	updateViews.setOnClickPendingIntent(R.id.btn_patte_moi, actionPendingIntentfanimallyPhoto);
	
	              
	            //chargement des edit text 
	            //updateViews.setTextViewText(R.id.nomAuto, Auto);
	            //updateViews.setTextViewText(R.id.pr, pr);
	            //updateViews.setTextViewText(R.id.sens, sens);
	            
	              
	            //chargement des boutons, association des actions et
	            if(enFonctionnement==false)
	            {
	            	
	            	/**
	            	 * Pour configurer le widget
	            	 */
	            	//updateViews.setViewVisibility(R.id.configure, View.VISIBLE);
	            	
	            	//Intent configure = new Intent(getApplicationContext(), WidgetCodeActivite.class);
	            	/*Intent configure = new Intent(getApplicationContext(), ConfigureWidget.class);
	            	PendingIntent actionPendingIntentConfigure = PendingIntent.getActivity(getApplicationContext(), 4, configure,  Intent.FLAG_ACTIVITY_NEW_TASK);
	            	updateViews.setOnClickPendingIntent(R.id.configure, actionPendingIntentConfigure);*/
	             
	            	/**
	            	 * Fin
	            	 */
	            	
	            	//Lance un refresh de l'application pour débuter le traitement
	            	Intent demarrer = new Intent(getApplicationContext(), WidgetWiew.UpdateService.class);
	            	demarrer.putExtra("demarre", true);
	               	PendingIntent actionPendingIntentArreter = PendingIntent.getService(getApplicationContext(), 1, demarrer,  Intent.FLAG_ACTIVITY_NEW_TASK);
	            	

	            	//updateViews.setOnClickPendingIntent(R.id.demarrer, actionPendingIntentArreter);
	            	
	            	//updateViews.setViewVisibility(R.id.codeactivitewidget, View.GONE);
	            	
	            	
	              
	            	//Pour rendre visible les boutons ou non
	            	if(peutCliquerSurBoutonArretMarche)
	            	{
	            		//updateViews.setViewVisibility(R.id.demarrer, View.VISIBLE);
	            		//updateViews.setViewVisibility(R.id.arreter, View.GONE);
	            	} 
	            }
	            
	             
	             		     	
		        return updateViews;
	        }
	        
			@Override
			public IBinder onBind(Intent intent) {
				// TODO Auto-generated method stub
				return null;
			}
			
			/**
			 * Permet d'envoyer le dernier fichier de log
			 */
			private void sendMail()
			{
				Scanner scanner; 
				try 
				{
					if(new File(Environment.getExternalStorageDirectory() + "/ANDREAS/log/ANDREAS.log").exists())
					{
						scanner = new Scanner(new File(Environment.getExternalStorageDirectory() + "/ANDREAS/log/ANDREAS.log"));
						
						String line="";
	
						// On boucle sur chaque champ detecté
						while (scanner.hasNextLine()) 
						{
						    line += scanner.nextLine();
						    line+="\n";
						   
						}
	
						scanner.close();
						
						fr.jacquemet.fanimally.utils.GMailSender m=new fr.jacquemet.fanimally.utils.GMailSender("seerp26@gmail.com","seerp26210");
						String[] toArr = {"seerp26@gmail.com"}; 
						m.setTo(toArr);
						m.setFrom("ANDREAS");   
						m.setSubject("Log ANDREAS " ); 
						m.setBody(line); 
						m.send();
					}
				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
				
			}
			
			/**
			   * Indique si le service est en cours d'exécution
			   * @param nameOfService le nom du service
			   * @return 
			   */
			  private boolean isMyServiceRunning(String nameOfService) 
			  {
			    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			        if (nameOfService.equals(service.service.getClassName())) {
			            return true;
			        }
			    }  
			    return false;
			  }
    
	    }
	
}