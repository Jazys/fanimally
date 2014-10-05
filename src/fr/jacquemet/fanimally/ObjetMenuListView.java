package fr.jacquemet.fanimally;

/**
 * Classe permettant de remplir un objet Menu à partir d'une source de donnée
 * XML pour le moment
 * @author JulienJ
 *
 */
public class ObjetMenuListView 
{
	private String libelle;
	private String vue;
	private int numero;
	private String icone;
	
	public ObjetMenuListView(String libelle, String vue, int numero, String icone)
	{
		this.libelle=libelle;
		this.vue=vue;
		this.numero=numero;
		this.icone=icone; 
	}
	
	public String getLibelle()
	{
		return libelle;
	}
	
	public String getVue()
	{
		return vue;
	}
	
	public int getPosition()
	{
		return numero;
	}
	
	public String getIcone()
	{
		return icone;
	}

}
