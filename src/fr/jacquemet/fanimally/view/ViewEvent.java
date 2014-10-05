package fr.jacquemet.fanimally.view;


import java.util.ArrayList;
import java.util.HashMap;

import fr.jacquemet.fanimally.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;



public class ViewEvent extends Fragment
{
	private BroadcastReceiver broadcastReceiver;
	private IntentFilter intention;
	
	private ListView maListViewPerso;


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

		return view; 
	}

	private SimpleAdapter chargeMenu()
	{   
		//Création de la ArrayList qui nous permettra de remplire la listView
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

		//On déclare la HashMap qui contiendra les informations pour un item
		HashMap<String, String> map; 
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", "Mes animaux");
		map.put("affichemenuImage", String.valueOf(R.drawable.ic_launcher));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", "Traitements");
		map.put("affichemenuImage", String.valueOf(R.drawable.ic_launcher));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", "Vaccins");
		map.put("affichemenuImage", String.valueOf(R.drawable.ic_launcher));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", "Interventions");
		map.put("affichemenuImage", String.valueOf(R.drawable.ic_launcher));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", "Rendez-vous");
		map.put("affichemenuImage", String.valueOf(R.drawable.ic_launcher));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", "Courbe");
		map.put("affichemenuImage", String.valueOf(R.drawable.ic_launcher));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", "Vermifuge");
		map.put("affichemenuImage", String.valueOf(R.drawable.ic_launcher));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", "Mon vétérinaire");
		map.put("affichemenuImage", String.valueOf(R.drawable.ic_launcher));
		listItem.add(map);  
		
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", "Configurations");
		map.put("affichemenuImage", String.valueOf(R.drawable.ic_launcher));
		listItem.add(map);  
		

	

		//Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter (this.getActivity().getBaseContext(), listItem, R.layout.affiche_menu_image,
				new String[] {"affichemenuDescription","affichemenuImage"}, new int[] {R.id.affichemenuDescription,R.id.affichemenuImage});

		return mSchedule;   
	} 




}
