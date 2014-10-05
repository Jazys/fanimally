package fr.jacquemet.fanimally.view;


import java.util.Calendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
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
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import fr.jacquemet.fanimally.R;
import fr.jacquemet.fanimally.bdd.FanimallyContentProvider;
import fr.jacquemet.fanimally.bdd.Table_Animal;
import fr.jacquemet.fanimally.bdd.Table_RDV;
import fr.jacquemet.fanimally.utils.utils;



public class ViewRdv extends Fragment
{
	private BroadcastReceiver broadcastReceiver;
	private IntentFilter intention;
	private Spinner spinnerAnimal;
	private ArrayAdapter<String> adapter;
	private EditText dateRdv;
	private EditText commentaire;
	private ImageButton calendar;
	private int year;
	private int day;
	private int month;
	private Button btn_save;
	private int minute;
	private int heure;
	private String animalName="";
	

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
		View view = inflater.inflate(R.layout.rdv, null);		

		//la liste des animaux
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
				// TODO Auto-generated method stub

			} 

		});
		

		//la date
		calendar= (ImageButton) view.findViewById(R.id.imageButtonCalencar);
		dateRdv= (EditText) view.findViewById(R.id.editTextDate);
		dateRdv.setText("");

		//le commentaire
		commentaire= (EditText) view.findViewById(R.id.editTextComment);
		commentaire.setText("");

		//
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

							dateRdv.setText(dayOfMonth + "/"
									+ (monthOfYear + 1) + "/" + year);
						}
						else
						{

							dateRdv.setText(dayOfMonth + "/0"
									+ (monthOfYear + 1) + "/" + year);
						}
						
						TimePickerDialog timepicker = new TimePickerDialog(getActivity(),
								day, new OnTimeSetListener() {
									
									@Override
									public void onTimeSet(TimePicker view, int hourOfDay, int min) {
										heure=hourOfDay;
										minute=min;
										
									}
								},day, day ,false);
						timepicker.show();

					}
				}, year, month, day);
				dpd.show();
				
				
				
  
			}
		});

		btn_save=(Button) view.findViewById(R.id.btn_save_rdv);
		btn_save.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				
				//formattage de la date
				Calendar beginTime = Calendar.getInstance();
				beginTime.set(year, month, day, heure, minute);
						
				Calendar endTime = Calendar.getInstance();
				endTime.set(year, month, day, heure+1, minute);
			
				
				if (Build.VERSION.SDK_INT >= 14)
				{
					Intent intent = new Intent(Intent.ACTION_INSERT)
					.setData(Events.CONTENT_URI)
					.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())					
					.putExtra(Events.TITLE, "Famnially - RDV")
					.putExtra(Events.DESCRIPTION, commentaire.getText().toString())
					//.putExtra(Events.EVENT_LOCATION, "The gym")
					.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
					//.putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
					startActivity(intent);
					
					/*Calendar cal = Calendar.getInstance();
				    TimeZone tz = cal.getTimeZone();
					
					long calID = 3;
					long startMillis = 0; 
					long endMillis = 0;     
					
					startMillis = beginTime.getTimeInMillis();
					endMillis = endTime.getTimeInMillis();
					
					ContentResolver cr = getActivity().getContentResolver();
					ContentValues values = new ContentValues();
					values.put(Events.DTSTART, startMillis);
					values.put(Events.DTEND, endMillis);
					values.put(Events.TITLE, "Famnially - RDV");
					values.put(Events.DESCRIPTION, commentaire.getText().toString());
					values.put(Events.EVENT_TIMEZONE, tz.getDisplayName());
					values.put(Events.CALENDAR_ID, 3);
					Uri uri = cr.insert(Events.CONTENT_URI, values);
					
					int id = Integer.parseInt(uri.getLastPathSegment());
				    Toast.makeText(getActivity(), "Created Calendar Event " + id,
				        Toast.LENGTH_SHORT).show();*/ 
					
				
				}
				else 
				{
					/*Calendar cal = Calendar.getInstance();              
					Intent intent = new Intent(Intent.ACTION_EDIT);
					intent.setType("vnd.android.cursor.item/event");
					intent.putExtra("beginTime", beginTime.getTimeInMillis());
					intent.putExtra("allDay", true);
					intent.putExtra("rrule", "FREQ=YEARLY");
					intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
					intent.putExtra("title", "A Test Event from android app");
					startActivity(intent);*/
					
					/*ContentValues event = new ContentValues();
					event.put("calendar_id", 3);
					event.put("title", "Famnially - RDV");
					event.put("description", commentaire.getText().toString());				
					//event.put("dtstart", beginTime.getTimeInMillis());				
					event.put("allDay", 0);   // 0 for false, 1 for true
					event.put("hasAlarm", 1); // 0 for false, 1 for true
					//Uri eventsUri = Uri.parse("content://calendar/events");
					Uri eventsUri = Uri.parse("content://com.android.calendar/calendars");
					getActivity().getContentResolver().insert(eventsUri, event);
					
					//Doit marcher en 2.3
					/*Intent intent = new Intent(Intent.ACTION_INSERT);
					intent.setData(CalendarContract.Events.CONTENT_URI);
					intent.setType("vnd.android.cursor.item/event");
					intent.putExtra(Events.TITLE, "Learn Android");
					intent.putExtra(Events.EVENT_LOCATION, "Home suit home");
					intent.putExtra(Events.DESCRIPTION, "Download Examples");

					// Setting dates
					GregorianCalendar calDate = new GregorianCalendar(2012, 10, 02);
					intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
					  calDate.getTimeInMillis());
					intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
					  calDate.getTimeInMillis());

					// make it a full day event
					intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

					// make it a recurring Event
					intent.putExtra(Events.RRULE, "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");

					// Making it private and shown as busy
					intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
					intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY); 
					startActivity(intent);*/
					
								
					ContentValues l_event = new ContentValues();
				    l_event.put("calendar_id", 3);
				    l_event.put("title", "Famnially - RDV");
				    l_event.put("description",  commentaire.getText().toString());
				    l_event.put("dtstart", beginTime.getTimeInMillis());
				    l_event.put("dtend", endTime.getTimeInMillis());
				    l_event.put("allDay", 0);
				    l_event.put("hasAlarm", 1);    
				    // status: 0~ tentative; 1~ confirmed; 2~ canceled 
				    // l_event.put("eventStatus", 1);

				    l_event.put("eventTimezone", "France");
				    Uri l_eventUri;  
				    if (Build.VERSION.SDK_INT >= 8) {
				        l_eventUri = Uri.parse("content://com.android.calendar/events");
				    } else {
				        l_eventUri = Uri.parse("content://calendar/events");
				    }
				    Uri l_uri = getActivity().getContentResolver()
				            .insert(l_eventUri, l_event);
				    
				    System.out.println("testestest");
					
				}
				
				
				ContentValues rdvInsert = new ContentValues();
				rdvInsert.put(Table_RDV.NAME_ANIMAL, animalName);
				rdvInsert.put(Table_RDV.DATE, utils.getDateTimeMin(year,month,day,heure,minute,0));
				rdvInsert.put(Table_RDV.COMMENT, commentaire.getText().toString());			
				
								 
				getActivity().getContentResolver().insert(FanimallyContentProvider.CONTENT_URI_RDV, rdvInsert);

				rdvInsert.clear();    
				

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




}
