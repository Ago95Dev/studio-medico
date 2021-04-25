package it.univaq.disim.isp.studiomedico.controller;

import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.SegretariaService;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.domain.Prenotazione;
import it.univaq.disim.isp.studiomedico.domain.Sort_by_Data;
import it.univaq.disim.isp.studiomedico.domain.Turno;
import it.univaq.disim.isp.studiomedico.domain.Utente;
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
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class GestioneTurniController  implements Initializable,  DataInitializable<Utente> {
    @FXML
    public TableView<Turno> TurniTableView;
    @FXML
    public TableColumn<Turno, String> dataTableColumn;
    @FXML
    public TableColumn<Turno,String> MedicoTableColumn;
    @FXML
    public TableColumn<Turno, LocalTime> oraInizioTableColumn;
    @FXML
    public TableColumn<Turno,LocalTime> oraFineTableColumn;
    @FXML
    public TableColumn<Turno, Button> accettaTableColumn;
    @FXML
    public TableColumn rifiutaTableColumn;
    private final ViewDispatcher manage;
    private PrenotazioneService prenotazioneService;
    private SegretariaService segretariaService;
    private Utente utente;
    private ObservableList<Turno> listaturni;
    private Turno turnostore = new Turno();

    public GestioneTurniController() {
        manage = ViewDispatcher.getInstance();
        StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
        prenotazioneService = factory.getPrenotazioneService();
        segretariaService = factory.getSegretariaService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataTableColumn.setStyle("-fx-alignment: CENTER;");
        MedicoTableColumn.setStyle("-fx-alignment: CENTER;");
        oraInizioTableColumn.setStyle("-fx-alignment: CENTER;");
        oraFineTableColumn.setStyle("-fx-alignment: CENTER;");
        accettaTableColumn.setStyle("-fx-alignment: CENTER;");
        rifiutaTableColumn.setStyle("-fx-alignment: CENTER;");
        dataTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getData().getDayOfWeek().toString()));
        MedicoTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getMedico().getNome() + " " + tf.getValue().getMedico().getCognome()));
        oraInizioTableColumn.setCellValueFactory(new PropertyValueFactory<>("orainizio"));
        oraFineTableColumn.setCellValueFactory(new PropertyValueFactory<>("orafine"));
        accettaTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turno, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Turno,Button> param) {
                // pulsante visualizza
                final Button accettaButton = new Button("Accetta");
                accettaButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                       accettaTurnoProposto(param.getValue());

                    }
                });
                return new SimpleObjectProperty<Button>(accettaButton);
            }
        });
        rifiutaTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turno, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Turno,Button> param) {
                // pulsante visualizza
                final Button rifiutaButton = new Button("Rifiuta");
                rifiutaButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        rifiutaTurnoProposto(param.getValue());

                    }
                });
                return new SimpleObjectProperty<Button>(rifiutaButton);
            }
        });
    }

    private void rifiutaTurnoProposto(Turno turno) {
        this.turnostore = turno;
        turnostore.setId_Medico(turno.getId_Medico());
        turnostore.setData(turno.getData());
        turnostore.setOrainizio(turno.getOrainizio());
        turnostore.setOrafine(turno.getOrafine());
        segretariaService.rifiutaTurnoProposto(turnostore);
        initializeData(manage.getUtenteloggato());
    }

    @FXML
    private void accettaTurnoProposto(Turno turno) {
        this.turnostore = turno;
        turnostore.setId_Medico(turno.getId_Medico());
        turnostore.setData(turno.getData());
        turnostore.setOrainizio(turno.getOrainizio());
        turnostore.setOrafine(turno.getOrafine());
        segretariaService.accettaTurnoProposto(turnostore);
        initializeData(manage.getUtenteloggato());
    }

    @Override
    public void initializeData(Utente utente) {
        stampaListaTurniProposti(segretariaService.getTurniProposti());
    }

    public void stampaListaTurniProposti(List<Turno> listaTurni){
        listaTurni.sort(new Sort_by_Data());
        listaturni = FXCollections.observableList(listaTurni);
        TurniTableView.setItems(listaturni);
    }
}
