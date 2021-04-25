package it.univaq.disim.isp.studiomedico.controller;

import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.SegretariaService;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.*;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
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
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class RiscontroVisiteController implements Initializable,  DataInitializable<Utente>{
    @FXML
    public TableView<Prenotazione> RiscontroTableView;
    @FXML
    public TableColumn<Prenotazione, String> dataTableColumn;
    @FXML
    public TableColumn<Prenotazione, String> visitaTableColumn;
    @FXML
    public TableColumn<Prenotazione, String> MedicoTableColumn;
    @FXML
    public TableColumn<Prenotazione, LocalTime> oraInizioTableColumn;
    @FXML
    public TableColumn<Prenotazione, LocalTime> oraFineTableColumn;
    @FXML
    public TableColumn<Prenotazione, String> prezzoTableColumn;
    @FXML
    public TableColumn<Prenotazione, String> durataTableColumn;
    @FXML
    public TableColumn checkinTableColumn;
    @FXML
    public TableColumn<Prenotazione, String> PazienteTableColumn;
    @FXML
    public TableColumn checkoutTableColumn;
    @FXML
    public TableColumn riscontroTableColumn;

    private final ViewDispatcher manage;
    private PrenotazioneService prenotazioneService;
    private SegretariaService segretariaService;
    private ObservableList<Prenotazione> listavisite;
    private Utente utente;


    public RiscontroVisiteController() {
        manage = ViewDispatcher.getInstance();
        StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
        prenotazioneService = factory.getPrenotazioneService();
        segretariaService = factory.getSegretariaService();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataTableColumn.setStyle("-fx-alignment: CENTER;");
        visitaTableColumn.setStyle("-fx-alignment: CENTER;");
        MedicoTableColumn.setStyle("-fx-alignment: CENTER;");
        PazienteTableColumn.setStyle("-fx-alignment: CENTER;");
        oraInizioTableColumn.setStyle("-fx-alignment: CENTER;");
        oraFineTableColumn.setStyle("-fx-alignment: CENTER;");
        durataTableColumn.setStyle("-fx-alignment: CENTER;");
        prezzoTableColumn.setStyle("-fx-alignment: CENTER;");
        riscontroTableColumn.setStyle("-fx-alignment: CENTER;");
/*        checkinTableColumn.setStyle("-fx-alignment: CENTER;");
        checkoutTableColumn.setStyle("-fx-alignment: CENTER;");*/
        dataTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getTurno().getData().toString()));
        visitaTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getVisita().getNome()));
        MedicoTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getMedico().getNome() + " " + tf.getValue().getMedico().getCognome()));
        PazienteTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getPaziente().getNome() + " " + tf.getValue().getPaziente().getCognome()));
        oraInizioTableColumn.setCellValueFactory(new PropertyValueFactory<>("orainizio"));
        oraFineTableColumn.setCellValueFactory(new PropertyValueFactory<>("orafine"));
        durataTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getVisita().stampaDurata()));
        prezzoTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getVisita().stampaPrezzo()));
        riscontroTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Prenotazione, HBox>, ObservableValue<HBox>>() {
            @Override
            public ObservableValue<HBox> call(TableColumn.CellDataFeatures<Prenotazione, HBox> param) {
                // pulsante visualizza
                final Button checkinButton = new Button("Check-in");
                final Button checkoutButton = new Button("Checkout");
                final Button fatturaButton = new Button(" Mostra Fattura");

                if (!param.getValue().isCheckin()) {
                    checkinButton.setDisable(false);
                    checkoutButton.setDisable(true);
                    fatturaButton.setDisable(true);
                }
                if (param.getValue().isCheckin()){
                    checkinButton.setDisable(true);
                    checkoutButton.setDisable(false);
                    fatturaButton.setDisable(true);
                }
                if (param.getValue().isCheckout()){
                    checkinButton.setDisable(true);
                    checkoutButton.setDisable(true);
                    fatturaButton.setDisable(false);
                }
                //checkinButton.setStyle("-fx-end-margin: 10px");
                HBox buttons = new HBox(checkinButton,checkoutButton,fatturaButton);
                checkinButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                            checkin(param.getValue());
                    }
                });
                checkoutButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        checkout(param.getValue());
                    }
                });
                fatturaButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        manage.renderView("visualizzafattura",param.getValue());
                    }
                });
                return new SimpleObjectProperty<HBox>(buttons);
            }
        });
    }

    private void visualizzaFattura(Prenotazione value) {
    }

    @Override
    public void initializeData(Utente utente) throws BusinessException {
        stampaListaVisite(segretariaService.getPrenotazioniByNow(LocalDate.now()));
    }

    public void stampaListaVisite(List<Prenotazione> listaVisite){
        listaVisite.sort(new Sort_by_Start_Time());
        listaVisite.sort(new Sort_By_Data_Prenotazione());
        listavisite = FXCollections.observableList(listaVisite);
        RiscontroTableView.setItems(listavisite);
    }

    @FXML
    private void checkin(Prenotazione prenotazione) {
        if (prenotazione.getTurno().isIncorso()){
            // settare ad 1 il checkin della prenotazione
            segretariaService.updateCheckin(prenotazione);
        }
        else {
            // settare a 1 il campo incorso del turno
            segretariaService.updateInCorsoTurno(prenotazione.getTurno());
            // aumentare di 1 il numero di presenze del medico
            segretariaService.updatePresenzeMedico(prenotazione.getMedico());
            // settare ad 1 il checkin della prenotazione
            segretariaService.updateCheckin(prenotazione);

        }
        stampaListaVisite(segretariaService.getPrenotazioniByNow(LocalDate.now()));
    }

    @FXML
    private void checkout(Prenotazione prenotazione) {
        // aumentare di 1 il campo numero di visite del medico
        segretariaService.updatePrestazioniMedico(prenotazione.getMedico());
        // settare a 1 il campo checkout della prenotazione
        segretariaService.updateCheckout(prenotazione);
        stampaListaVisite(segretariaService.getPrenotazioniByNow(LocalDate.now()));
    }
}

