////////////////////////////////////////////////////////////////////////////////
//             //                                                             //
//   #####     // Field hospital simulator                                    //
//  ######     // (!) 2013 Giovanni Squillero <giovanni.squillero@polito.it>  //
//  ###   \    //                                                             //
//   ##G  c\   // Field Hospital DAO                                          //
//   #     _\  // Test with MariaDB 10 on win                                 //
//   |   _/    //                                                             //
//   |  _/     //                                                             //
//             // 03FYZ - Tecniche di programmazione 2012-13                  //
////////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import it.polito.tdp.emergency.simulation.Core;
import it.polito.tdp.emergency.simulation.Evento;
import it.polito.tdp.emergency.simulation.Evento.TipoEvento;
import it.polito.tdp.emergency.simulation.Paziente;
import it.polito.tdp.emergency.simulation.Paziente.StatoPaziente;

public class FieldHospitalDAO {

	public void popola(Core sim){				
		Connection conn=DBConnect.getInstance().getConnection();
		try {
			
			String sql= "SELECT *  FROM arrivals ORDER BY timestamp;";			
			
			PreparedStatement st = conn.prepareStatement(sql);
			
			
			ResultSet res=st.executeQuery();
			long mezzanotte=0;
			while(res.next()){
				//aggiungi paziente ad elenco
				String tipo=res.getString("triage");
				Paziente p=null;
				if(tipo.compareToIgnoreCase("yellow")==0)
					p=new Paziente(res.getInt("patient"),StatoPaziente.GIALLO);
				else if(tipo.compareToIgnoreCase("green")==0)
					p=new Paziente(res.getInt("patient"),StatoPaziente.VERDE);
				else if(tipo.compareToIgnoreCase("red")==0)
					p=new Paziente(res.getInt("patient"),StatoPaziente.ROSSO);
				else if(tipo.compareToIgnoreCase("white")==0)
					p=new Paziente(res.getInt("patient"),StatoPaziente.BIANCO);
				else
					throw new RuntimeException("TRIAGE NON RICONOSCIUTO");
				sim.aggiungiPaziente(p);
				
				//aggiungi evento
				if(mezzanotte==0)
					mezzanotte=res.getTimestamp("timestamp").getTime()-143000;
				long tempo=(res.getTimestamp("timestamp").getTime()-mezzanotte)/(1000*60);
				Evento e=new Evento(tempo,TipoEvento.PAZIENTE_ARRIVA,res.getInt("patient"));
				sim.aggiungiEvento(e);
			}			
			res.close();		
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
}
