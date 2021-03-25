package it.univaq.disim.isp.studiomedico.controller;

import it.univaq.disim.isp.studiomedico.domain.Utente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable, DataInitializable<Utente> {

	//componente
	@FXML
	private Label benvenutoLabel;

	//inizializzazione
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	//inizializzazione medico e visualizzazione stringa di benvenuto
	public void initializeData(Utente utente) {
		StringBuilder testo = new StringBuilder();
		testo.append("Benvenuto ");
		testo.append(utente.getNome());
		testo.append("  ");
		testo.append(utente.getCognome());
		testo.append("  ");


		benvenutoLabel.setText(testo.toString());
		benvenutoLabel.setAlignment(Pos.CENTER);
	}


}
