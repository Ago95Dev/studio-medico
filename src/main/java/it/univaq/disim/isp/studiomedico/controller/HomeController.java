package it.univaq.disim.isp.studiomedico.controller;

import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.domain.Turno;
import it.univaq.disim.isp.studiomedico.domain.Utente;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable, DataInitializable<Utente> {

	@FXML
	public TableView<Turno> TurniTableView;
	@FXML
	public TableColumn<Turno, LocalDate> dataTurnoTableColumn;
	@FXML
	public TableColumn<Turno, String> medicoTableColumn;
	@FXML
	public TableColumn<Turno, String> specializzazioneTableColumn;
	@FXML
	public TableColumn<Turno, String> oraInizioTableColumn;
	@FXML
	public TableColumn<Turno, String> oraFineTableColumn;
	@FXML
	public TableColumn prenotaTableColumn;
	@FXML
	private Label benvenutoLabel;

	private final ViewDispatcher manage;
	private PrenotazioneService prenotazioneService;

	//avvio controller della vista
	public HomeController() {
		manage = ViewDispatcher.getInstance();
		StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
		prenotazioneService = factory.getPrenotazioneService();
	}

	//inizializzazione
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dataTurnoTableColumn.setStyle("-fx-alignment: CENTER;");
		medicoTableColumn.setStyle("-fx-alignment: CENTER;");
		specializzazioneTableColumn.setStyle("-fx-alignment: CENTER;");
		oraInizioTableColumn.setStyle("-fx-alignment: CENTER;");
		oraFineTableColumn.setStyle("-fx-alignment: CENTER;");
		prenotaTableColumn.setStyle("-fx-alignment: CENTER;");
		dataTurnoTableColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
		medicoTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getMedico().getNome() + " " + tf.getValue().getMedico().getCognome()));
		specializzazioneTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getMedico().getSpecializzazione().toString()));
		oraInizioTableColumn.setCellValueFactory(new PropertyValueFactory<>("orainizio"));
		oraFineTableColumn.setCellValueFactory(new PropertyValueFactory<>("orafine"));
		prenotaTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turno, Button>, ObservableValue<Button>>() {
			@Override
			public ObservableValue<Button> call(TableColumn.CellDataFeatures<Turno,Button> param) {
				// pulsante visualizza
				final Button prenotaButton = new Button("Visualizza");
				prenotaButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						manage.renderView("prenotazioneslotliberi", param.getValue());

					}
				});
				return new SimpleObjectProperty<Button>(prenotaButton);
			}
		});
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
		stampaListaTurni(prenotazioneService.getTurniByNow(LocalDate.now()));

	}

	public void stampaListaTurni(List<Turno> listaTurni){
		listaTurni.removeIf(t -> t.getMedico().getCf().equals(manage.getUtenteloggato().getCf()));
		TurniTableView.setItems(FXCollections.observableList(listaTurni));
	}

}
