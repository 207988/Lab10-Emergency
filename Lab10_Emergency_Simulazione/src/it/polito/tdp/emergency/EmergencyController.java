package it.polito.tdp.emergency;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.emergency.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EmergencyController {
	Model model;
	
	public void setModel(Model m){
		this.model=m;
	}

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtDottore;

    @FXML
    private TextField txtTurno;

    @FXML
    private Button btnDottore;

    @FXML
    private Button btnSimulazione;

    @FXML
    private TextArea txtOutput;

    @FXML
    void doDottore(ActionEvent event) {
    	
    	String nome=txtDottore.getText();
    	String turno=txtTurno.getText();  	
    	try {    		
			long tempo=Long.parseLong(turno.substring(0, 2))*60+Long.parseLong(turno.substring(2,4));
			model.getSimulatore().aggiungiDottore(nome, tempo);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			System.out.println("ERRORE NELL'ORA");
			e.printStackTrace();
		}
    	
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	model.caricaEventi();
    	model.stub();
    }

    @FXML
    void initialize() {
        assert txtDottore != null : "fx:id=\"txtDottore\" was not injected: check your FXML file 'Emergency.fxml'.";
        assert txtTurno != null : "fx:id=\"txtTurno\" was not injected: check your FXML file 'Emergency.fxml'.";
        assert btnDottore != null : "fx:id=\"btnDottore\" was not injected: check your FXML file 'Emergency.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Emergency.fxml'.";
        assert txtOutput != null : "fx:id=\"txtOutput\" was not injected: check your FXML file 'Emergency.fxml'.";

    }
}
