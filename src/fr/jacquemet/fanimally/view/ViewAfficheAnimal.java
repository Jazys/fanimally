package fr.jacquemet.fanimally.view;


import java.io.File;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.jacquemet.fanimally.R;
import fr.jacquemet.fanimally.bdd.FanimallyContentProvider;
import fr.jacquemet.fanimally.bdd.Table_Animal;



@SuppressLint("ValidFragment")
public class ViewAfficheAnimal extends Fragment
{
	private BroadcastReceiver broadcastReceiver;
	private IntentFilter intention;
	private String animalName;

	private ImageView modify;
	private ImageView delete;

	private TextView nameAnimal;
	private TextView dateAnimal;
	private TextView raceAnimal;
	private TextView tatouageAnimal;
	private TextView robeAnimal;
	private TextView sexeAnimal;	
	private TextView assurance;
	private TextView espece;
	private String pathImageAnimal;


	private ImageView photoAnimal;


	public ViewAfficheAnimal(String animalName)
	{
		this.animalName=animalName;
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
		View view = inflater.inflate(R.layout.affiche_animal, null);

		nameAnimal = (TextView) view.findViewById(R.id.afficheAnimalName);
		dateAnimal= (TextView) view.findViewById(R.id.afficheAnimalDateNaissance);
		raceAnimal= (TextView) view.findViewById(R.id.afficheAnimalRace);
		tatouageAnimal= (TextView) view.findViewById(R.id.afficheAnimalTatouage);
		robeAnimal= (TextView) view.findViewById(R.id.afficheAnimalRobe);
		sexeAnimal= (TextView) view.findViewById(R.id.afficheAnimalMale);	
		assurance= (TextView) view.findViewById(R.id.afficheAnimalAssurance);
		espece= (TextView) view.findViewById(R.id.afficheAnimalType);

		photoAnimal=(ImageView) view.findViewById(R.id.afficheanimalphoto);

		delete=(ImageView) 	view.findViewById(R.id.afficheAnimalSupprimer);
		modify=(ImageView) 	view.findViewById(R.id.afficheAnimalModifier);

		fillAnimalFile();

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(getActivity())
				.setTitle(getActivity().getResources().getString(R.string.supprimer_animal))
				.setMessage(getActivity().getResources().getString(R.string.supprimer_animal)+" "+animalName+" ?")
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 

						getActivity().getContentResolver().delete(FanimallyContentProvider.CONTENT_URI_ANIMAL, Table_Animal.NAME +" ="+"'"+animalName+"'", null);

						FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

						Fragment newFragment = new ViewAnimalList();
						ft.replace(R.id.contentFrag, newFragment);
						ft.commit();

					}
				})
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 

					}
				})
				.setIcon(android.R.drawable.ic_dialog_alert)
				.show();


			}
		});



		return view; 
	} 

	private void fillAnimalFile()
	{
		//Recupère l'image et le nom de l'animal
		String columns[] = new String[] {Table_Animal.NAME ,Table_Animal.BIRTHDATE, Table_Animal.RACE, Table_Animal.ROBE
				, Table_Animal.SPECIE, Table_Animal.SEXE, Table_Animal.ASSURANCE, Table_Animal.PATHIMAGE,Table_Animal.TATOUAGE};
		Uri animal = FanimallyContentProvider.CONTENT_URI_ANIMAL;
		Cursor cur = getActivity().getContentResolver().query(animal, columns, Table_Animal.NAME +" ="+"'"+animalName+"'", null, null);
		//Cursor cur = getActivity().getContentResolver().query(animal, columns, null, null, null);

		if(cur !=null) 
		{		
			cur.moveToFirst(); 

			nameAnimal.setText(cur.getString(cur.getColumnIndex(Table_Animal.NAME)) );
			dateAnimal.setText("Née le : "+ cur.getString(cur.getColumnIndex(Table_Animal.BIRTHDATE)) );
			raceAnimal.setText(cur.getString(cur.getColumnIndex(Table_Animal.RACE)) );
			tatouageAnimal.setText(cur.getString(cur.getColumnIndex(Table_Animal.TATOUAGE)) );
			robeAnimal.setText(cur.getString(cur.getColumnIndex(Table_Animal.ROBE)) );
			espece.setText(cur.getString(cur.getColumnIndex(Table_Animal.SPECIE)) ); 

			if( cur.getInt(cur.getColumnIndex(Table_Animal.SEXE))>0)
				sexeAnimal.setText("Male" ); 
			else
				sexeAnimal.setText("Femelle" );

			if( cur.getInt(cur.getColumnIndex(Table_Animal.ASSURANCE))>0)
				assurance.setText("Oui" );
			else
				assurance.setText("non" );

			//La photo 
			pathImageAnimal=cur.getString(cur.getColumnIndex(Table_Animal.PATHIMAGE));			
			File imgFile = new  File(pathImageAnimal);
			if(imgFile.exists()){

				Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				photoAnimal.setImageBitmap(bmp);

			}

			cur.close();



		}
	}






}
