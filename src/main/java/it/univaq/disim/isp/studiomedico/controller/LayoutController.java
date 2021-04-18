package it.univaq.disim.isp.studiomedico.controller;

import it.univaq.disim.isp.studiomedico.domain.Medico;
import it.univaq.disim.isp.studiomedico.domain.Paziente;
import it.univaq.disim.isp.studiomedico.domain.Segretaria;
import it.univaq.disim.isp.studiomedico.domain.Utente;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

public class LayoutController implements Initializable, DataInitializable<Utente> {

	private static final MenuElement MENU_HOME = new MenuElement("Home", "home");

	private static final MenuElement[] MENU_PAZIENTI = {
			new MenuElement("Prenotazione Visita", "prenotazionevisita"),
			new MenuElement("Storico Visite", "storicovisite"),
			new MenuElement("Profilo Utente", "profiloutente")};
	private static final MenuElement[] MENU_MEDICI = {
			new MenuElement("Storico Visite", "storicovisite")};
	private static final MenuElement[] MENU_SEGRETERIA = {
			new MenuElement("Gestione turni", "gestioneturni"),
			new MenuElement("Gestisci utenti", "gestioneutenti"),
			new MenuElement("Aggiungi Medico","registrazionemedico"),
	};

	@FXML
	private VBox menuBar;

	private Utente utente;

	//private ViewDispatcher dispatcher;
	private ViewDispatcher manage;

	public LayoutController() {
		manage = ViewDispatcher.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@Override
	public void initializeData(Utente utente) {

		this.utente = utente;
		menuBar.getChildren().addAll(createButton(MENU_HOME));
		menuBar.getChildren().add(new Separator());
		if (utente instanceof Segretaria) {
			for (MenuElement menu : MENU_SEGRETERIA) {
				menuBar.getChildren().add(createButton(menu));
				menuBar.getChildren().add(new Separator());
			}
		}
		if (utente instanceof Paziente) {
			for (MenuElement menu : MENU_PAZIENTI) {
				menuBar.getChildren().add(createButton(menu));
				menuBar.getChildren().add(new Separator());
			}
		}
		if (utente instanceof Medico) {
			for (MenuElement menu : MENU_MEDICI) {
				menuBar.getChildren().add(createButton(menu));
				menuBar.getChildren().add(new Separator());
			}
		}
	}

	@FXML
	public void esciAction(MouseEvent event) {
		manage.logout();
		
	}

/*	private Button createButton(MenuElement viewItem) {
		Button button = new Button(viewItem.getNome());
		button.setStyle("-fx-background-color: transparent; -fx-font-size: 14;");
		button.setTextFill(Paint.valueOf("white"));
		button.setPrefHeight(10);
		button.setPrefWidth(180);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dispatcher.renderView(viewItem.getVista());
			}
		});
		return button;
	}*/

	//inizializzazione del menù con i vari bottoni
	private Button createButton(MenuElement viewItem) {

		Button button = new Button(viewItem.getNome());
		button.setStyle("-fx-background-color: transparent; -fx-font-size: 14;");
		button.setTextFill(Paint.valueOf("white"));
		button.setPrefHeight(10);
		button.setPrefWidth(180);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				manage.renderView(viewItem.getVista(), utente);

			}
		});
		return button;
	}
}
