package fr.jacquemet.fanimally.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.format.DateFormat;

public class utils {
	/**
	 * Permet de découper une chaine avec un délimiteur
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
	 * Compte le nombre d'occurence présent dans la chaîne
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

	public static final String getDateTime(int year, int month, int day) 
	{
		Calendar calendacr = new GregorianCalendar(year,month,day);		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd", Locale.getDefault());		
		
		return dateFormat.format(calendacr.getTime());
	}
	
	public static final String getDateTimeMin(int year, int month, int day, int hour, int minute, int seconde) 
	{
		Calendar calendacr = new GregorianCalendar(year,month,day, hour, minute, 0);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:MM:SS", Locale.getDefault());
	
		return dateFormat.format(calendacr.getTime());
	}
	
	public static final int getMonthsDifference(Date date1, Date date2) {
	    int m1 = date1.getYear() * 12 + date1.getMonth();
	    int m2 = date2.getYear() * 12 + date2.getMonth();
	    return m2 - m1 + 1;
	}

}
