package it.polito.tdp.emergency.simulation;

public class Dottore {
	private String nome;
	private int id;
	public enum StatoDottore {LIBERO,OCCUPATO,PAUSA};
	private StatoDottore stato;
	
	public Dottore(String nome, int id, StatoDottore stato) {
		super();
		this.nome = nome;
		this.id = id;
		this.stato = stato;
	}

	public StatoDottore getStato() {
		return stato;
	}

	public void setStato(StatoDottore stato) {
		this.stato = stato;
	}

	public String getNome() {
		return nome;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dottore other = (Dottore) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	
	
}
