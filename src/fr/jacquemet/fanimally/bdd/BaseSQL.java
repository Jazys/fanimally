package fr.jacquemet.fanimally.bdd;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class BaseSQL extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "Faminally_t113_.db";


	// la requête permettant de créer la table
	private static final String CREATE_ANIMAL =
			"CREATE TABLE "+ Table_Animal.TABLENAME+" ("+
					Table_Animal.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
					Table_Animal.NAME+" TEXT,"+
					Table_Animal.BIRTHDATE+" datetime,"+
					Table_Animal.SEXE+" boolean,"+
					Table_Animal.SPECIE+" TEXT,"+
					Table_Animal.RACE+" TEXT,"+
					Table_Animal.ROBE+" TEXT,"+
					Table_Animal.PATHIMAGE+" TEXT,"+
					Table_Animal.ASSURANCE+" boolean,"+
					Table_Animal.TATOUAGE+" TEXT); ";

	private static String CREATE_RDV=
			"CREATE TABLE "+ Table_RDV.TABLENAME+" ("+
					Table_RDV.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
					Table_RDV.NAME_ANIMAL+" TEXT,"+
					Table_RDV.DATE+" datetime,"+			
					Table_RDV.COMMENT+" TEXT); ";

	private static String CREATE_COURBE=
			"CREATE TABLE "+ Table_COURBE.TABLENAME+" ("+
					Table_COURBE.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
					Table_COURBE.NAME_ANIMAL+" TEXT,"+
					Table_COURBE.DATE+" datetime,"+		
					Table_COURBE.VALUE+" float,"+	
					Table_COURBE.TYPE+" integer,"+	
					Table_COURBE.COMMENT+" TEXT); ";
	
	private static String CREATE_VETO=
			"CREATE TABLE "+ Table_VETO.TABLENAME+" ("+
					Table_VETO.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
					Table_VETO.NAME_VETO+" TEXT,"+
					Table_VETO.ADRESS+" TEXT,"+		
					Table_VETO.TEL+" TEXT); ";

	/*+  

				"CREATE TABLE Rappel ("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"id_animal integer,"+
				"date datetime,"+
				"type TEXT,"+
				"comment TEXT); "+

				"CREATE TABLE Album ("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"id_animal integer,"+
				"name TEXT,"+
				"path TEXT); "+

				"CREATE TABLE Configuration("+
				"name TEXT PRIMARY KEY,"+
				"value TEXT); "+

				"CREATBE TABLE Veto ("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"name TEXT,"+
				"adress TEXT,"+
				"tel TEXT,"+
				"port  TEXT,"+
				"comment TEXT); "+

				"CREATE TABLE Courbe ("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"type integer,"+
				"date datetime,"+
				"value TEXT); "+

				"CREATE TABLE Intervention ("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"name TEXT,"+
				"id_veto integer,"+
				"id_animal integer"+
				"commentaire TEXT); "+

				"CREATE TABLE Cout ("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"price real,"+
				"id_veto integer,"+
				"id_animal integer"+
				"commentaire TEXT); "+


				"CREATE TABLE Vaccin ("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"name TEXT,"+
				"id_veto integer,"+
				"id_animal integer"+
				"commentaire TEXT); "*/;




				public BaseSQL(Context context, String name, CursorFactory factory, int version) 
				{
					super(context, DATABASE_NAME, factory, 3);

				} 

				@Override  
				public void onCreate(SQLiteDatabase db)
				{ 
					//db.execSQL("DROP TABLE " + Table_Animal.TABLENAME + ";");			
					//création de la table
					db.execSQL(CREATE_ANIMAL);
					db.execSQL(CREATE_RDV);
					db.execSQL(CREATE_COURBE);
					db.execSQL(CREATE_VETO);
				}

				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
					// TODO Auto-generated method stub

				} 



}
