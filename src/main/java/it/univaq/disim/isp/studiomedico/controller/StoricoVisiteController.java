package it.univaq.disim.isp.studiomedico.controller;

import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.domain.*;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class StoricoVisiteController implements Initializable,  DataInitializable<Utente>{

    @FXML
    public TableView<Prenotazione> PrenotazioniTableView;
    @FXML
    public TableColumn<Prenotazione,String> dataTableColumn;
    @FXML
    public TableColumn<Prenotazione,String> visitaTableColumn;
    @FXML
    public TableColumn<Prenotazione,String> MedicoTableColumn;
    @FXML
    public TableColumn annullaTableColumn;
    @FXML
    public TableColumn<Prenotazione,LocalTime> oraInizioTableColumn;
    @FXML
    public TableColumn<Prenotazione,LocalTime> oraFineTableColumn;
    @FXML
    public TableColumn<Prenotazione,String> durataTableColumn;
    @FXML
    public TableColumn<Prenotazione,String> prezzoTableColumn;

    private final ViewDispatcher manage;
    private PrenotazioneService prenotazioneService;
    private ObservableList<Prenotazione> listavisite;
    private Utente utente;

    //avvio controller della vista
    public StoricoVisiteController() {
        manage = ViewDispatcher.getInstance();
        StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
        prenotazioneService = factory.getPrenotazioneService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataTableColumn.setStyle("-fx-alignment: CENTER;");
        visitaTableColumn.setStyle("-fx-alignment: CENTER;");
        MedicoTableColumn.setStyle("-fx-alignment: CENTER;");
        oraInizioTableColumn.setStyle("-fx-alignment: CENTER;");
        oraFineTableColumn.setStyle("-fx-alignment: CENTER;");
        durataTableColumn.setStyle("-fx-alignment: CENTER;");
        prezzoTableColumn.setStyle("-fx-alignment: CENTER;");
        annullaTableColumn.setStyle("-fx-alignment: CENTER;");
        dataTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getTurno().getData().toString()));
        visitaTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getVisita().getNome()));
        MedicoTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getMedico().getNome() + " " + tf.getValue().getMedico().getCognome()));
        oraInizioTableColumn.setCellValueFactory(new PropertyValueFactory<>("orainizio"));
        oraFineTableColumn.setCellValueFactory(new PropertyValueFactory<>("orafine"));
        durataTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getVisita().stampaDurata()));
        prezzoTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getVisita().stampaPrezzo()));
        annullaTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Prenotazione, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Prenotazione, Button> param) {
                // pulsante visualizza
                final Button annullaButton = new Button("Annulla Visita");
                final Button eseguitaButton = new Button("Visita effettuata");
                eseguitaButton.setDisable(true);
                annullaButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        annullaPrenotazione(param.getValue());

                    }
                });
                if (!(LocalDate.now().isBefore(param.getValue().getTurno().getData()))) {
                    return new SimpleObjectProperty<Button>(eseguitaButton);
                }
                return new SimpleObjectProperty<Button>(annullaButton);
            }
        });
    }

    @FXML
    private void annullaPrenotazione(Prenotazione prenotazione) {
        prenotazioneService.annullaPrenotazione(prenotazione.getId());
        manage.renderView("storicovisite",utente);
    }

    @Override
    public void initializeData(Utente utente) {
        this.utente = utente;
        stampaListaVisite(prenotazioneService.getPrenotazioniByIdPaziente(utente.getId()));
    }

    public void stampaListaVisite(List<Prenotazione> listaVisite){
        listaVisite.sort(new Sort_by_Start_Time());
        listaVisite.sort(new Sort_By_Data_Prenotazione());
        listavisite = FXCollections.observableList(listaVisite);
        PrenotazioniTableView.setItems(listavisite);
    }
}
