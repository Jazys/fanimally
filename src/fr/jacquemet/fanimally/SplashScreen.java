package fr.jacquemet.fanimally;


import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import fr.jacquemet.fanimally.R;
import fr.jacquemet.fanimally.log.SRSDexception;
import fr.jacquemet.fanimally.model.ModeleDonnees;
import fr.jacquemet.fanimally.view.ViewAnimalList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Permet de créer une boite de dialogue temporaire
 * AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	                builder.setTitle("Auto-closing Dialog");
	                builder.setMessage("After 2 second, this dialog will be closed automatically!");
	                builder.setCancelable(true);

	                final AlertDialog dlg = builder.create();

	                dlg.show();

	                final Timer t = new Timer();
	                t.schedule(new TimerTask() {
	                    public void run() {
	                        dlg.dismiss(); // when the task active then close the dialog
	                        t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
	                    }
	                }, 2000); 
 * @author JulienJ
 *
 */ 


/**
 * Passage des layout de la S2 à la S1, il faut changer au moins :
 * -header.xml
 * -ecran_principal.xml
 * -code_activite.xml
 * -texte_code_activite.xml
 * -bandeau_volume.xml
 * -liste_menu_appel.xml
 * -viewscreenpk.xml
 * 
 * 
 * @author JulienJ
 *
 */
public class SplashScreen extends FragmentActivity
{  
	private ListView maListViewPerso;
	private boolean isLoaded; 
	private static final int STOPSPLASH = 0;
	private static final int MAJ=1;
	private static final int REFRESH_COLOR_LIST=2;
	private static final int TIMER_HANDLER=300;
	private BroadcastReceiver broadcastReceiver;
	private IntentFilter intention; 

	//header
	private static boolean dateLoaded;
	private static TextView date;
	private static TextView heure;
	private static TextView routeSens;
	private static TextView pkHeader;
	private static TextView reseauRelais;
	private static TextView tx_locuteur;
	private static TextView infoAppel;
	private static TextView canalIdentite;

	private static Date dateGPS;

	private int currentModuleView=0;

	//Variable à modifier a chaque fois qu'on crée un nouveau module
	private boolean idModulePrisEnCharge;



	private ImageView voieDroiteOk;
	private ImageView voieDroiteNOk;
	private ImageView voieGaucheOk;
	private ImageView voieGaucheNOk;

	private ImageView gpsOk;
	private ImageView gpsNok;

	private ImageView wifiOk;
	private ImageView wifiNok;

	private ImageView reseauOk;
	private ImageView reseauNok;

	private ImageView volumeEtat;

	//bouton pour volume
	private Button volumePlus;
	private Button volumeMoins;
	private boolean attendInfoVolume=false;
	private long derniereDemandeVolume=0;

	private int currentZoneColore=0;
	private int indiceBarreGraphe=0;

	private TextView volumeTexteEnCours;
	private TextView volumeTexteMax;

	private AlertDialog dialogueEnComm;

	private String pathAffichageMenu="/ANDREAS/configuration/Affichage_Parametre.xml"; 
	private String pathCodeActivite="/ANDREAS/configuration/Code_activite.xml";
	private String pathJournal="/ANDREAS/configuration/Journal.txt";

	public static DecimalFormat df = new DecimalFormat("#.##");
	private Pattern pattern=Pattern.compile("1:[0-9]{3,6}");
	private Matcher matcher;
	private static SimpleDateFormat formaterDate;

	private PowerManager.WakeLock wl;
	

	private Timer timerRefresh;
	private long tempsDerniereDataChamp=0;	

	private Timer timerFenetreAppel;
	private int nombrePassageCompteurTimerFenetreAppel;

	private String numeroAppel;



