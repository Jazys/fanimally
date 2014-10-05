package fr.jacquemet.fanimally.utils;

import java.io.Serializable;

public class VetoName implements Serializable {
	private String name = "";
	private String tel="";


	public VetoName(String name, String tel) {
		this.name=name;
		this.tel=tel;
		
	}
	
	public String getName() {
		return this.name;
		
	}
	
	public String getTel() {
		return this.tel;
		
	}

}