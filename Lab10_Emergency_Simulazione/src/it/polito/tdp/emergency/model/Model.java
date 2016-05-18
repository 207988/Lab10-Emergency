package it.polito.tdp.emergency.model;

import it.polito.tdp.emergency.db.FieldHospitalDAO;
import it.polito.tdp.emergency.simulation.Core;
import it.polito.tdp.emergency.simulation.Evento;
import it.polito.tdp.emergency.simulation.Paziente;

public class Model {

	Core simulatore= new Core();

	
	public Core getSimulatore() {
		return simulatore;
	}

	
	public void caricaEventi(){
		simulatore.setMediciDisponibili(0);
		FieldHospitalDAO dao=new FieldHospitalDAO();
		dao.popola(simulatore);
		//long tempo=Long.parseLong(turno.substring(0, 2))*60+Long.parseLong(turno.substring(2,4));
		
		simulatore.aggiungiDottore("@Doc 1", 0);
		simulatore.aggiungiDottore("@Doc 2", 0*60);
		simulatore.aggiungiDottore("@Doc 3", 2*60);
		simulatore.aggiungiDottore("@Doc 4", 4*60);
		simulatore.aggiungiDottore("@Doc 5", 6*60);
		
		simulatore.aggiungiAssistente("@Ass 1", 0);
		simulatore.aggiungiAssistente("@Ass 2", 8*60);
		simulatore.aggiungiAssistente("@Ass 3", 16*60);
		
	}
	public void stub() {		

		
		
		
		/*
		simulatore.aggiungiPaziente(new Paziente(1, Paziente.StatoPaziente.ROSSO));
		simulatore.aggiungiPaziente(new Paziente(2, Paziente.StatoPaziente.ROSSO));
		simulatore.aggiungiPaziente(new Paziente(3, Paziente.StatoPaziente.ROSSO));
		simulatore.aggiungiPaziente(new Paziente(4, Paziente.StatoPaziente.ROSSO));

		simulatore.aggiungiEvento(new Evento(10, Evento.TipoEvento.PAZIENTE_ARRIVA, 1));
		simulatore.aggiungiEvento(new Evento(11, Evento.TipoEvento.PAZIENTE_ARRIVA, 2));
		simulatore.aggiungiEvento(new Evento(12, Evento.TipoEvento.PAZIENTE_ARRIVA, 3));
		simulatore.aggiungiEvento(new Evento(13, Evento.TipoEvento.PAZIENTE_ARRIVA, 4));
		*/
		simulatore.simula();

		System.out.println("Persi:" + simulatore.getPazientiPersi());
		System.out.println("Salvati:" + simulatore.getPazientiSalvati());
	}

}
