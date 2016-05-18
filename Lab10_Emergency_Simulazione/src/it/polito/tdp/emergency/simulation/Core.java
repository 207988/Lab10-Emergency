//////////////////////////////////////////////////////////////////-*-java-*-//
//             // Classroom code for "Tecniche di Programmazione"           //
//   #####     // (!) Giovanni Squillero <giovanni.squillero@polito.it>     //
//  ######     //                                                           //
//  ###   \    // Copying and distribution of this file, with or without    //
//   ##G  c\   // modification, are permitted in any medium without royalty //
//   #     _\  // provided this notice is preserved.                        //
//   |   _/    // This file is offered as-is, without any warranty.         //
//   |  _/     //                                                           //
//             // See: http://bit.ly/tecn-progr                             //
//////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.simulation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

import it.polito.tdp.emergency.simulation.Dottore.StatoDottore;
import it.polito.tdp.emergency.simulation.Evento.TipoEvento;
import it.polito.tdp.emergency.simulation.Paziente.StatoPaziente;

public class Core {
	
	
	Queue<Evento> listaEventi = new PriorityQueue<Evento>();
	Map<Integer, Paziente> pazienti = new HashMap<Integer, Paziente>();	
	Queue<Paziente> pazientiInAttesa = new PriorityQueue<Paziente>();
	Map<Integer,Dottore>dottori=new HashMap<Integer,Dottore>();
	Map<Integer,Assistente>assistenti=new TreeMap<Integer,Assistente>();	
	//Map<Integer,Dottore>dottoriPausa=new HashMap<Integer,Dottore>();
	int mediciDisponibili = 0;
	int idDottore=1;
	int idAssistente=-1;
	int pazientiSalvati = 0;
	int pazientiPersi = 0;
	
	public int getPazientiSalvati() {
		return pazientiSalvati;
	}

	public int getPazientiPersi() {
		return pazientiPersi;
	}
	

	public int getMediciDisponibili() {
		return mediciDisponibili;
	}

	public void setMediciDisponibili(int mediciDisponibili) {
		this.mediciDisponibili = mediciDisponibili;
	}
	
	public void aggiungiDottore(String nome,long temp){
		dottori.put(idDottore, new Dottore(nome,idDottore,StatoDottore.PAUSA));		
		listaEventi.add(new Evento(temp,TipoEvento.DOTTORE_INIZIA_TURNO,idDottore));
		idDottore++;
		System.out.println(temp);
		
	}
	
	public void aggiungiAssistente(String nome,long temp){
		assistenti.put(idAssistente, new Assistente(nome,idAssistente,StatoDottore.PAUSA));		
		listaEventi.add(new Evento(temp,TipoEvento.DOTTORE_INIZIA_TURNO,idAssistente--));		
		
	}
	

	public void aggiungiEvento(Evento e) {
		listaEventi.add(e);
	}

	public void aggiungiPaziente(Paziente p) {
		pazienti.put(p.getId(), p);
	}

