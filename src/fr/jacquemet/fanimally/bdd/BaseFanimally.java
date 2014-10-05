package fr.jacquemet.fanimally.bdd;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import fr.jacquemet.fanimally.log.ConfigureLogging;
import fr.jacquemet.fanimally.model.Animal;
import fr.jacquemet.fanimally.model.ModeleDonnees;


public class BaseFanimally {

  	//The Android's default system path of your application database.
    private static final String DB_PATH = Environment.getExternalStorageDirectory()+"/Fanimaly/bdd/";

    private static String DB_NAME = "fanimally1.db";

    private SQLiteDatabase myDataBase; 

    private final Context myContext;
        
	private BaseSQL baseMeseam;
	
	private final Logger log = LoggerFactory.getLogger(BaseFanimally.class);


    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     * @return 
     */
    public BaseFanimally(Context context) 
    {
    	 
    	baseMeseam = new BaseSQL(context, DB_PATH+DB_NAME, null, 1);
        this.myContext = context;
        ConfigureLogging.getInstance();
		// TODO Auto-generated constructor stub
	}

     
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase(){

    	File dbFile = new File(DB_PATH + DB_NAME);
    	return dbFile.exists();
    }

    
    public synchronized void openDataBase() throws SQLException
    {
    	myDataBase = baseMeseam.getWritableDatabase();
    }     

    /**
	 * Fermeture de la base de données
	 */
	public synchronized void close(){
		//on ferme l'accès à la BDD
		myDataBase.close();
		//copyDataBase(myContext);
	
	}
	
	/**
	 * Retourne la base de données
	 * @return
	 */
	public synchronized SQLiteDatabase getBDD(){
		return myDataBase;
	}


	
	public synchronized boolean setAnimal(String name, String dateNaissance, boolean sexe, String specie, String race, String robe, boolean assurance, long tatouage)
	{
		ContentValues args = new ContentValues();
	    args.put(Table_Animal.NAME, name);
	    args.put(Table_Animal.BIRTHDATE, dateNaissance);
	    args.put(Table_Animal.SEXE, sexe);
	    args.put(Table_Animal.SPECIE, specie);
	    args.put(Table_Animal.RACE, race);
	    args.put(Table_Animal.ROBE, robe);
	    args.put(Table_Animal.ASSURANCE, robe);
	    args.put(Table_Animal.TATOUAGE, tatouage); 
	    
	    if(myDataBase!=null)
	    	myDataBase.update("Animal", args, "", null);
	    
	    return true;		
	}
	
	/*public synchronized ArrayList<Animal> getAnimal()
	{
		String requete="";
		
		ArrayList<Animal> animalList= new ArrayList<Animal>();
		
		
		requete="Select * FROM Animal ;";
	
		if(myDataBase!=null)
			myDataBase.execSQL(requete);
		
		Cursor cur=myDataBase.rawQuery(requete, null);
		if(cur.moveToNext())
		{
			Animal newAnimal;
			newAnimal=new Animal(cur.getInt(0), cur.getString(1), cur.getInt(2),  cur.getString(3),  cur.getString(4),  cur.getString(5), cur.getString(6), cur.getLong(7), cur.getShort(8));
			
			requete="Select * FROM Animal WHERE";
			Cursor cur=myDataBase.rawQuery(requete, null);
			if(cur.moveToNext())
			{
				animalList.add(new )
				nombreDataRestantes=cur.getInt(0);
			}
			
			animalList.add(new )
			nombreDataRestantes=cur.getInt(0);
		}
		cur.close();
		
		requete=null;
		
		return nombreDataRestantes;
	    
	    return true;		
	}*/
	
