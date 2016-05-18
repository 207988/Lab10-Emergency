package it.polito.tdp.emergency.simulation;

public class Dottore {
	protected String nome;
	protected int id;
	public enum StatoDottore {TURNO,PAUSA};
	protected StatoDottore stato;
	protected Paziente p;
	//se p==null allora il dottore è libero e può ricevere un paziente
	
	public Dottore(String nome, int id, StatoDottore stato) {
		super();
		this.nome = nome;
		this.id = id;
		this.stato = stato;
		p=null;
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

	public Paziente getP() {
		return p;
	}

	public void setP(Paziente p) {
		this.p = p;
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

	@Override
	public String toString() {
		return "@"+nome+" ("+id+")";
	}
	
	
	
	
	
	
	
}
