package it.univaq.disim.isp.studiomedico.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedList;

public class Turno {
    private Integer id;
    private LocalDate data;
    private LocalTime orainizio;
    private LocalTime orafine;
    private boolean accettato;
    private boolean incorso;
    private LinkedList<Prenotazione> listaPrenotazioni = new LinkedList<>();

    public Turno(LocalDate data, LocalTime orainizio, LocalTime orafine, LinkedList<Prenotazione> listaPrenotazioni) {
        super();
        this.data = data;
        this.orainizio = orainizio;
        this.orafine = orafine;
        this.listaPrenotazioni = listaPrenotazioni;
    }

    public Turno(LocalDate data, LocalTime orainizio, LocalTime orafine) {
        super();
        this.data = data;
        this.orainizio = orainizio;
        this.orafine = orafine;
    }

    public Turno() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isAccettato() {
        return accettato;
    }

    public void setAccettato(boolean accettato) {
        this.accettato = accettato;
    }

    public boolean isIncorso() {
        return incorso;
    }

    public void setIncorso(boolean incorso) {
        this.incorso = incorso;
    }

    public LinkedList<Prenotazione> getListaPrenotazioni() {
        return listaPrenotazioni;
    }

    public void setListaPrenotazioni(LinkedList<Prenotazione> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
    }

    public boolean inCorso(){
        return incorso;
    }

    public void addlistaPrenotazioni(Prenotazione prenotazione){
        this.listaPrenotazioni.add(prenotazione);
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOrainizio() {
        return orainizio;
    }

    public void setOrainizio(LocalTime orainizio) {
        this.orainizio = orainizio;
    }

    public LocalTime getOrafine() {
        return orafine;
    }

    public void setOrafine(LocalTime orafine) {
        this.orafine = orafine;
    }

    public void Sort_by_name() {
        Collections.sort(this.listaPrenotazioni, new Sort_by_Start_Time());
    }


}
