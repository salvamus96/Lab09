package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BordersController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCountries"
    private ComboBox<Country> cmbCountries; // Value injected by FXMLLoader

    @FXML // fx:id="btnTrovaTuttiIVicini"
    private Button btnTrovaTuttiIVicini; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
	
    @FXML
	void doCalcolaConfini(ActionEvent event) {
		this.txtResult.clear();
		
		try {
			int year = Integer.parseInt(this.txtAnno.getText());
			
			if (year < 1816 || year > 2016) {
				this.txtResult.appendText("Inserire un anno nell'intervallo 1816 - 2016!");
				return;
			}
			
			model.createGraph(year);
			
			txtResult.appendText(String.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents()));
			
			for (Country c : model.getNumStatiConfinanti().keySet())
				this.txtResult.appendText(String.format("%s %d\n", c.getStateAbb(), model.getNumStatiConfinanti().get(c)));
				
		} catch (NumberFormatException e) {
			this.txtResult.appendText("Inserire un anno valido!");
		}
			
	}
    
    @FXML
    void doTrovaTuttiIVicini(ActionEvent event) {
		this.txtResult.clear();
		
		try {
			int year = Integer.parseInt(this.txtAnno.getText());
			
			if (year < 1816 || year > 2016) {
				this.txtResult.appendText("Inserire un anno nell'intervallo 1816 - 2016!");
				return;
			}
			
			model.createGraph(year);
			
		} catch (NumberFormatException e) {
			this.txtResult.appendText("Inserire un anno valido!");
			return;
		}
	
    	Country start = this.cmbCountries.getValue();
    	
    	if (this.cmbCountries.getValue() == null) { 
    		this.txtResult.appendText("Selezionare uno stato!");
    		return;
    	}

    	if (!model.vertex(start)) {
    		this.txtResult.appendText(String.format("Lo stato selezionato (%s) non era presente prima dell'anno inserito!", start.getStateNme()));
    		return ;
    	}
    	
//    		for (Country c : model.trovaVicini(start))
//    		for (Country c : model.trovaViciniAmpiezza(start))
//    		for (Country c : model.trovaViciniProfondita(start))
    		for (Country c : model.trovaViciniIteratore(start)) 			
    			
    			this.txtResult.appendText(c + "\n");
    	
    			
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
        assert cmbCountries != null : "fx:id=\"cmbCountries\" was not injected: check your FXML file 'Borders.fxml'.";
        assert btnTrovaTuttiIVicini != null : "fx:id=\"btnTrovaTuttiIVicini\" was not injected: check your FXML file 'Borders.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";
     

		txtResult.setStyle("-fx-font-family: monospace");
    }
    
	public void setModel(Model model) {
		this.model = model;
		
		this.cmbCountries.getItems().addAll(model.getCountries());
	}

}
