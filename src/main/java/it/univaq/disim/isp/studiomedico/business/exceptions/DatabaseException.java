package it.univaq.disim.isp.studiomedico.business.exceptions;

import java.sql.SQLException;

public class DatabaseException extends BusinessException {

    public DatabaseException(String errore_esecuzione_query, SQLException e) {
        super();
    }

    public DatabaseException(String msg) {
        super(msg);
    }

}


