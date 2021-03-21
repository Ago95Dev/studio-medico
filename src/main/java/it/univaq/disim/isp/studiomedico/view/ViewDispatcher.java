package it.univaq.disim.isp.studiomedico.view;

import java.io.IOException;

import it.univaq.disim.isp.studiomedico.controller.DataInitializable;
import it.univaq.disim.isp.studiomedico.domain.Utente;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class ViewDispatcher {


	private static final String RESOURE_BASE = "/viste/";
	private static final ViewDispatcher instance = new ViewDispatcher();//dichiaro una varabile statica

	private Stage stage;
	private BorderPane layout;

	private ViewDispatcher() //definisco il costruttore privato cosi nessuna altra classe puo fare new
	{

	}

	public static ViewDispatcher getInstance() //ritorna l'oggetto a cui punta
	{
		return instance;
	}

	public void loginView(Stage stage) throws ViewException {
		this.stage = stage;
		Parent loginView = loadView("login").getView();
		Scene scene = new Scene(loginView);
		stage.setScene(scene);
		stage.show();
	}


	public void loggedIn(Utente utente) {
		try {
			View<Utente> layoutView = loadView("layout");
			//Deve essere invocato il metodo initializeData per fornire al controllore di
			DataInitializable<Utente> layoutController = layoutView.getController();
			layoutController.initializeData(utente);
			layout = (BorderPane) layoutView.getView();
			//Anche in questo caso viene passato l'utente perche' nella vista di
			//benvenuto il testo varia a seconda se e' docente od utente
			renderView("home", utente);
			Scene scene = new Scene(layout);
			stage.setScene(scene);
		} catch (ViewException e) {
			renderError(e);
		}
	}

	public void registraView(Utente utente) throws ViewException {
		//View<Utente> registraView = loadView("registrazione");
		//this.stage=stage;
		Parent registraView = loadView("registrazione").getView();
		Scene scene = new Scene(registraView);
		stage.setScene(scene);
		stage.show();
	}

	public void logout() {
		try {
			Parent loginView = loadView("login").getView();
			Scene scene = new Scene(loginView);
			stage.setScene(scene);
		} catch (ViewException e) {
			renderError(e);
		}
	}

	public <T> void renderView(String viewName, T data) {
		try {
			View<T> view = loadView(viewName);
			DataInitializable<T> controller = view.getController();
			controller.initializeData(data);
			layout.setCenter(view.getView());
		} catch (ViewException e) {
			renderError(e);
		}
	}


	public void renderError(Exception e) {
		e.printStackTrace();
		System.exit(1);
	}


	private <T> View<T> loadView(String viewName) throws ViewException {//ritorna nn un oggetto parent ma un oggetto di tipo view
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURE_BASE + viewName + ".fxml"));
			Parent parent = loader.load();
			return new View<>(parent, loader.getController());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ViewException(e);
		}
	}



}
