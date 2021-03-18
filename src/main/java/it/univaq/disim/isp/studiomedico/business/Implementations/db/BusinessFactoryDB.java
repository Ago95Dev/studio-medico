package it.univaq.disim.isp.studiomedico.business.Implementations.db;
import it.univaq.disim.isp.studiomedico.business.BusinessException;
import it.univaq.disim.isp.studiomedico.business.UtenteService;

public class BusinessFactoryDB extends StudioMedicoBusinessFactory {

        private UtenteService utenteService;

    public BusinessFactoryDB() {

        try {
            utenteService = new UtenteServiceDB();
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

        @Override
        public UtenteService getUtenteService() {
            return utenteService;
        }

}
