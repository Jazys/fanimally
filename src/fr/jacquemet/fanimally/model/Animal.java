package fr.jacquemet.fanimally.model;

import java.util.Date;

public class Animal {
	
	private int id;
	private String name;
	private boolean sexe;
	private String specie;
	private String race;
	private String robe;
	private Date birdDate;
	private long puceId;
	private String tatouage;
	private String urlPhoto;

	
	public Animal(int id, String name, boolean sexe, String specie, String race, String robe, Date birdDate, long puceId, String tatouage)
	{
		this.id=id;
		this.name=name;
		this.sexe=sexe;
		this.specie=specie;
		this.race= race;
		this.robe= robe;
		this.birdDate= birdDate;
		this.puceId=puceId;
		this.tatouage=tatouage;
		
	}
	
	public void setPhotoPath(String url)
	{
		this.urlPhoto=url;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public boolean getSexe()
	{
		return this.sexe;
	}
	
	public long getPuceId()
	{
		return this.puceId;
	}
	
	public Date getBirthDate()
	{
		return birdDate;
	}
	
	public String getSpecie()
	{
		return specie;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getRobe()
	{
		return robe;
	}
	
	public String getRace()
	{
		return race;
	}
	
	public String getTatouage()
	{
		return tatouage;
	}
	
	public String getUrlPhoto()
	{
		return urlPhoto;
	}
	
	

}