	private final transient Handler splashHandler = new Handler()
	{  
		@Override
		public void handleMessage(Message msg)
		{
			/*if (msg.what == STOPSPLASH)
			{

				//Permet le chargement du fragment contenant l'affichage du PK
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

				Fragment newFragment = new ViewMain();
				ft.add(R.id.contentFrag, newFragment);
				ft.commit();


				//garde en memoire la vue chargée
				currentModuleView=-1;   

				isLoaded=true;

				//charge le contenu de l'affichage
				loadContentAndView();

				//Pour rafraichir le barreGraphe du ChampRadio + reseau+ relais
				timerRefresh=new Timer();
				timerRefresh.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {

						if( ((System.currentTimeMillis()-tempsDerniereDataChamp)>(1000*60)) || tempsDerniereDataChamp==0)
							changeEtatChamp(null);

					}
				}, 1000*60, 1000*60);

			}  */
			/**
			 * Permet de mettre à jour le menu selectionné dans la liste
			 */ 
			/*else if(msg.what==REFRESH_COLOR_LIST)
			{  

				for(int i=maListViewPerso.getFirstVisiblePosition(); i<= maListViewPerso.getLastVisiblePosition(); i++)
				{   

					// if(i == position-maListViewPerso.getFirstVisiblePosition())
					if(i==currentModuleView)
					{  
						//Pour dessiner un background 
						//maListViewPerso.getChildAt(i).setBackgroundColor(Color.BLACK);
						//remet le texte en blanc
						if(maListViewPerso.getChildAt(i-maListViewPerso.getFirstVisiblePosition())!=null)  
						{     	        					
							// 	maListViewPerso.getChildAt(currentModuleView-maListViewPerso.getFirstVisiblePosition()).setBackgroundColor(Color.WHITE);
							((TextView) (maListViewPerso.getChildAt(currentModuleView-maListViewPerso.getFirstVisiblePosition())).findViewById(R.id.affichemenuDescription)).setTextColor(Color.RED);
					
						}   

					}    
					else     
					{   

						//Pour dessiner un background
						// maListViewPerso.getChildAt(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.degrade));
						//remet le texte en noir

						if(maListViewPerso.getChildAt(i-maListViewPerso.getFirstVisiblePosition())!=null)
						{ 
							// maListViewPerso.getChildAt(i-maListViewPerso.getFirstVisiblePosition()).setBackgroundColor(Color.BLACK); 
							((TextView) (maListViewPerso.getChildAt(i-maListViewPerso.getFirstVisiblePosition())).findViewById(R.id.affichemenuDescription)).setTextColor(Color.BLACK);
						
						}
					}    

				} 

			}   */    

			super.handleMessage(msg);
		}
	};

	/**
	 * Permet d'instancier l'activité principale
	 */
	public void onCreate(Bundle savedInstanceState) 
	{  
		super.onCreate(savedInstanceState);


		Thread.setDefaultUncaughtExceptionHandler(new SRSDexception());

		requestWindowFeature(Window.FEATURE_NO_TITLE);   

		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_FULLSCREEN);

		
		loadContentAndView();
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		Fragment newFragment = new ViewAnimalList();
		ft.add(R.id.contentFrag, newFragment);
		ft.commit();
		

		
		//Au premier lancement de l'application
		/*if(savedInstanceState==null) 
		{               
			//Pour la gestion de la langue 

			/*String languageToLoad = "fr";
	        	Locale locale = new Locale(languageToLoad);
	        	Locale.setDefault(locale);

	        	Configuration config = new Configuration();
	        	config.locale = locale; 
	        	this.getBaseContext().getResources().updateConfiguration(config, null);*/ 

			/*if(Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("English"))
				ModeleDonnees.getInstance().langueAnglais=true;

			isLoaded=false;   
			dateLoaded=false;  		    

			final Message msg = new Message();
			msg.what = STOPSPLASH;
			splashHandler.sendMessageDelayed(msg, 2000);  

			//lecture de configuration des modules a afficher dans la liste
			//affectation au tableau de boolean




			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
					| PowerManager.ON_AFTER_RELEASE, this.getClass().getName());

			wl.acquire();

			//Chargement du fichier XML de code activité
			//Regle le problème du long Chargement 
			/*new Thread(new Runnable() {

				@Override
				public void run() {
					ModeleDonnees.getInstance().codeActivite=new CodeActivite();

					//recupère l'arbre
					ModeleDonnees.getInstance().codeActivite.chargerFichierCodeActiviteXML(pathCodeActivite);

				}
			}).start();*/

			/*formaterDate=new SimpleDateFormat("HH:mm:ss");
			formaterDate.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));


			setContentView(R.layout.main); 



		}
		//cas qui se produit lors de la rotation
		else
		{
			//restaurer les modules
			currentModuleView=savedInstanceState.getInt("currentFragment");

			currentModuleView=0;


			loadContentAndView();

			//restaure les valeurs
			date.setText(savedInstanceState.getString("date"));
			routeSens.setText(savedInstanceState.getString("routeSens"));
			pkHeader.setText(savedInstanceState.getString("pkHeader"));

			timerFenetreAppel=null;



		}    */

