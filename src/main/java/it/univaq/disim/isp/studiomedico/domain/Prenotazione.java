package it.univaq.disim.isp.studiomedico.domain;

import java.time.LocalTime;


public class Prenotazione implements Comparable<Prenotazione>{
    private Integer id;
    private LocalTime orainizio;
    private LocalTime orafine;
    private boolean checkin;
    private boolean checkout;
    private Visita visita;
    private Fattura fattura;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public boolean isCheckin() {
        return checkin;
    }

    public void setCheckin(boolean checkin) {
        this.checkin = checkin;
    }

    public boolean isCheckout() {
        return checkout;
    }

    public void setCheckout(boolean checkout) {
        this.checkout = checkout;
    }

    public Visita getVisita() {
        return visita;
    }

    public void setVisita(Visita visita) {
        this.visita = visita;
    }

    public Fattura getFattura() {
        return fattura;
    }

    public void setFattura(Fattura fattura) {
        this.fattura = fattura;
    }

    @Override
    public int compareTo(Prenotazione o) {
        return 0;
    }
}
