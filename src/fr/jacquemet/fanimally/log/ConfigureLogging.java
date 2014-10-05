package fr.jacquemet.fanimally.log;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogConfigurator;
import fr.jacquemet.fanimally.model.ModeleDonnees;


/**
 * Classe permettant de gérer les logs
 * @author JulienJ
 *
 */
public class ConfigureLogging {
	
	private static ConfigureLogging uniqueInstance;
	private static LogConfigurator logConfigurator;
	private static Logger log=LoggerFactory.getLogger(ConfigureLogging.class);
	
	/**
	 * Configure le logger
	 */
	private ConfigureLogging()
	{
		logConfigurator = new LogConfigurator();
                   
        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + "/ANDREAS/log/ANDREAS.log");
        logConfigurator.setRootLevel(Level.INFO);  
        // Set log level of a specific logger 
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.setMaxBackupSize(Integer.parseInt(ModeleDonnees.getInstance().properties.getProperty("NB_LOG", "1")));
        logConfigurator.setMaxFileSize(Integer.parseInt(ModeleDonnees.getInstance().properties.getProperty("TAILLE_LOG", "1000"))*1000); 
        logConfigurator.configure();     
        
    }
    
	   
	
   
    // Méthode statique qui sert de pseudo-constructeur (utilisation du mot clef "synchronized" pour le multithread).
    public static synchronized ConfigureLogging getInstance()
    {
            if(uniqueInstance==null)
            {
                    uniqueInstance = new ConfigureLogging();
            }
            return uniqueInstance;   
    } 
    
    public void removeWriteLog()
    {
    	uniqueInstance=null;  
    	logConfigurator=null; 
    } 
   
}