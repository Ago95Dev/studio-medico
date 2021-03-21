package it.univaq.disim.isp.studiomedico;

import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import it.univaq.disim.isp.studiomedico.view.ViewException;
import javafx.application.Application;
import javafx.stage.Stage;

public class StudioMedicoApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		try {
			ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
			viewDispatcher.loginView(stage);
		} catch (ViewException e) {
			e.printStackTrace();
		}
	}

}
