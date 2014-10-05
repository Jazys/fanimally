/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.jacquemet.fanimally.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.jacquemet.fanimally.model.ModeleDonnees;


import android.os.Environment;

/**
 *
 * @author fabrice
 */
public class FonctionsSEE {
    
    public enum typeOS {nonConnu,windows,linux,android};
    
    public static HashMap<Byte, String> conversionByteString=new HashMap<Byte, String>();

    NumberFormat formatNombre = NumberFormat.getInstance();

    /**
     * renvoi la valeur ordinale du caractere contenu dans c
     * @param c caractere
     * @return la valeur ordinale
     */
    public static byte ord(String c){
        return(c.getBytes()[0]);
    }

    public static byte ord(char c){
        return (byte)c;
    }

    /**
     * renvoi le caractere dont la valeur ordinale est b
     * @param c caractere
     * @return la valeur ordinale
     */
    public static String _char(byte b){
        byte[] val = {b};
        //String.decode(null);
        return(new String(val,0,1));
    }

    /**
     * transforme la date local en date GMT (ATTENTION! au changement ete/hiver la date est indefinie...)
     * @param dLocal date a transformer
     * @return la date en GMT
     */
    public static Date localToGMT(Date dLocal) {
        long utcMiliseconds = dLocal.getTime();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(utcMiliseconds);
        return new Date(utcMiliseconds - cal.get(Calendar.ZONE_OFFSET) - cal.get(Calendar.DST_OFFSET));
    }

