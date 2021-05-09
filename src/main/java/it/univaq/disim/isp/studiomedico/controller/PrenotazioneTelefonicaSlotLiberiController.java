package it.univaq.disim.isp.studiomedico.controller;

import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.*;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class PrenotazioneTelefonicaSlotLiberiController implements Initializable, DataInitializable<Turno>{
    @FXML
    public ChoiceBox visiteChoiceBoxField;
    @FXML
    public TableColumn prenotaTableColumn;
    @FXML
    public TableColumn<Prenotazione,String> prezzoTableColumn;
    @FXML
    public TableColumn<Prenotazione,String> durataTableColumn;
    @FXML
    public TableColumn<Prenotazione, LocalTime> oraFineTableColumn;
    @FXML
    public TableColumn<Prenotazione,LocalTime> oraInizioTableColumn;
    @FXML
    public TableColumn<Prenotazione,String> visitaTableColumn;
    /* @FXML
     public TableColumn<Turno,LocalDate> dataVisitaTableColumn;*/
    @FXML
    public TableView<Prenotazione> SlotTableView;
    @FXML
    public Button cercaButton;
    @FXML
    public ChoiceBox<String> utentiChoiceBoxField;
    @FXML
    private Label SlotNonDisponibiliLabel;

    private final ViewDispatcher manage;
    private PrenotazioneService prenotazioneService;
    private ObservableList<Prenotazione> listaslot;
    private ObservableList<String> listaVisite;
    private ObservableList<String> listautenti = FXCollections.observableArrayList();
    private List<Prenotazione> listaSlotPrenotabili = new LinkedList<>();
    private Turno turno;
    private Visita visita;
    private Prenotazione prenotazionestore;
    private UtenteService utenteService;
    private List<Utente> listaUtenti = new LinkedList<>();
    @FXML
    private Label PrenotazioneEffettuataLabel;


    //avvio controller della vista
    public PrenotazioneTelefonicaSlotLiberiController() {
        manage = ViewDispatcher.getInstance();
        StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
        prenotazioneService = factory.getPrenotazioneService();
        utenteService = factory.getUtenteService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //dataVisitaTableColumn.setStyle("-fx-alignment: CENTER;");
        visitaTableColumn.setStyle("-fx-alignment: CENTER;");
        oraInizioTableColumn.setStyle("-fx-alignment: CENTER;");
        oraFineTableColumn.setStyle("-fx-alignment: CENTER;");
        durataTableColumn.setStyle("-fx-alignment: CENTER;");
        prezzoTableColumn.setStyle("-fx-alignment: CENTER;");
        prenotaTableColumn.setStyle("-fx-alignment: CENTER;");
        //dataVisitaTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getData().toString()));
        visitaTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getVisita().getNome()));
        oraInizioTableColumn.setCellValueFactory(new PropertyValueFactory<>("orainizio"));
        oraFineTableColumn.setCellValueFactory(new PropertyValueFactory<>("orafine"));
        durataTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getVisita().stampaDurata()));
        prezzoTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getVisita().stampaPrezzo()));
        prenotaTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Prenotazione,Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Prenotazione, Button> param) {
                // pulsante visualizza
                final Button prenotaButton = new Button("Prenota");
                prenotaButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        prenotaSlot(param.getValue());

                    }
                });
                return new SimpleObjectProperty<Button>(prenotaButton);
            }
        });
    }

    @Override
    public void initializeData(Turno turno) throws BusinessException {
        this.turno = turno;
/*        this.turno.setId(turno.getId());
        this.turno.setData(turno.getData());
        this.turno.setOrainizio(turno.getOrainizio());
        this.turno.setOrafine(turno.getOrafine());*/
        this.turno.setListaPrenotazioni((LinkedList<Prenotazione>) prenotazioneService.getPrenotazioniByIdTurno(turno.getId()));
        List<String> listaNomiVisite = new LinkedList<>();

        listaUtenti = utenteService.getAllUtenti();
        for (Utente u: listaUtenti) {
            listautenti.add(u.getNome() + " " + u.getCognome());
        }
        //listautenti = FXCollections.observableArrayList(listaUtenti);
        utentiChoiceBoxField.setItems(listautenti);
        listaNomiVisite = prenotazioneService.getVisiteByIdTurno(turno.getId());
        listaVisite =  FXCollections.observableArrayList(listaNomiVisite);
        visiteChoiceBoxField.setItems(listaVisite);
    }

    public void effettuaRiercaSlot(ActionEvent actionEvent) {
        SlotNonDisponibiliLabel.setVisible(false);
        SlotTableView.getItems().clear();
        this.turno.getListaPrenotazioni().sort(new Sort_by_Start_Time());
        this.visita = prenotazioneService.getVisita(visiteChoiceBoxField.getValue().toString());

        int dur = (int) visita.getDurata().toMinutes();
        boolean disponibili = false;
        // nel caso il turno sia vuoto (nessuna prenotazione effettuata)
        if (turno.getListaPrenotazioni().isEmpty()) {
            Duration total_slot = Duration.between(turno.getOrainizio(), turno.getOrafine());
            LocalTime partial_time = turno.getOrainizio();
            for (int i = 0; i < total_slot.dividedBy(Duration.ofMinutes(visita.getDurata().toMinutes())); i++) {
                listaSlotPrenotabili.add(new Prenotazione(partial_time,partial_time.plus(Duration.ofMinutes(dur)),visita));
                //System.out.println("Visita B: " + partial_time + " " + partial_time.plus(Duration.ofMinutes(dur)));
                disponibili = true;
                if (visita.getDurata().toMinutes() == 60 && !(partial_time.plus(Duration.ofMinutes(visita.getDurata().toMinutes()+30)).isAfter(turno.getOrafine()))) {
                    listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(30)),partial_time.plus(Duration.ofMinutes(dur+30)),visita));
                    //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(30)) + " " + partial_time.plus(Duration.ofMinutes(dur+30)));
                }
                else if (dur == 120 && !(partial_time.plus(Duration.ofMinutes(dur+30)).isAfter(turno.getOrafine()))) {
                    listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(30)),partial_time.plus(Duration.ofMinutes(dur+30)),visita));
                    //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(30)) + " " + partial_time.plus(Duration.ofMinutes(dur+30)));
                    if (!(partial_time.plus(Duration.ofMinutes(dur+60)).isAfter(turno.getOrafine())))
                        listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(60)),partial_time.plus(Duration.ofMinutes(dur+60)),visita));
                    //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(60)) + " " + partial_time.plus(Duration.ofMinutes(dur+60)));
                    if (!(partial_time.plus(Duration.ofMinutes(dur+90)).isAfter(turno.getOrafine())))
                        listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(90)),partial_time.plus(Duration.ofMinutes(dur+90)),visita));
                    //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(90)) + " " + partial_time.plus(Duration.ofMinutes(dur+90)));
                }
                partial_time = partial_time.plus(Duration.ofMinutes(dur));
            }
        }
        else {
            // check tra inizio del turno e la prima prenotazione
            if (!(turno.getOrainizio().equals(turno.getListaPrenotazioni().getFirst().getOrainizio()))) {
                Duration total_slot = Duration.between(turno.getOrainizio(), turno.getListaPrenotazioni().getFirst().getOrainizio());
                LocalTime partial_time = turno.getOrainizio();
                for (int i = 0; i < total_slot.dividedBy(Duration.ofMinutes(dur)); i++) {
                    listaSlotPrenotabili.add(new Prenotazione(partial_time,partial_time.plus(Duration.ofMinutes(dur)),visita));
                    //System.out.println("Visita B: " + partial_time + " " + partial_time.plus(Duration.ofMinutes(dur)));
                    disponibili = true;
                    if (dur == 60 && !(partial_time.plus(Duration.ofMinutes(dur+30)).isAfter(turno.getListaPrenotazioni().getFirst().getOrainizio()))) {
                        listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(30)),partial_time.plus(Duration.ofMinutes(dur+30)),visita));
                        //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(30)) + " " + partial_time.plus(Duration.ofMinutes(dur+30)));
                    }
                    else if (dur == 120 && !(partial_time.plus(Duration.ofMinutes(dur+30)).isAfter(turno.getListaPrenotazioni().getFirst().getOrainizio()))) {
                        listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(30)),partial_time.plus(Duration.ofMinutes(dur+30)),visita));
                        //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(30)) + " " + partial_time.plus(Duration.ofMinutes(dur+30)));
                        if (!(partial_time.plus(Duration.ofMinutes(dur+90)).isAfter(turno.getListaPrenotazioni().getFirst().getOrainizio())))
                            listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(60)),partial_time.plus(Duration.ofMinutes(dur+60)),visita));
                        //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(60)) + " " + partial_time.plus(Duration.ofMinutes(dur+60)));
                        if (!(partial_time.plus(Duration.ofMinutes(dur+90)).isAfter(turno.getListaPrenotazioni().getFirst().getOrainizio())))
                            listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(90)),partial_time.plus(Duration.ofMinutes(dur+90)),visita));
                        //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(90)) + " " + partial_time.plus(Duration.ofMinutes(dur+90)));
                    }
                    partial_time = partial_time.plus(Duration.ofMinutes(dur));
                }
            }
            // ciclo tra le prenotazioni
            Prenotazione x = turno.getListaPrenotazioni().getFirst();
            for (int j = 0; j < turno.getListaPrenotazioni().size(); j++) {
                if (turno.getListaPrenotazioni().size()==1)
                    break;
                if (!(x.getOrafine().equals(turno.getListaPrenotazioni().get(j+1).getOrainizio()))) {
                    Duration total_slot = Duration.between(x.getOrafine(), turno.getListaPrenotazioni().get(j+1).getOrainizio());
                    LocalTime partial_time = x.getOrafine();
                    for (int i = 0; i < total_slot.dividedBy(Duration.ofMinutes(dur)); i++) {
                        listaSlotPrenotabili.add(new Prenotazione(partial_time,partial_time.plus(Duration.ofMinutes(dur)),visita));
                        //System.out.println("Visita B: " + partial_time + " " + partial_time.plus(Duration.ofMinutes(dur)));
                        disponibili = true;
                        if (dur == 60 && !(partial_time.plus(Duration.ofMinutes(dur+30)).isAfter(turno.getListaPrenotazioni().get(j+1).getOrainizio()))) {
                            listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(30)),partial_time.plus(Duration.ofMinutes(dur+30)),visita));
                            //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(30)) + " " + partial_time.plus(Duration.ofMinutes(dur+30)));
                        }
                        else if (dur == 120 && !(partial_time.plus(Duration.ofMinutes(dur+30)).isAfter(turno.getListaPrenotazioni().get(j+1).getOrainizio()))) {
                            listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(30)),partial_time.plus(Duration.ofMinutes(dur+30)),visita));
                            //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(30)) + " " + partial_time.plus(Duration.ofMinutes(dur+30)));
                            if (!(partial_time.plus(Duration.ofMinutes(dur+60)).isAfter(turno.getListaPrenotazioni().get(j+1).getOrainizio())))
                                listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(60)),partial_time.plus(Duration.ofMinutes(dur+60)),visita));
                            //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(60)) + " " + partial_time.plus(Duration.ofMinutes(dur+60)));
                            if (!(partial_time.plus(Duration.ofMinutes(dur+90)).isAfter(turno.getListaPrenotazioni().get(j+1).getOrainizio())))
                                listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(90)),partial_time.plus(Duration.ofMinutes(dur+90)),visita));
                            //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(90)) + " " + partial_time.plus(Duration.ofMinutes(dur+90)));
                        }
                        partial_time = partial_time.plus(Duration.ofMinutes(dur));
                    }
                }
                if (turno.getListaPrenotazioni().get(j+1).equals(turno.getListaPrenotazioni().getLast()))
                    break;
                x = turno.getListaPrenotazioni().get(j+1);
            }
            // check tra l'ultima prenotazione e la fine del turno
            if (!(turno.getOrafine().equals(turno.getListaPrenotazioni().getLast().getOrafine()))) {
                Duration total_slot = Duration.between(turno.getListaPrenotazioni().getLast().getOrafine(), turno.getOrafine());
                LocalTime partial_time = turno.getListaPrenotazioni().getLast().getOrafine();
                for (int i = 0; i < total_slot.dividedBy(Duration.ofMinutes(dur)); i++) {
                    listaSlotPrenotabili.add(new Prenotazione(partial_time,partial_time.plus(Duration.ofMinutes(dur)),visita));
                    //System.out.println("Visita B: " + partial_time + " " + partial_time.plus(Duration.ofMinutes(dur)));
                    disponibili = true;
                    if (dur == 60 && !(partial_time.plus(Duration.ofMinutes(dur+30)).isAfter(turno.getOrafine()))) {
                        listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(30)),partial_time.plus(Duration.ofMinutes(dur+30)),visita));
                        //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(30)) + " " + partial_time.plus(Duration.ofMinutes(dur+30)));
                    }
                    else if (dur == 120 && !(partial_time.plus(Duration.ofMinutes(dur+30)).isAfter(turno.getOrafine()))) {
                        listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(30)),partial_time.plus(Duration.ofMinutes(dur+30)),visita));
                        //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(30)) + " " + partial_time.plus(Duration.ofMinutes(dur+30)));
                        if (!(partial_time.plus(Duration.ofMinutes(dur+60)).isAfter(turno.getOrafine())))
                            listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(60)),partial_time.plus(Duration.ofMinutes(dur+60)),visita));
                        //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(60)) + " " + partial_time.plus(Duration.ofMinutes(dur+60)));
                        if (!(partial_time.plus(Duration.ofMinutes(dur+90)).isAfter(turno.getOrafine())))
                            listaSlotPrenotabili.add(new Prenotazione(partial_time.plus(Duration.ofMinutes(90)),partial_time.plus(Duration.ofMinutes(dur+90)),visita));
                        //System.out.println("Visita B: " + partial_time.plus(Duration.ofMinutes(90)) + " " + partial_time.plus(Duration.ofMinutes(dur+90)));
                    }
                    partial_time = partial_time.plus(Duration.ofMinutes(dur));
                }
            }
        }
        if (disponibili == false) {
            SlotNonDisponibiliLabel.setText("Non ci sono Slot Disponibili");
            SlotNonDisponibiliLabel.setVisible(true);
            //System.out.println("Non ci sono slot disponibili!");
        }
        else {
            // Questo controllo sarebbe da testare accuratamente se funziona in modo corretto
            List<Prenotazione> listaPrenotazioniUtente = prenotazioneService.getPrenotazioniByIdPaziente(manage.getUtenteloggato().getId());
            for (Prenotazione p: listaPrenotazioniUtente){
                for (Prenotazione s: listaSlotPrenotabili) {
                    if (p.getTurno().getData().equals(turno.getData())){
                        if ((p.getOrainizio().equals(s.getOrainizio()) && p.getOrafine().equals(s.getOrafine())) ||
                                (p.getOrainizio().isAfter(s.getOrainizio()) && p.getOrafine().isAfter(s.getOrafine())) ||
                                (p.getOrainizio().isBefore(s.getOrainizio()) && p.getOrafine().isBefore(s.getOrafine())))
                            listaSlotPrenotabili.remove(s);
                    }
                }
            }
            listaslot = FXCollections.observableList(listaSlotPrenotabili);
            SlotTableView.setItems(listaslot);
        }
    }

    @FXML
    private void prenotaSlot(Prenotazione prenotazione){
        String stringapaziente = utentiChoiceBoxField.getValue();
        String[] splitStrings = stringapaziente.split("\\s+");
        Utente paziente = new Utente();
        for (Utente u: listaUtenti){
            if (u.getNome().equals(splitStrings[0]) && u.getCognome().equals(splitStrings[1]))
                paziente = u;
        }
        this.prenotazionestore = prenotazione;
        prenotazionestore.setTurno(this.turno);
        prenotazionestore.setPaziente((Paziente) paziente);
        prenotazionestore.setOrainizio(prenotazione.getOrainizio());
        prenotazionestore.setOrafine(prenotazione.getOrafine());
        prenotazionestore.setVisita(this.visita);
        prenotazioneService.prenotaVisita(prenotazionestore);
        PrenotazioneEffettuataLabel.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(javafx.util.Duration.seconds(3));
        visiblePause.setOnFinished(
                event -> PrenotazioneEffettuataLabel.setVisible(false)
        );
        visiblePause.play();
        SlotTableView.getItems().clear();
    }
}
