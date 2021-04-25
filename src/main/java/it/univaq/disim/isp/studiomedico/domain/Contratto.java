package it.univaq.disim.isp.studiomedico.domain;

public class Contratto {
    private Integer id;
    private TipologiaContratto tipo;
    private float quota;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipologiaContratto getTipo() {
        return tipo;
    }

    public void setTipo(TipologiaContratto tipo) {
        this.tipo = tipo;
    }

    public float getQuota() {
        return quota;
    }

    public void setQuota(float quota) {
        this.quota = quota;
    }

    public String stampaContratto(int numeropresenze, int numeroprestazioni) {
        switch (tipo){
            case Forfettario:
                break;
            case Presenze:
                this.quota *= numeropresenze;
                break;
            case Prestazioni:
                this.quota *= numeroprestazioni;
                break;
        }
        return this.quota + "€";
    }
}
