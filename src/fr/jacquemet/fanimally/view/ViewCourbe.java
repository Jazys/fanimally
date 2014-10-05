package fr.jacquemet.fanimally.view;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

import fr.jacquemet.fanimally.R;
import fr.jacquemet.fanimally.bdd.FanimallyContentProvider;
import fr.jacquemet.fanimally.bdd.Table_Animal;
import fr.jacquemet.fanimally.bdd.Table_COURBE;
import fr.jacquemet.fanimally.bdd.Table_RDV;
import fr.jacquemet.fanimally.utils.utils;



public class ViewCourbe extends Fragment
{
	private BroadcastReceiver broadcastReceiver;
	private IntentFilter intention;

	private Button btn_voir;
	private Button btn_ajouter;
	private Button btn_valider;

	private ArrayAdapter<String> adapter;
	private Spinner spinnerAnimal;

	private String animalName;
	private View view ;
	private Timer timer;

	private GraphView graphView;

	private boolean modeAdd;
	private EditText datePoids;
	private EditText comment;
	private EditText saisirPoids;
	private ImageButton calendar;
	private int year;
	private int day;
	private int month;
	private int minute;
	private int heure;


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
		view = inflater.inflate(R.layout.courbe, null);

		//Récupération de la listview créée dans le fichier main.xml
		btn_voir = (Button) view.findViewById(R.id.courbe_voir_btn);    
		btn_ajouter = (Button) view.findViewById(R.id.courbe_ajouter_btn);   
		btn_valider = (Button) view.findViewById(R.id.courbe_btn_valider);   

		datePoids= (EditText) view.findViewById(R.id.editTextDate);
		datePoids.setText("");

		comment= (EditText) view.findViewById(R.id.editTextComment);
		comment.setText("");

		saisirPoids= (EditText) view.findViewById(R.id.editTextPoids);
		saisirPoids.setText("");

		calendar= (ImageButton) view.findViewById(R.id.imageButtonCalencar);


		graphView = new LineGraphView(
				getActivity().getApplicationContext() // context
				, " Poids (m/kg) " // heading
				);


		btn_voir.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				view.findViewById(R.id.courbe_animal).setVisibility(View.VISIBLE);
				view.findViewById(R.id.courbe_animal_linear).setVisibility(View.VISIBLE);

				view.findViewById(R.id.courbe_graphique).setVisibility(View.VISIBLE);
				view.findViewById(R.id.courbe_btn).setVisibility(View.VISIBLE);

				view.findViewById(R.id.courbe_date).setVisibility(View.GONE);
				view.findViewById(R.id.courbe_saisie).setVisibility(View.GONE);
				view.findViewById(R.id.courbe_comment).setVisibility(View.GONE);	
				view.findViewById(R.id.courbe_graphique_linear).setVisibility(View.GONE);	

				((LinearLayout) view.findViewById(R.id.courbe_graphique_linear)).removeAllViews();

				btn_valider.setText("Afficher la courbe");
				
