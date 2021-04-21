package it.univaq.disim.isp.studiomedico.controller;

import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.Sort_by_Data;
import it.univaq.disim.isp.studiomedico.domain.Sort_by_Start_Time;
import it.univaq.disim.isp.studiomedico.domain.Turno;
import it.univaq.disim.isp.studiomedico.domain.Utente;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import javafx.beans.property.SimpleObjectProperty;
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

public class StoricoTurniController implements Initializable,  DataInitializable<Utente>{



    @FXML
    public TableView<Turno> TurniTableView;
    @FXML
    public TableColumn<Turno,LocalDate> dataTableColumn;
    @FXML
    public TableColumn<Turno,LocalTime> oraInizioTableColumn;
    @FXML
    public TableColumn<Turno,LocalTime> oraFineTableColumn;
    @FXML
    public TableColumn<Turno, Button> visiteTableColumn;

    private final ViewDispatcher manage;
    private PrenotazioneService prenotazioneService;
    private UtenteService utenteService;
    private Utente utente;
    private ObservableList<Turno> listaturni;

    //avvio controller della vista
    public StoricoTurniController() {
        manage = ViewDispatcher.getInstance();
        StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
        prenotazioneService = factory.getPrenotazioneService();
        utenteService = factory.getUtenteService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataTableColumn.setStyle("-fx-alignment: CENTER;");
        oraInizioTableColumn.setStyle("-fx-alignment: CENTER;");
        oraFineTableColumn.setStyle("-fx-alignment: CENTER;");
        visiteTableColumn.setStyle("-fx-alignment: CENTER;");
        dataTableColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        oraInizioTableColumn.setCellValueFactory(new PropertyValueFactory<>("orainizio"));
        oraFineTableColumn.setCellValueFactory(new PropertyValueFactory<>("orafine"));
        visiteTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turno,Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Turno,Button> param) {
                // pulsante visualizza
                final Button visualizzaButton = new Button("Visualizza");
                visualizzaButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        manage.renderView("elencoprenotazioni", param.getValue());

                    }
                });
                return new SimpleObjectProperty<Button>(visualizzaButton);
            }
        });
    }

    @Override
    public void initializeData(Utente utente) {
        stampaListaTurni(prenotazioneService.getTurniByMedico(utente.getNome(), utente.getCognome()));
    }

    public void stampaListaTurni(List<Turno> listaTurni){
        listaTurni.sort(new Sort_by_Data());
        listaturni = FXCollections.observableList(listaTurni);
        TurniTableView.setItems(listaturni);
    }
}