	public void passo() {
		Evento e = listaEventi.remove();
		
		switch (e.getTipo()) {
			case PAZIENTE_ARRIVA:
			//System.out.println("Arrivo paziente:" + e);
			pazientiInAttesa.add(pazienti.get(e.getDato()));
			switch (pazienti.get(e.getDato()).getStato()) {
			case BIANCO:
				break;
			case GIALLO:
				this.aggiungiEvento(new Evento(e.getTempo() + 6 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			case ROSSO:
				this.aggiungiEvento(new Evento(e.getTempo() + 1 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			case VERDE:
				this.aggiungiEvento(new Evento(e.getTempo() + 12 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			default:
				System.err.println("Panik!");
			}
			break;
		case PAZIENTE_GUARISCE:
			if (pazienti.get(e.getDato()).getStato() != Paziente.StatoPaziente.NERO) {
				//System.out.println("Paziente salvato: " + e);
				pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.SALVO);
				++mediciDisponibili;
				++pazientiSalvati;
				//libero il dottore
				this.liberaDottore(e.getDato());				
			}
			break;
		case PAZIENTE_MUORE:
			if (pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.SALVO) {
				//System.out.println("Paziente giï¿½ salvato: " + e);
			} else {
				if (pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.IN_CURA) {					
					++mediciDisponibili;
					//libero il dottore
					this.liberaDottore(e.getDato());					
				}
				++pazientiPersi;
				pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.NERO);
				//System.out.println("Paziente morto: " + e);	
				
			}			
			break;
		case DOTTORE_INIZIA_TURNO:
			Dottore d=dottori.get(e.getDato());
			//caso dottore
			if(d!=null){
				mediciDisponibili++;
				listaEventi.add(new Evento(e.getTempo()+480,TipoEvento.DOTTORE_FINE_TURNO,e.getDato()));
				//Prendo il dottore,cambio lo stato e azzero il paziente
				
				d.setP(null);
				d.setStato(StatoDottore.TURNO);
				//System.out.println("#############"+d.toString().toUpperCase()+" INIZIA TURNO"+"#############"+e.getTempo());
			}
			//caso assistente
			else{
				Assistente a=assistenti.get(e.getDato());
				listaEventi.add(new Evento(e.getTempo()+480,TipoEvento.DOTTORE_FINE_TURNO,e.getDato()));
				a.setP(null);
				a.setStato(StatoDottore.TURNO);
				System.out.println("#############"+a.toString().toUpperCase()+" INIZIA TURNO"+"#############"+e.getTempo());
				for(Assistente a1:assistenti.values()){
					if(!a.equals(a1))
						a1.setStato(StatoDottore.PAUSA);
				}
			}
			
			/*Dottore d=dottoriPausa.get(e.getDato());
			d.setP(null);
			d.setStato(StatoDottore.TURNO);
			dottoriPausa.remove(d.getId());
			dottori.put(d.getId(), d);*/
			//dottoriPausa.get(e.getDato()).setStato(StatoDottore.TURNO);
			//dottori.get(e.getDato()).setP(null);
			break;
		case DOTTORE_FINE_TURNO:
			Dottore d1=dottori.get(e.getDato());
			//caso dottore
			if(d1!=null){
				mediciDisponibili--;
				listaEventi.add(new Evento(e.getTempo()+960,TipoEvento.DOTTORE_INIZIA_TURNO,e.getDato()));			
				d1.setP(null);
				d1.setStato(StatoDottore.PAUSA);
				//System.out.println("#############"+d1.toString().toUpperCase()+" FINE TURNO"+"#############"+e.getTempo());
			}
			//caso assistente
			else{
				Assistente a=assistenti.get(e.getDato());
				listaEventi.add(new Evento(e.getTempo()+960,TipoEvento.DOTTORE_INIZIA_TURNO,e.getDato()));
				a.setP(null);
				a.setStato(StatoDottore.PAUSA);
				System.out.println("#############"+a.toString().toUpperCase()+" FINE TURNO"+"#############"+e.getTempo());
				
			}
			
			
			/*Dottore d1=dottori.get(e.getDato());
			d1.setP(null);
			d1.setStato(StatoDottore.PAUSA);
			dottori.remove(d1.getId());
			dottoriPausa.put(d1.getId(), d1);*/
			//dottori.get(e.getDato()).setStato(StatoDottore.PAUSA);
			//dottori.get(e.getDato()).setP(null);
			break;
		default:
			System.err.println("Panik!");
		}

		while (cura(e.getTempo()))
			;
	}

	protected boolean cura(long adesso) {
		if (pazientiInAttesa.isEmpty())
			return false;
		//if (mediciDisponibili == 0)
		if(this.numeroMediciDisponibili()==0)
			return false;

		Paziente p = pazientiInAttesa.remove();
		if (p.getStato() != Paziente.StatoPaziente.NERO) {
			//provo a cercare un assistente se il paziente non è ROSSO
			if(p.getStato().ordinal()!=StatoPaziente.ROSSO.ordinal()){
				for(Assistente a:assistenti.values()){
					if(a.getStato().ordinal()==StatoDottore.TURNO.ordinal())
						if(a.getP()==null){
						a.setP(p);
						aggiungiEvento(new Evento(adesso + 30, Evento.TipoEvento.PAZIENTE_GUARISCE, p.getId()));
						//System.out.println(a + " inizia a curare: " + p);
						return true;
					}
				}
			}
			//Cerco il dottore libero			
			for(Dottore d:dottori.values()){
				//condizione dottore libero
				if(d.getStato().ordinal()==StatoDottore.TURNO.ordinal())
					if(d.getP()==null){
						--mediciDisponibili;
						d.setP(p);
						pazienti.get(p.getId()).setStato(Paziente.StatoPaziente.IN_CURA);
						aggiungiEvento(new Evento(adesso + 30, Evento.TipoEvento.PAZIENTE_GUARISCE, p.getId()));
						//System.out.println(d+" inizia a curare: " + p);
						return true;
					}
			}
		}		
		return true;		
			
	}

	public void simula() {
		while (((!listaEventi.isEmpty())&&((pazientiSalvati+pazientiPersi)<2000))) {
			passo();
		}
	}

	private int numeroMediciDisponibili(){
		int res=0;
		for(Dottore d:dottori.values()){
			if(d.getStato().ordinal()==StatoDottore.TURNO.ordinal())
				if(d.getP()==null)
					res++;
		}
		for(Assistente a:assistenti.values()){
			if(a.getStato().ordinal()==StatoDottore.TURNO.ordinal())
				if(a.getP()==null)
					res++;
				
		}
		return res;
	}
	
	private void liberaDottore(int id){
		//caso dottore
		if(id>0){
			for(Dottore d:dottori.values()){
				if(d.getP()!=null)
					if(d.getP().equals(pazienti.get(id)))
							d.setP(null);				
			}
		}
		//caso assistente
		else if(id<0){
			for(Assistente a:assistenti.values()){
				if(a.getP()!=null)
					if(a.getP().equals(pazienti.get(id)))
							a.setP(null);				
			}
		}
	}
}
