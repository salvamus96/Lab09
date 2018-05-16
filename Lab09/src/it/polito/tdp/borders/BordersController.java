package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";
     

		txtResult.setStyle("-fx-font-family: monospace");
    }
    
	public void setModel(Model model) {
		this.model = model;
	}
	
}
