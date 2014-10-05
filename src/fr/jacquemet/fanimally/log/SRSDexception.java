package fr.jacquemet.fanimally.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SRSDexception implements Thread.UncaughtExceptionHandler {

private Thread.UncaughtExceptionHandler defaultUEH;
private final Logger log = LoggerFactory.getLogger(SRSDexception.class);

	public SRSDexception() 
	{
		ConfigureLogging.getInstance();
		this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
	  
	
	}

	public void uncaughtException(Thread t, Throwable e) 
	{   
	
		StackTraceElement[] arr = e.getStackTrace();
		String Raghav =t.toString();
		String report = e.toString()+"\n\n";
		report += "--------- Stack trace ---------\n\n"+Raghav;
		
		for (int i=0; i<arr.length; i++)
		{
			report += "    "+arr[i].toString()+"\n";
		}
		report += "-------------------------------\n\n";
	
		// If the exception was thrown in a background thread inside
		// AsyncTask, then the actual exception can be found with getCause
		report += "--------- Cause ---------\n\n";
		Throwable cause = e.getCause();
		if(cause != null) {
		report += cause.toString() + "\n\n";
		arr = cause.getStackTrace();
		
		for (int i=0; i<arr.length; i++)
		{
		report += "    "+arr[i].toString()+"\n";
		}
		}
		report += "-------------------------------\n\n";
		
			
		//Si la SDCARD est monté
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) 
			log.error(report);
		
		//pour envoyer un mail
		/*try
		{
			GMailSender m=new GMailSender("seerp26@gmail.com","seerp26210");
			String[] toArr = {"seerp26@gmail.com"}; 
			m.setTo(toArr);
			m.setFrom("ANDREAS");   
			m.setSubject("Log ANDREAS "+ ModeleDonnees.getInstance().properties.getProperty("ID_CHAUFFEUR_GPRS", "")); 
			m.setBody(report); 
			m.send();
		}catch( Exception e1)
		{
			
		}*/
		
		
			
		//defaultUEH.uncaughtException(t, e);
	}

}