    /**
     * transforme la date GMT en date local
     * @param dGMT date a transformer
     * @return la date en local
     */
    public static Date gmtToLocal(java.util.Date dGMT) {
        long utcMiliseconds = dGMT.getTime();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(utcMiliseconds);
        return new java.util.Date(utcMiliseconds + cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET));
    }

    /**
     * Convertit la valeur pass√©e en param√®tre en boolean
     * @param val valeur a convertir
     * @param defaut valeur par defaut
     * @return resultat boolean
     */
    public static boolean stringToBooleanDef(String val, boolean defaut){
        boolean result;
        try{
            val = val.toUpperCase();
            if (val.length()==0) { if (defaut) val = "OUI"; else val = "NON" ; }
            result = val.equals("1") || val.equals("TRUE") || val.equals("OUI");
        }
        catch(Exception e){
            result=defaut;
        }
        return(result);
    }

    /**
     * Convertit le nombre, dont le separateur est ',' (France)
     * @param val representation du nombre
     * @param defaut valeur par defaut
     * @return le nombre
     */
    public static double stringToDoubleDef(String val, double defaut){
        double result;
        try{
            val = val.replace(".", ",");
            result = NumberFormat.getInstance(Locale.FRENCH).parse(val).doubleValue();
        }
        catch(ParseException e){  //NumberFormatException
            result=defaut;
        }
        return(result);
    }

    public static short stringToShortDef(String val, short defaut){
        short result=defaut;
        try{
            result = Short.parseShort(val);
        }
        catch(NumberFormatException e){
        }
        return(result);
    }

    /**
     * Convertit le nombre
     * @param val representation du nombre
     * @param defaut valeur par defaut
     * @return le nombre
     */
    public static int stringToIntDef(String val, int defaut){
        int result;
        try{
            result = Integer.parseInt(val);
        }
        catch(NumberFormatException e){  
            result=defaut;
        }
        return(result);
    }

    public static short stringToHexaDef(String val, short defaut){
        short result=defaut;
        try{
            result = (short)Integer.decode("0x"+val).intValue();
        }
        catch(NumberFormatException e){
        }
        return(result);
    }

    public static int stringToHexaDef(String val, int defaut){
        int result=defaut;
        try{
            result = (int)Integer.decode("0x"+val).intValue();
        }
        catch(NumberFormatException e){
        }
        return(result);
    }

    /**
     * Renvoie la chaine de caract√®res correspondant √† une chaine de caract√®res representant des valeurs Hexa
     * Ex: "DataASCII" = 313233 donc result = 123
     * @param dataASCII
     * @return
     */
    public static String strHexaToString(String dataASCII) {
        int nbChar = dataASCII.length();
        String result = "";

        if ((nbChar%2)!=0) {
            dataASCII = '0'+dataASCII;
            nbChar++;
        }
        for(int i=0; i<nbChar; i+=2) {
            result += (char)Integer.parseInt(dataASCII.substring(i,i+2),16);
        }
        return result;
    }

    /**
     * Convertie la chaine de caract√®res contenue dans "DataHexa" en chaine representant de l'hexa
     * ex: "DataHexa" = 123 donc result = 313233}
     * @param dataHexa
     * @return
     */
    public static String stringToStrHexa(String dataHexa) {
        return stringToStrHexa(dataHexa, '\0');
    }

    /**
     * Convertie la chaine de caract√®res contenue dans "DataHexa" en chaine representant de l'hexa
     * Si separateur!='\0' alors la chaine sera decoup√©e en octet separ√©s par separateur
     * ex: "DataHexa" = 123 et "separateur"='.' donc result = 31.32.33}
     * @param dataHexa
     * @param separateur
     * @return
     */
    public static String stringToStrHexa(String dataHexa, char separateur) {
        String result = "";

        for(int i=0; i<dataHexa.length(); i++) {
            result = result+String.format("%1$02X",(short)dataHexa.charAt(i));
            if(separateur!='\0') result += separateur;
        }
        if(separateur!='\0') result = result.substring(0, result.length()-1);
        return result;
    }

    /**
     * @return le type d'exploitation sur lequel on travail.
     */
    public static typeOS getSystemExploitation(){
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) return(typeOS.windows);
        else return(typeOS.nonConnu);
    }

    /**
     * Met √† jour l'heure systeme.
     * La mise √† jour se fait par commande specifique √† l'OS
     * Implement√© pour windows, Linux(A verifier).
     * @param jourEnCours
     */
    public static boolean setSystemTime(Date horodate) {
        typeOS os = getSystemExploitation();
        try {            
            if (os==typeOS.windows){
                SimpleDateFormat dfJour = new SimpleDateFormat("dd/MM/yyyy");
                Runtime.getRuntime().exec("cmd.exe /k Date "+dfJour.format(horodate));

                SimpleDateFormat dfHeure = new SimpleDateFormat("HH:mm:ss");
                Runtime.getRuntime().exec("cmd.exe /k Time "+dfHeure.format(horodate));
            }
            else if (os==typeOS.linux){ // A VERIFIER
                SimpleDateFormat dfJour = new SimpleDateFormat("MMddHHmmyyyy.ss");
                Runtime.getRuntime().exec("date "+dfJour.format(horodate));
            }
            return (true);
        } catch (IOException ex) {
            return (false);
        }
    }
    
    /**
     * Permet de dÈcouper une chaine avec un dÈlimiteur
     * @param chaine
     * @param delimiteur
     * @return
     */
    public static String[] DecoupeChaineDelimiter(String chaine, String delimiteur)
    {
    	String[] toReturn= new String[CompteNombreData(chaine, delimiteur)+1];
    	
    	//egal au nombre de delimiteur
    	    	
    	toReturn=chaine.split(delimiteur);
    	
    	/*StringTokenizer st=new StringTokenizer(chaine,delimiteur);
    	
    	System.out.println("vidage taille "+FonctionsSEE.stringToHexa(chaine));
    	System.out.println("vidage taille "+st.countTokens());
    	System.out.println("vidage taille "+CompteNombreData(chaine, delimiteur));
    	
    	
    	while (st.hasMoreTokens()) 
    	{
    		 toReturn[i]=st.nextToken();
    		 i++;
    	}*/
    	
    	return toReturn;
    	
    }
    
    /**
     * Compte le nombre d'occurence prÈsent dans la chaÓne
     * @param text
     * @param regex
     * @return
     */
    public static final int CompteNombreData(String text, String regex) 
    {
        Matcher matcher = Pattern.compile(regex).matcher(text);
        int occur = 0;
        while(matcher.find()) {
            occur ++;
        }
        return occur;
    }
    
    public static String stringToHexa(String texte) 
    { 
        //To safe memory - limite gc requests 
        StringBuffer buff = new StringBuffer(texte.length()); 
        for (int i = 0; i < texte.length(); i++) 
        { 
            buff.append(conversionByteString.get((byte) texte.charAt(i))+"."); 
        } 
        return buff.toString(); 
    } 
    
	public static String getHexString(byte[] bytes) 
	{
		StringBuilder sb = new StringBuilder(bytes.length*2);
		for (byte b : bytes) 
		{
			//sb.append( String.format("%x", b)+"." );
			sb.append(conversionByteString.get(b)+".");
		}
		return sb.toString(); 
	}
	
	public static String getHexStringDebug(byte[] bytes) 
	{
		StringBuilder sb = new StringBuilder(bytes.length*2);
		for (byte b : bytes) 
		{
			//sb.append( String.format("%x", b)+"." );
			if(b!=(byte)0xFF)
				sb.append(conversionByteString.get(b)+".");
			else
			{
				sb.append(conversionByteString.get(b)+".");
				break;
			}
				
				
		}
		return sb.toString(); 
	}
	
	/**
	 * Permet de transformer un int en BCD(char)
	 * @param i
	 * @return
	 */
	public static char INT_TO_BCD( int i)
	{
	  return (char) ( (i/16*10) + (i%10) );
	}
	
	public static double calculDistanceEntreDeuxPointsPrecis(double latA, double lonA, double latB, double lonB)
	{
		int r = 6366;
		
		//transformation en radians
		lonA = Math.toRadians(lonA);
		lonB = Math.toRadians(lonB);
		latA = Math.toRadians(latA);
		latB = Math.toRadians(latB);
		
		return		2 *r*	Math.asin(Math.sqrt(Math.pow((Math.sin((latA - latB) / 2)),2)+
	 						Math.cos(latA)*Math.cos(latA)*(Math.pow(Math.sin(((lonA-lonB)/2)),2))));
	 
	}
	
	public static double calculDistanceEntreDeuxPoints(double latA, double lonA, double latB, double lonB)
	{
		double distanceToReturn;
		int r = 6366;
		
		//transformation en radians
		lonA = Math.toRadians(lonA);
		lonB = Math.toRadians(lonB);
		latA = Math.toRadians(latA);
		latB = Math.toRadians(latB);
		 
		//calcul de la distance
		distanceToReturn=Math.acos(Math.sin(latA)*Math.sin(latB)+Math.cos(latA)*Math.cos(latB)*Math.cos(lonA-lonB));
		
		return distanceToReturn*r;
	}
	
	 /**
	  * Permet d'Ècrire dans le fichier contenant les numÈros favoris
	  * @param line
	  * @throws IOException
	  */
	 public static void WriteInFavoris(String line, boolean aLaSuite, String path) throws IOException
	 {
		 File file = new File(Environment.getExternalStorageDirectory()+path);
		  
		 if(!(file.exists()))
			 file.createNewFile();
		 FileWriter filewriter = new FileWriter(file,aLaSuite);
		 filewriter.write(line);
		 filewriter.close();
	 }
	 
	 /**
	  * Permet d'Ècrire dans le journal d'appel
	  */
	 public static void EcrireDansJournalAppel(String type,long date, String numAppel,String path)
	 {
		Scanner scanner;
		String resultatAEcrire="";
		int nombreLigne=1;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy '‡' HH:mm:ss");
		 try 
		 { 
			scanner = new Scanner(new File(Environment.getExternalStorageDirectory()+path));
			
			//
			resultatAEcrire+=type+"= "+dateFormat.format(date)+" ="+numAppel+"\n";
			
			 // On boucle sur chaque champ detectÈ
			 while (scanner.hasNextLine() && nombreLigne<5) 
			 {
				 resultatAEcrire+=scanner.nextLine()+"\n";
				 nombreLigne++;
			 }

			 scanner.close(); 
			 WriteInFavoris(resultatAEcrire, false,path);
			
			
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 }
	 
	 /**
	  * Permet d'Ècrire dans la chaine de LOG
	  * @param chaine
	  */
	 public static void SauvegarderLog(String chaine)
	 {
	
			
	 }
	 
	 /**
	  * Permet de savoir si la carte SD est montÈe
	  * @return
	  */
	 public static boolean isSdPresent() 
	 {
		 return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	 }
	 
	 public static Properties readProperties(String path)
	  { 
		Properties properties=null;;
	    try 
	    {  
	    	properties=new Properties();
	    	FileInputStream fichierProperties = new FileInputStream (path); 
	    	properties.load(fichierProperties);
	        fichierProperties.close();
	  	   
	     } 
	     catch (Exception e)
	     {
			
		 } 
	    
	    return properties;
	  }


}

