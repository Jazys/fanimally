package fr.jacquemet.fanimally.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;





public class ModeleDonnees 
{
	 private static ModeleDonnees uniqueInstance;
	 public Properties properties;
	 
	 
	 //Pour sauvegarder les infos des fragments
	 public String fAddAnimal;
	 public String  numUrgence;
	
		 
	 		
	 public static synchronized ModeleDonnees getInstance()
	 {
        if(uniqueInstance==null)
        {
                uniqueInstance = new ModeleDonnees();
        }
        return uniqueInstance;
	 }
	 
	 public ModeleDonnees()
	 {
		 
		 properties = new Properties();
		 fAddAnimal="";
		
		
	 }
	 
	 public void clear()
	 {
		 uniqueInstance=new ModeleDonnees();
		
	 }

}
