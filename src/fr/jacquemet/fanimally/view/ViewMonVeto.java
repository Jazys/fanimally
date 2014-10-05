package fr.jacquemet.fanimally.view;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import com.jjoe64.graphview.GraphView;

import fr.jacquemet.fanimally.R;
import fr.jacquemet.fanimally.bdd.FanimallyContentProvider;
import fr.jacquemet.fanimally.bdd.Table_Animal;
import fr.jacquemet.fanimally.bdd.Table_COURBE;
import fr.jacquemet.fanimally.bdd.Table_VETO;
import fr.jacquemet.fanimally.utils.VetoListAdapter;
import fr.jacquemet.fanimally.utils.VetoName;
import fr.jacquemet.fanimally.utils.utils;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;



public class ViewMonVeto extends Fragment
{
	private BroadcastReceiver broadcastReceiver;
	private IntentFilter intention;

	private Button btn_valider;


	private View view ;	


	private boolean modeAdd;
	private EditText nomVeto;
	private EditText adresseVeto;
	private EditText numVeto;

	
	private VetoListAdapter adapter;
	private ListView atomPaysListView;
	


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
		
		
		view = inflater.inflate(R.layout.list_veto, null);
		
		modeAdd=false;
				
		btn_valider = (Button) view.findViewById(R.id.vetoValide); 
		btn_valider.setText(getActivity().getResources().getString(R.string.ajouter_lib));
		
		nomVeto= (EditText) view.findViewById(R.id.editVetoNom);
		adresseVeto= (EditText) view.findViewById(R.id.editAdresseVeto);
		numVeto= (EditText) view.findViewById(R.id.editTelVeto);
		
		nomVeto.setText("");
		adresseVeto.setText("");
		numVeto.setText("");
		

		//remplit la liste des veto
		fillVeto();
		
		
		btn_valider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(modeAdd)
				{
					
					view.findViewById(R.id.nomveto_veto).setVisibility(View.GONE);
					view.findViewById(R.id.adresse_veto).setVisibility(View.GONE);
					view.findViewById(R.id.tel_veto).setVisibility(View.GONE);
					
					modeAdd=false;
					
					
					ContentValues vetoInsert = new ContentValues();
					vetoInsert.put(Table_VETO.NAME_VETO, nomVeto.getText().toString());
					vetoInsert.put(Table_VETO.ADRESS, adresseVeto.getText().toString());
					vetoInsert.put(Table_VETO.TEL, numVeto.getText().toString());			
										

					getActivity().getContentResolver().insert(FanimallyContentProvider.CONTENT_URI_VETO, vetoInsert);

					vetoInsert.clear(); 

					Toast.makeText(getActivity().getApplicationContext(), "Vétérinaire enregistré", Toast.LENGTH_LONG).show(); 
					
					btn_valider.setText(getActivity().getResources().getString(R.string.ajouter_lib));
					
					fillVeto();
				}
				else 
				{
					modeAdd=true;
					view.findViewById(R.id.nomveto_veto).setVisibility(View.VISIBLE);
					view.findViewById(R.id.adresse_veto).setVisibility(View.VISIBLE);
					view.findViewById(R.id.listeVeto).setVisibility(View.GONE);	

					view.findViewById(R.id.tel_veto).setVisibility(View.VISIBLE);
					
					btn_valider.setText(getActivity().getResources().getString(R.string.validate));
				}

			}
		});
		return view; 
	}

	private void fillVeto()
	{
		
		adapter = new VetoListAdapter(getActivity(), R.layout.affiche_veto_ligne, new ArrayList<VetoName>());
		atomPaysListView = (ListView)view.findViewById(R.id.listVeto);
		atomPaysListView.setAdapter(adapter);
		
		view.findViewById(R.id.listeVeto).setVisibility(View.VISIBLE);	
		
		if(adapter.isEmpty()== false)
			adapter.clear();	
		
		//Recupère l'image et le nom de l'animal
		String columns[] = new String[] { Table_VETO.ID, Table_VETO.NAME_VETO , Table_VETO.TEL};
		Uri mContacts = FanimallyContentProvider.CONTENT_URI_VETO;
		Cursor cur = getActivity().getContentResolver().query(mContacts, columns, null, null, null);

		if(cur !=null)
		{
			int count=cur.getCount();	

			cur.moveToFirst();

			//Parcours la liste des animaux  
			for(int i=0;i<count;i++)
			{ 
				adapter.add(new VetoName(cur.getString(cur.getColumnIndex(Table_VETO.NAME_VETO)),cur.getString(cur.getColumnIndex(Table_VETO.TEL)))); 	
				cur.moveToNext();  
			}  

			cur.close();
		}
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
