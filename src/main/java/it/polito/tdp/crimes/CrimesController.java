/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenti;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<LocalDate> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenti> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	if(!isValid()) {
    		return;
    	}
    	String categoria = this.boxCategoria.getValue();
    	LocalDate data = this.boxGiorno.getValue();
    	
    	txtResult.appendText(model.creaGrafo(categoria, data));
    	
    	List<Adiacenti> result = model.getArchiInferiori();
    	txtResult.appendText("\n\n");
    	if(result.isEmpty()) {
    		txtResult.appendText("GRAFO ASSOCIATI ALLA CATEGORIA DI REATO " + categoria + "\nNEL GIORNO " + data + " NON HA ARCHI CON PESO INFERIORE AL PESO MEDIANO");
    		this.boxArco.setDisable(true);
    		this.btnPercorso.setDisable(true);   		
    	}else {
    		txtResult.appendText("ARCHI CON PESO INFERIORE AL PESO MEDIANO:");
    		for(Adiacenti a : result) {
    			txtResult.appendText("\n" + a.toString() + " (" + a.getPeso() + ")");
    		}
    		this.boxArco.setDisable(false);
    		this.btnPercorso.setDisable(false);
    		this.boxArco.getItems().setAll(result);
    	}
    	
    }

    private boolean isValid() {
		boolean check = true;
		if(this.boxCategoria.getValue() == null) {
			txtResult.appendText("ERRORE: scegliere una categoria\n");
			check = false;
		}
		if(this.boxGiorno.getValue() == null) {
			txtResult.appendText("ERRORE: scegliere un giorno");
			check = false;
		}
		return check;
	}

	@FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso...\n");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().setAll(model.getCategorie());
    	this.boxGiorno.getItems().setAll(model.getDate());
    }
}
