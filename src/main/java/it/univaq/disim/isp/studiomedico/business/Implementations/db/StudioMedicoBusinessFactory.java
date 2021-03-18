package it.univaq.disim.isp.studiomedico.business.Implementations.db;

import it.univaq.disim.isp.studiomedico.business.UtenteService;

public abstract class StudioMedicoBusinessFactory {


    private static final StudioMedicoBusinessFactory factory = new BusinessFactoryDB();

    public abstract UtenteService getUtenteService();
}
