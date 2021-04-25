package it.univaq.disim.isp.studiomedico.business.impl;

import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.SegretariaService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;

public class BusinessFactoryDB extends StudioMedicoBusinessFactory {

    private UtenteService utenteService;
    private PrenotazioneService prenotazioneService;
    private SegretariaService segretariaService;

    public BusinessFactoryDB() {

        try {
            utenteService = new UtenteServiceDB();
            prenotazioneService = new PrenotazioneServiceDB();
            segretariaService = new SegretariaServiceDB();
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UtenteService getUtenteService() {
            return utenteService;
        }

    @Override
    public PrenotazioneService getPrenotazioneService() {
        return prenotazioneService;
    }

    @Override
    public SegretariaService getSegretariaService() {
        return segretariaService;
    }

}
