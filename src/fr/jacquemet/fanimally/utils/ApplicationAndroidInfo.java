package fr.jacquemet.fanimally.utils;

public class ApplicationAndroidInfo 
{
    public String appname = "";
    public String pname = "";
    public String versionName = "";
    public int versionCode = 0;
    public boolean needToInstall=true;
    public boolean isPresent=false;
    
    public String toString()
    {
    	return ("Nom : "+appname+" Package : "+pname+" VersionName : "+versionName+" VersionCode : "+versionCode+" MAJ requis : "+needToInstall+" Est installe :  "+isPresent);
    }
}
