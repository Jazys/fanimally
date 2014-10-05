package fr.jacquemet.fanimally.view;


import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import fr.jacquemet.fanimally.R;
import fr.jacquemet.fanimally.bdd.FanimallyContentProvider;
import fr.jacquemet.fanimally.bdd.Table_Animal;
import fr.jacquemet.fanimally.model.ModeleDonnees;
import fr.jacquemet.fanimally.utils.utils;



public class ViewAddAnimal extends Fragment
{
	private BroadcastReceiver broadcastReceiver;
	private IntentFilter intention;

	private Button btnPhoto;
	private Button save;

	private EditText nameAnimal;
	private EditText dateAnimal;
	private EditText raceAnimal;
	private EditText tatouageAnimal;
	private EditText robeAnimal;
	
	private CheckBox male_cb;
	private CheckBox female_cb;
	private CheckBox assurance;
	

	private ImageButton calendar;
	private Spinner spinnerType;
	
	private ImageView photoAnimal;
	private LinearLayout linearPhotoAnimal;

	private int year=0;
	private int day=0;
	private int month=0;
	
	private String pathPhoto="";
	
	private int positionType=0;

	private ArrayAdapter<CharSequence> adapter;
	private String typeStr;




	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);	

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
		//getActivity().registerReceiver(broadcastReceiver, intention);	


	} 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_animal, null);
		

		nameAnimal= (EditText) view.findViewById(R.id.editTextUsageName);
		nameAnimal.setText("");

		dateAnimal= (EditText) view.findViewById(R.id.editTextBirthDate);
		dateAnimal.setText("");
		
		raceAnimal= (EditText) view.findViewById(R.id.editTextRace);
		raceAnimal.setText(""); 
		
		tatouageAnimal= (EditText) view.findViewById(R.id.editTextTatouage);
		tatouageAnimal.setText(""); 
		
		robeAnimal= (EditText) view.findViewById(R.id.editTextRobe);
		robeAnimal.setText(""); 
		
		female_cb=(CheckBox) view.findViewById(R.id.checkBoxFemale);		
		male_cb=(CheckBox) view.findViewById(R.id.checkBoxMale);
		assurance=(CheckBox) view.findViewById(R.id.checkBoxAssurance);
		
	
		calendar= (ImageButton) view.findViewById(R.id.imageButtonCalencar);

		spinnerType = (Spinner) view.findViewById(R.id.spinnerType);

		adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.type_animal, android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		linearPhotoAnimal=(LinearLayout) view.findViewById(R.id.linearPhotoAnimal);
		
		photoAnimal= (ImageView) view.findViewById(R.id.imageViewPhotoAnimal);
		

		spinnerType.setAdapter(adapter);

		spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
			{
				typeStr= parent.getItemAtPosition(pos).toString();
				positionType=pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}  

		});
		
		typeStr=spinnerType.getItemAtPosition(0).toString();

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

							dateAnimal.setText(dayOfMonth + "/"
									+ (monthOfYear + 1) + "/" + year);
						}
						else
						{

							dateAnimal.setText(dayOfMonth + "/0"
									+ (monthOfYear + 1) + "/" + year);
						}
							
						System.out.println(year);

					}
				}, year, month, day);
				dpd.show();

			}
		});

		btnPhoto= (Button) view.findViewById(R.id.btn_add_take_photo);
		btnPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
							
				ModeleDonnees.getInstance().fAddAnimal=nameAnimal.getText().toString()+";"+dateAnimal.getText().toString()+";"+raceAnimal.getText().toString()+
						";"+robeAnimal.getText().toString()+";"+tatouageAnimal.getText().toString()+";"+positionType+";"+male_cb.isChecked();

				/*FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

				Fragment newFragment = new ViewPhoto();
				ft.replace(R.id.contentFrag, newFragment);
				ft.commit();*/				

				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
				intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 		
				getActivity().startActivityForResult( intent, 0 );
				  
			
  
			}
		});
		
		save=(Button) view.findViewById(R.id.btn_save_animal);
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
				if(nameAnimal.getText().toString()!="" && dateAnimal.getText().toString()!="" && raceAnimal.getText().toString()!="" 
						&& robeAnimal.getText().toString()!="" && tatouageAnimal.getText().toString()!="" && pathPhoto!="")
				{
										
					ContentValues animalInsert = new ContentValues();
					animalInsert.put(Table_Animal.NAME, nameAnimal.getText().toString());
					animalInsert.put(Table_Animal.BIRTHDATE, utils.getDateTime(year,month,day));
					animalInsert.put(Table_Animal.SEXE, male_cb.isChecked());
					animalInsert.put(Table_Animal.SPECIE, typeStr);
					animalInsert.put(Table_Animal.RACE, raceAnimal.getText().toString());
					animalInsert.put(Table_Animal.ROBE, robeAnimal.getText().toString());
					animalInsert.put(Table_Animal.PATHIMAGE, pathPhoto);
					animalInsert.put(Table_Animal.ASSURANCE, assurance.isChecked());
					animalInsert.put(Table_Animal.TATOUAGE, tatouageAnimal.getText().toString());
					
									 
					getActivity().getContentResolver().insert(FanimallyContentProvider.CONTENT_URI_ANIMAL, animalInsert);

					animalInsert.clear();    
					
					FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

					Fragment newFragment = new ViewAnimalList();
					ft.replace(R.id.contentFrag, newFragment);
					ft.commit();
				} 
				else
				{
					Toast.makeText(getActivity(), "Remplir tous les champs", 1000).show();
				}
				
			}
		});
		
		
		//Dans le cas où le fragment doit être rechargé avec ses infos
		if(ModeleDonnees.getInstance().fAddAnimal!="")
		{
			String[] data =utils.DecoupeChaineDelimiter(ModeleDonnees.getInstance().fAddAnimal, ";");
			
			nameAnimal.setText(data[0]);
			dateAnimal.setText(data[1]);
			raceAnimal.setText(data[2]);
			robeAnimal.setText(data[3]);
			tatouageAnimal.setText(data[4]);
			
			spinnerType.setSelection(Integer.parseInt(data[5]));
			
			if(Boolean.parseBoolean(data[6]))
				male_cb.setChecked(true);
			else
				female_cb.setChecked(true);
			
			
		}


		return view; 
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		System.out.println("request code "+ requestCode); 
		if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                        	
            	// Find the last picture
            	String[] projection = new String[]{
            	    MediaStore.Images.ImageColumns._ID,
            	    MediaStore.Images.ImageColumns.DATA,
            	    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            	    MediaStore.Images.ImageColumns.DATE_TAKEN,
            	    MediaStore.Images.ImageColumns.MIME_TYPE
            	    };
            	final Cursor cursor = getActivity().getContentResolver()
            	        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, 
            	               null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

            	// Put it in the image view
            	if (cursor.moveToFirst()) {
            	    
            		linearPhotoAnimal.setVisibility(View.VISIBLE);
            		linearPhotoAnimal.setGravity(Gravity.CENTER);
            		pathPhoto = cursor.getString(1); 
            	    
            		
            		//permet de faire en sorte que l'image soit une icône
            	    BitmapFactory.Options options = new BitmapFactory.Options();
            	    options.inSampleSize = 2;
            	    Bitmap bm = BitmapFactory.decodeFile(pathPhoto, options); 
            	    bm=Bitmap.createScaledBitmap(bm, 150, 150, false);
            	    photoAnimal.setImageBitmap(Bitmap.createScaledBitmap(bm, 150, 150, false));    

            	    FileOutputStream out = null;
            	    try {
            	           out = new FileOutputStream(pathPhoto);
            	           bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            	    } catch (Exception e) {
            	        e.printStackTrace();
            	    } finally {
            	           try{
            	               out.close();
            	           } catch(Throwable ignore) {}
            	    }
            	   
            	} 

            }
        }  
	}

	
	
	


}