				modeAdd=false;

			}   
		});

		btn_ajouter.setOnClickListener(new OnClickListener() {
		

			@Override
			public void onClick(View v) {

				
				modeAdd=true;
				
				view.findViewById(R.id.courbe_animal).setVisibility(View.VISIBLE);				
				view.findViewById(R.id.courbe_graphique).setVisibility(View.GONE);
				view.findViewById(R.id.courbe_animal_linear).setVisibility(View.VISIBLE);
				view.findViewById(R.id.courbe_graphique_linear).setVisibility(View.GONE);

				view.findViewById(R.id.courbe_date).setVisibility(View.VISIBLE);
				view.findViewById(R.id.courbe_saisie).setVisibility(View.VISIBLE);
				view.findViewById(R.id.courbe_comment).setVisibility(View.VISIBLE);
				view.findViewById(R.id.courbe_btn).setVisibility(View.VISIBLE);

				((LinearLayout) view.findViewById(R.id.courbe_graphique_linear)).removeAllViews();
				btn_valider.setText("Valider poids");

				calendar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						final Calendar c = Calendar.getInstance();

						if(year==0 && month==0 && day==0)
						{
							year = c.get(Calendar.YEAR);
							month = c.get(Calendar.MONTH);
							day = c.get(Calendar.DAY_OF_MONTH);
						}
						else
						{
							c.set(year, month, day);
						}

						DatePickerDialog dpd = new DatePickerDialog(getActivity(),
								new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int yearR,
									int monthOfYear, int dayOfMonth) {

								year=yearR;
								month=monthOfYear;
								day=dayOfMonth;

								if( ((monthOfYear+1)/10)>0)
								{

									datePoids.setText(dayOfMonth + "/"
											+ (monthOfYear + 1) + "/" + year);
								}
								else
								{

									datePoids.setText(dayOfMonth + "/0"
											+ (monthOfYear + 1) + "/" + year);
								}							

							}
						}, year, month, day);
						dpd.show();




					}
				});


			}
		});

		btn_valider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(modeAdd)
				{
					String poidsStr= saisirPoids.getText().toString();
					Float poidsFloat=Float.parseFloat(poidsStr);
					ContentValues poidsInsert = new ContentValues();
					poidsInsert.put(Table_COURBE.NAME_ANIMAL, animalName);
					poidsInsert.put(Table_COURBE.DATE, utils.getDateTimeMin(year,month,day,heure,minute,0));
					poidsInsert.put(Table_COURBE.COMMENT, comment.getText().toString());			
					poidsInsert.put(Table_COURBE.TYPE, 0);	
					poidsInsert.put(Table_COURBE.VALUE, poidsFloat);						

					getActivity().getContentResolver().insert(FanimallyContentProvider.CONTENT_URI_COURBE, poidsInsert);

					poidsInsert.clear(); 

					Toast.makeText(getActivity().getApplicationContext(), "Poids enregistré", Toast.LENGTH_LONG).show(); 
				}
				else
				{
					drawCurve();
				}

			}
		});

		spinnerAnimal = (Spinner) view.findViewById(R.id.spinnerSelectAnimal);		
		spinnerAnimal.setAdapter(createSPinnerAnimal());

		spinnerAnimal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
			{
				animalName= parent.getItemAtPosition(pos).toString();	
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				

			} 

		});




		return view; 
	}

	private ArrayAdapter<String> createSPinnerAnimal()
	{

		String[] tabAnimal = null;		
		int nombreAnimal=0;

		//Recupère l'image et le nom de l'animal
		String columns[] = new String[] { Table_Animal.ID, Table_Animal.NAME , Table_Animal.PATHIMAGE};
		Uri mContacts = FanimallyContentProvider.CONTENT_URI_ANIMAL;
		Cursor cur = getActivity().getContentResolver().query(mContacts, columns, null, null, null);

		if(cur !=null)
		{
			nombreAnimal=cur.getCount();
			tabAnimal=new String[nombreAnimal];			

			System.out.println("nombre ani "+nombreAnimal); 

			cur.moveToFirst();

			//Parcours la liste des animaux  
			for(int i=0;i<nombreAnimal;i++)
			{ 
				tabAnimal[i]=cur.getString(cur.getColumnIndex(Table_Animal.NAME));
				cur.moveToNext();
			} 

			cur.close();
		}

		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, tabAnimal);	

		return adapter;

	}

	private void drawCurve()
	{	
		view.findViewById(R.id.courbe_graphique_linear).setVisibility(View.VISIBLE);
		view.findViewById(R.id.courbe_animal_linear).setVisibility(View.VISIBLE);


		int nbRecord=0;
		GraphViewSeries curvePoids = null;
		Date dateDataPoids = null;
		Date bitrhDayAnimal = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd", Locale.getDefault());

		/**
		 * Récupération de la date de naissance d el'animal
		 */

		String columns[] = new String[] {Table_Animal.NAME ,Table_Animal.BIRTHDATE, Table_Animal.RACE, Table_Animal.ROBE
				, Table_Animal.SPECIE, Table_Animal.SEXE, Table_Animal.ASSURANCE, Table_Animal.PATHIMAGE,Table_Animal.TATOUAGE};
		Uri animal = FanimallyContentProvider.CONTENT_URI_ANIMAL;
		Cursor cur = getActivity().getContentResolver().query(animal, columns, Table_Animal.NAME +" ="+"'"+animalName+"'", null, null);


		if(cur !=null) 
		{		
			cur.moveToFirst(); 

			try {
				bitrhDayAnimal=dateFormat.parse(cur.getString(cur.getColumnIndex(Table_Animal.BIRTHDATE)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

			cur.close();
		}
		
		System.out.println(bitrhDayAnimal.toString());

		/**
		 * Récupération des différents poids de l'animal
		 */

		String columnsCourbe[] = new String[] {Table_COURBE.NAME_ANIMAL , Table_COURBE.DATE, Table_COURBE.VALUE};
		Uri poidsListe = FanimallyContentProvider.CONTENT_URI_COURBE;
		Cursor curCourbe = getActivity().getContentResolver().query(poidsListe,columnsCourbe, null,  null, Table_COURBE.DATE + " ASC");

		if(curCourbe !=null)
		{
			
			
			nbRecord=curCourbe.getCount();
			
			System.out.println(nbRecord);
			curCourbe.moveToFirst();

			GraphViewData[] data = new GraphViewData[nbRecord];

			for (int i=0; i<nbRecord; i++) 
			{


				try {
					dateDataPoids=dateFormat.parse(curCourbe.getString(curCourbe.getColumnIndex(Table_COURBE.DATE)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println(utils.getMonthsDifference(dateDataPoids, bitrhDayAnimal));
				
				data[i] = new GraphViewData(utils.getMonthsDifference(bitrhDayAnimal, dateDataPoids), curCourbe.getFloat(curCourbe.getColumnIndex(Table_COURBE.VALUE)));

				curCourbe.moveToNext();
			}

			curvePoids = new GraphViewSeries("Poids (mois/kg)", new GraphViewSeriesStyle(Color.rgb(200, 50, 00), 3), data);

			curCourbe.close();
		}
		else
		{
			curvePoids = new GraphViewSeries(new GraphViewData[] { new GraphViewData(0, 0)});

		}

		// init example series data
		/*GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
		    new GraphViewData(0, 3d)
		    , new GraphViewData(1, 4d)
		    , new GraphViewData(2, 4.25d)
		    , new GraphViewData(3, 5d)
		    , new GraphViewData(4, 6d)
		    , new GraphViewData(5, 6.5d)
		    , new GraphViewData(6, 7d)
		    , new GraphViewData(7, 7.1d)
		    , new GraphViewData(8, 7.2d)
		    , new GraphViewData(9, 7.3d)
		    ,new GraphViewData(10, 3d)
		    , new GraphViewData(11, 4d)
		    , new GraphViewData(12, 4.25d)
		    , new GraphViewData(13, 5d)
		    , new GraphViewData(14, 6d)
		    , new GraphViewData(15, 6.5d)
		    , new GraphViewData(16, 7d)
		    , new GraphViewData(17, 7.1d)
		    , new GraphViewData(18, 7.2d)
		    , new GraphViewData(19, 7.3d)
		    ,new GraphViewData(20, 3d)
		    , new GraphViewData(21, 4d)
		    , new GraphViewData(22, 4.25d)
		    , new GraphViewData(23, 5d)
		    , new GraphViewData(24, 6d)
		    , new GraphViewData(25, 6.5d)
		    , new GraphViewData(26, 7d)
		    , new GraphViewData(27, 7.1d)
		    , new GraphViewData(28, 7.2d)
		    , new GraphViewData(29, 7.3d)
		    , new GraphViewData(30, 50d)
		});*/


		graphView.getGraphViewStyle().setTextSize(12);
		graphView.setScalable(true);
		graphView.setScrollable(true); 
		graphView.setViewPort(0, 10);
		graphView.addSeries(curvePoids); // data
		//graphView.getGraphViewStyle().setGridColor(Color.BLUE);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);		   


		LinearLayout layout = (LinearLayout) view.findViewById(R.id.courbe_graphique_linear);
		layout.addView(graphView);


		view.findViewById(R.id.courbe_animal).setVisibility(View.GONE);
		view.findViewById(R.id.courbe_animal_linear).setVisibility(View.GONE);
		view.findViewById(R.id.courbe_btn).setVisibility(View.INVISIBLE);
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