	public synchronized boolean setRappel(String name, String dateNaissance, boolean sexe, String specie, String race, String robe, long tatouage)
	{
		ContentValues args = new ContentValues();
	    args.put("name", name);
	    args.put("bird_date", dateNaissance);
	    args.put("sexe", sexe);
	    args.put("specie", specie);
	    args.put("race", race);
	    args.put("robe", robe);
	    args.put("tatouage", tatouage);
	    
	    if(myDataBase!=null)
	    	myDataBase.update("Animal", args, "", null);
	    
	    return true;		
	}
	
	
	
	/**
	 * Supprime la ligne correspondant à l'ID passé en paramètre
	 * @param id
	 * @return
	 */
	public synchronized boolean deleteEntry(int id)
	{
		String requete="";
		
		requete="DELETE FROM mesaem WHERE id=";
		requete+=id+" ;";
	
		if(myDataBase!=null)
			myDataBase.execSQL(requete);
		
		requete=null;
		
		return true;
	}
	
	/**
	 * Compte le nombre de ligne dans la base de données
	 * @param 
	 * @return le nombre de ligne
	 */
	public synchronized int dataRestantes()
	{
		String requete="";
		int nombreDataRestantes=0;
		
		requete="Select count(*) FROM MESAEM";
	
		if(myDataBase!=null)
			myDataBase.execSQL(requete);
		
		Cursor cur=myDataBase.rawQuery(requete, null);
		if(cur.moveToNext())
		{
			nombreDataRestantes=cur.getInt(0);
		}
		cur.close();
		
		requete=null;
		
		return nombreDataRestantes;
	}
	
	/**
	 * Supprime la ligne correspondant à l'ID passé en paramètre
	 * @param id
	 * @return
	 */
	public synchronized boolean DeleteOldEntry()
	{
		boolean aFini=false;
		String requeteSelect="";
		String requeteDelete="";
		Calendar dateActuelle=new GregorianCalendar();
		dateActuelle.setTimeInMillis(System.currentTimeMillis());
		
		
		Calendar retourRequete=new GregorianCalendar();
		
		//parcours les entrées les unes après les autres
		//et recupère la date de l'entrée séléctionnée
		//Si la donnée date de plus de 7 jours alors on la supprime
		while(aFini==false)
		{
			requeteSelect="SELECT * FROM mesaem ORDER BY id ASC LIMIT 1";
		
			Cursor cur=myDataBase.rawQuery(requeteSelect, null);
			
			//Si j'ai un résultat alors je recupère la date
			if(cur.moveToNext())
			{
				//sauv l'id de la requete et la date
				int id_ligne=cur.getInt(0);
				retourRequete.setTimeInMillis(Long.parseLong(cur.getString(5)));
				
				//if(ModeleDonnees.getInstance().properties.getProperty("LEVEL_LOG","").equalsIgnoreCase("DEBUG")) 
					log.debug("delete old entry date actuelle:dateLigne:id "+dateActuelle.get(Calendar.DAY_OF_YEAR)+":"+retourRequete.get(Calendar.DAY_OF_YEAR)+":"+id_ligne);
				//si il y a plus de 7 jours de différence
				//Mettre une constante
				if(dateActuelle.get(Calendar.DAY_OF_YEAR)-retourRequete.get(Calendar.DAY_OF_YEAR)>10)
				{
										
					requeteDelete="DELETE FROM mesaem WHERE id=";
					requeteDelete+=id_ligne+" ;";
				
					myDataBase.execSQL(requeteDelete);
					
					requeteDelete=null;
					
				//	if(ModeleDonnees.getInstance().properties.getProperty("LEVEL_LOG","").equalsIgnoreCase("DEBUG")) 
						log.debug("date Delete entry "+id_ligne);
				}
				//Si c'est non les données suivantes seront encore plus récentes
				//donc pas besoin de faire le traiteme,t
				else
					aFini=true;
					
			} 
			//Si la base de donnée est vide
			else
			{
				aFini=true;
			}
			
			cur.close();
		}
		
		dateActuelle=null;
		requeteDelete=null;
		requeteSelect=null;
		retourRequete=null;

		
		return true;
	}
	 
}