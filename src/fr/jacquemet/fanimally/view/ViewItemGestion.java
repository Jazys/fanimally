package fr.jacquemet.fanimally.view;


import java.util.ArrayList;
import java.util.HashMap;

import fr.jacquemet.fanimally.R;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;



@SuppressLint("ValidFragment")
public class ViewItemGestion extends Fragment
{
	private BroadcastReceiver broadcastReceiver;
	private IntentFilter intention;
	
	
	private ListView maListViewPerso;

	
	public ViewItemGestion()
	{
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setRetainInstance(true);		


	} 


	@Override
	public void onDestroy()
	{
		//getActivity().unregisterReceiver(broadcastReceiver);

		//sauvegarder les data

		super.onDestroy();
	}  

	@Override
	public void onStop()
	{
		getActivity().unregisterReceiver(broadcastReceiver);

		//sauvegarder les data


		super.onDestroy();
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		broadcastReceiver= new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) 
			{


			}
		};		

		intention=new IntentFilter();	
		getActivity().registerReceiver(broadcastReceiver, intention);	


	} 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_fanimally, null);

		//Récupération de la listview créée dans le fichier main.xml
		maListViewPerso = (ListView) view.findViewById(R.id.listAnimal);    

		//On attribut à notre listView l'adapter que l'on vient de créer
		maListViewPerso.setAdapter(chargeMenu()); 
		
		maListViewPerso.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment newFragment=null;
				
				switch(position)
				{
					case 0:	
						newFragment = new ViewAnimalList();					
					break;
					case 1:	
						newFragment = new ViewVaccins();					
					break;
					case 2:	
						newFragment = new ViewTraitements();					
					break;
					case 3:	
						//intervention
						newFragment = new ViewTraitements();
										
					break;
					case 4:	
						newFragment = new ViewRdv();					
					break;
					case 5:	
						newFragment = new ViewCourbe();				
					break;
					case 6:	
						//reproduction		
					break;
					case 7:	
						//cout			
					break;
					case 8:	
						newFragment = new ViewMonVeto();			
					break;
					case 9:	
						newFragment = new ViewConfigurations();			
					break;
					default:					
						newFragment = new ViewAnimalList();					
						break;
					
				}
				
				ft.replace(R.id.contentFrag, newFragment);
				ft.commit();
				
				
			}
			
		});

		return view; 
	}

	private SimpleAdapter chargeMenu()
	{   
		//Création de la ArrayList qui nous permettra de remplire la listView
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

		//On déclare la HashMap qui contiendra les informations pour un item
		HashMap<String, String> map; 
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", getResources().getString(R.string.animalItem));
		map.put("affichemenuImage", String.valueOf(R.drawable.animal_item));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", getResources().getString(R.string.vaccinItem));
		map.put("affichemenuImage", String.valueOf(R.drawable.vaacin_item));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", getResources().getString(R.string.traitementItem));
		map.put("affichemenuImage", String.valueOf(R.drawable.traitement_item));
		listItem.add(map);  		
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", getResources().getString(R.string.interventionItem));
		map.put("affichemenuImage", String.valueOf(R.drawable.intervention_item));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", getResources().getString(R.string.rdvItem));
		map.put("affichemenuImage", String.valueOf(R.drawable.rdv_item));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", getResources().getString(R.string.courbeItem));
		map.put("affichemenuImage", String.valueOf(R.drawable.courbe_item));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", getResources().getString(R.string.reproductionItem));
		map.put("affichemenuImage", String.valueOf(R.drawable.reproduction_item));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", getResources().getString(R.string.coutItem));
		map.put("affichemenuImage", String.valueOf(R.drawable.cout_item));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", getResources().getString(R.string.vetoItem));
		map.put("affichemenuImage", String.valueOf(R.drawable.veto_item));
		listItem.add(map); 
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", getResources().getString(R.string.confItem));
		map.put("affichemenuImage", String.valueOf(R.drawable.conf_item));
		listItem.add(map);  
		

	

		//Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter (this.getActivity().getBaseContext(), listItem, R.layout.affiche_menu_image,
				new String[] {"affichemenuDescription","affichemenuImage"}, new int[] {R.id.affichemenuDescription,R.id.affichemenuImage});

		return mSchedule;   
	} 




}