		//ajout des broadcast Receiver pour le header
		broadcastReceiver= new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) 
			{
				

				/*if(intent.getAction().equalsIgnoreCase(EVT.EVT_MODULE_AIGUILLAGE_ACK_OR_DATA))
						ReceptionAppel(intent);*/

				/*if(intent.getAction().equalsIgnoreCase(EVT.EVT_ALTERNAT) )
					{
						//Permet de récupérer le nom du menu
						String nomItem="";

			     		//Parcours de la map pour récupérer le nom du menu par rapport à la position
			     		for (Iterator i = ModeleDonnees.getInstance().viewCorrespondancePosition.keySet().iterator() ; i.hasNext() ; )
			     		{
			     		    String key = (String) i.next();

			     		    if(currentModuleView==ModeleDonnees.getInstance().viewCorrespondancePosition.get(key))
			     		    	nomItem=key;
			     		}

			     		//Recupère le nom de la classe à lancer en fonction du nom du menu        		
			     		for (Iterator i = ModeleDonnees.getInstance().viewCorrespondanceTexte.keySet().iterator() ; i.hasNext() ; )
			     		{
			     		    String key = (String) i.next();
			     		    if(nomItem.equalsIgnoreCase(ModeleDonnees.getInstance().viewCorrespondanceTexte.get(key)))
			     		    	nomItem=key;
			     		}

						if( (!(nomItem.equalsIgnoreCase(ModeleDonnees.getInstance().ECRAN_APPEL)) || nomItem.equalsIgnoreCase("")) && ModeleDonnees.getInstance().estEnAppel && intent.getExtras().getBoolean(EVT.EXTRA_ALTERNAT))
							((ImageView) dialogueEnComm.findViewById(R.id.telephoneRaccroche)).setVisibility(View.VISIBLE);
						else if((!(nomItem.equalsIgnoreCase(ModeleDonnees.getInstance().ECRAN_APPEL)) || nomItem.equalsIgnoreCase("")) && ModeleDonnees.getInstance().estEnAppel && intent.getExtras().getBoolean(EVT.EXTRA_ALTERNAT)==false)
							((ImageView) dialogueEnComm.findViewById(R.id.telephoneRaccroche)).setVisibility(View.GONE);
					}*/

				//Permet d'afficher le pk
				
				if(intent.getAction().equalsIgnoreCase(""))
				{
					

						int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
						for (int i = 0; i < backStackCount; i++) {

							// Get the back stack fragment id.
							int backStackId = getSupportFragmentManager().getBackStackEntryAt(i).getId();

							getSupportFragmentManager().popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);

						

						onDestroy();
					}
				}

				//Permet d'afficher le locuteur actuelle
				/*if(intent.getAction().equalsIgnoreCase(EVT.EVT_TX_KEY_ON))
						changeEtatTXKEYON(intent);*/



			}
		};

		//attache les intentions 
		intention= new IntentFilter("");

		//intention.addAction(EVT.EVT_TX_KEY_ON);
		registerReceiver(broadcastReceiver, intention); 


	}



	public void onDestroy()
	{	
		super.onDestroy();
	

		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());

	} 

	public void OnResume()
	{
		super.onResume();
	}

	/**
	 * On ne permet pas de passer l'application en pause
	 * On quitte l'application et fini tous les services
	 */
	public void onPause()
	{
		super.onPause();
	}



	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
		//l'item de la listeView
		savedInstanceState.putInt("currentFragment", currentModuleView);

		//Pour le header
		if(date!=null && routeSens!=null && pkHeader!=null)
		{
			savedInstanceState.putString("date", (String) date.getText());  
			savedInstanceState.putString("routeSens", (String) routeSens.getText());
			savedInstanceState.putString("pkHeader", (String) pkHeader.getText());
		}  

		//sauvegarder les modules à charger 


		super.onSaveInstanceState(savedInstanceState);
	}


	/* @Override
	 public void onAttachedToWindow()
	 {  
	     this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);     
	     super.onAttachedToWindow();  
	 }*/ 



	/**
	 * Pour empecher de quitter l'application	 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{ 
		if (keyCode == KeyEvent.KEYCODE_BACK)
			return true; 



		if (keyCode == KeyEvent.KEYCODE_HOME) 
			return true;

		return super.onKeyDown(keyCode, event);    
	}

	/**
	 * Charge l'ensemble des outils/modules disponibles dans la barre de droite
	 * @return
	 */
	private SimpleAdapter chargeMenu()
	{   
		//Création de la ArrayList qui nous permettra de remplire la listView
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

		//On déclare la HashMap qui contiendra les informations pour un item
		HashMap<String, String> map; 

		for(int i=0;i<4;i++)
		{ 
			map = new HashMap<String, String>(); 
			map.put("affichemenuDescription", "mon chien");
			map.put("affichemenuImage", String.valueOf(R.drawable.ic_launcher));
			//on insère la référence à l'image (convertit en String car normalement c'est un int) que l'on récupérera dans l'imageView créé dans le fichier affichageitem.xml

			//permet d'associer les icones en fonction de l'écran
			/*if(ModeleDonnees.getInstance().listeMenu.get(i).getVue().trim().equalsIgnoreCase("ECRAN_APPEL"))
			{
				map.put("affichemenuImage", String.valueOf(R.drawable.telephoneinverse));
			}else if(ModeleDonnees.getInstance().listeMenu.get(i).getVue().trim().equalsIgnoreCase("ECRAN_FLU"))
			{
				map.put("affichemenuImage", String.valueOf(R.drawable.image_autoroute));
			}else if(ModeleDonnees.getInstance().listeMenu.get(i).getVue().trim().equalsIgnoreCase("ECRAN_CODE_ACTIVITE"))
			{
				map.put("affichemenuImage", String.valueOf(R.drawable.code_activite));
			}else if(ModeleDonnees.getInstance().listeMenu.get(i).getVue().trim().equalsIgnoreCase("ECRAN_PRINCIPAL"))
			{
				map.put("affichemenuImage", String.valueOf(R.drawable.image_home));
			}else if(ModeleDonnees.getInstance().listeMenu.get(i).getVue().trim().equalsIgnoreCase("ECRAN_PK"))
			{
				map.put("affichemenuImage", String.valueOf(R.drawable.image_borne));
			}else if(ModeleDonnees.getInstance().listeMenu.get(i).getVue().trim().equalsIgnoreCase("ECRAN_PHOTO"))
			{
				map.put("affichemenuImage", String.valueOf(R.drawable.image_app_photo));
			}else if(ModeleDonnees.getInstance().listeMenu.get(i).getVue().trim().equalsIgnoreCase("ECRAN_PARAM_MOBILE"))
			{
				map.put("affichemenuImage", String.valueOf(R.drawable.image_frequence));
			}*/   	 

			//enfin on ajoute cette hashMap dans la arrayList   
			listItem.add(map);   

			//Permet de remplir les associations suivantes:
			//- (Nom du menu,position dans le menu)
			//- (Nom du menu,nom de la classe permettant l'affichage du menu)
			//ModeleDonnees.getInstance().viewCorrespondancePosition.put(ModeleDonnees.getInstance().listeMenu.get(i).getLibelle(),ModeleDonnees.getInstance().listeMenu.get(i).getPosition());
			//ModeleDonnees.getInstance().viewCorrespondanceTexte.put(ModeleDonnees.getInstance().listeMenu.get(i).getVue(),ModeleDonnees.getInstance().listeMenu.get(i).getLibelle());
		} 


		//Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affiche_menu_image,
				new String[] {"affichemenuDescription","affichemenuImage"}, new int[] {R.id.affichemenuDescription,R.id.affichemenuImage});

		return mSchedule;   
	} 







	/**                                          
	 * Charge l'ecran ANDREAS contenant:
	 * - un bandeau haut (fragment)
	 * - un content (un fragment)
	 * - une listeView pouvant changer le content en fonction du choix
	 */        
	private void loadContentAndView()
	{      
		setContentView(R.layout.main); 

		//Récupération de la listview créée dans le fichier main.xml
		/*maListViewPerso = (ListView) findViewById(R.id.ListeChoix);    

		//On attribut à notre listView l'adapter que l'on vient de créer
		maListViewPerso.setAdapter(chargeMenu()); 

		maListViewPerso.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {

				//Pour charger un autre fragment et chnager le content
				FragmentManager fm=getSupportFragmentManager();
				FragmentTransaction ft=fm.beginTransaction();

				Fragment frag=null;

				System.out.println("position aff"+ position+":"+maListViewPerso.getFirstVisiblePosition()+":"+currentZoneColore); 

				String nomItem="";


				//met à jour la position courante          
				currentModuleView=position;

				//indique qu'il faut changer la couleur du texte du menu selectionné
				final Message msg = new Message();
				msg.what = REFRESH_COLOR_LIST;
				splashHandler.sendMessageDelayed(msg, TIMER_HANDLER);    
			}
		});*/
	}

			

}
