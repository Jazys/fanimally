package fr.jacquemet.fanimally.view;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import fr.jacquemet.fanimally.R;
import fr.jacquemet.fanimally.bdd.FanimallyContentProvider;
import fr.jacquemet.fanimally.bdd.Table_Animal;
import fr.jacquemet.fanimally.utils.MyViewBinder;



public class ViewAnimalList extends Fragment
{
	private BroadcastReceiver broadcastReceiver;
	private IntentFilter intention;
	
	private int positionItemAjoutAnimal=0;

	private ListView maListViewPerso;
	private String[] nomAnimal;


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
		//getActivity().unregisterReceiver(broadcastReceiver);

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
		
		maListViewPerso.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				
				//On veut ajouter un animal
				if(position==positionItemAjoutAnimal)
				{
					FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

					Fragment newFragment = new ViewAddAnimal();
					ft.replace(R.id.contentFrag, newFragment);
					ft.commit();
				}
				//C'est pour voir la fiche d'un animal
				else
				{ 
					FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
 
					//
					Fragment newFragment = new ViewAfficheAnimal(nomAnimal[position]);
					ft.replace(R.id.contentFrag, newFragment);
					ft.commit();
				} 
				
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

		int nombreAnimal=0;
		int i=0;
		
				


		//Recupère l'image et le nom de l'animal
		String columns[] = new String[] { Table_Animal.ID, Table_Animal.NAME , Table_Animal.PATHIMAGE};
		Uri mContacts = FanimallyContentProvider.CONTENT_URI_ANIMAL;
		Cursor cur = getActivity().getContentResolver().query(mContacts, columns, null, null, null);

		if(cur !=null)
		{
			nombreAnimal=cur.getCount();	
			
			nomAnimal=new String[nombreAnimal];
			
			System.out.println("nombre ani "+nombreAnimal); 
	
			cur.moveToFirst();
	
			//Parcours la liste des animaux  
			for(i=0;i<nombreAnimal;i++)
			{ 
				map = new HashMap<String, String>(); 
				
				String nameReqAnimal=cur.getString(cur.getColumnIndex(Table_Animal.NAME));
	 
				//Pour afficher le nom de l'animal
				map.put("affichemenuDescription", nameReqAnimal );
	
				//Faire un requete pour récupérer l'image de l'animal			
				map.put("affichemenuImage", cur.getString(cur.getColumnIndex(Table_Animal.PATHIMAGE)));
				//on insère la référence à l'image (convertit en String car normalement c'est un int) que l'on récupérera dans l'imageView créé dans le fichier affichageitem.xml
	
	
				//enfin on ajoute cette hashMap dans la arrayList   
				listItem.add(map);   
				cur.moveToNext();
				
				nomAnimal[i]=nameReqAnimal;
			} 
	
			cur.close();
		}
		
		positionItemAjoutAnimal=i;
		//Permet d'ajouter un animal
		map = new HashMap<String, String>(); 
		map.put("affichemenuDescription", "Ajouter un animal");
		map.put("affichemenuImage", String.valueOf(R.drawable.plus_orange));
		listItem.add(map);  

		//Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter (this.getActivity().getBaseContext(), listItem, R.layout.affiche_menu_image,
				new String[] {"affichemenuDescription","affichemenuImage"}, new int[] {R.id.affichemenuDescription,R.id.affichemenuImage});

		return mSchedule;    
	} 




}
