package it.polito.tdp.emergency.simulation;

public class Assistente extends Dottore{

	public Assistente(String nome, int id, StatoDottore stato) {
		super(nome, id, stato);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "@"+nome+" ("+id+")";
	}
	
}
	
