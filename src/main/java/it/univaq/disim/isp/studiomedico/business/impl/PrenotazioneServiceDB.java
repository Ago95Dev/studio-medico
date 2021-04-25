package it.univaq.disim.isp.studiomedico.business.impl;

import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.exceptions.DatabaseException;
import it.univaq.disim.isp.studiomedico.business.exceptions.UtenteNotFoundException;
import it.univaq.disim.isp.studiomedico.domain.Medico;
import it.univaq.disim.isp.studiomedico.domain.*;
import javafx.scene.control.ChoiceBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class PrenotazioneServiceDB extends ConnessioneDB implements PrenotazioneService {

    private static Connection con;

    private UtenteServiceDB utenteservice;

    public PrenotazioneServiceDB() throws BusinessException {

        try {
            con = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new BusinessException("Errore di connessione");
        }
    }

    // query che data una Visita restituisce la Specializzazione associata
    @Override
    public int getIdSpecializzazioneByVisita (String visita){
    	String query = "select specializzazione_di_competenza from tipi_visita where tipo=?";
    	Integer idSpecializzazione = null;
    	try(PreparedStatement st = con.prepareStatement(query)){
            st.setString(1,visita);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    idSpecializzazione = rs.getInt("specializzazione_di_competenza");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
    	return idSpecializzazione;
    }

    // query che data una Specializzazione restituisce la lista dei Medici associati a quella Specializzazione
    @Override
    public List<Medico> getMediciByIdSpecializzazione (int id_specializzazione){
        String query = "select * from utenti where id_specializzazione=?";
        List<Medico> listaMedici = new LinkedList<>();
        Utente utente = null;
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_specializzazione);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    switch (rs.getString("ruolo")) {
                        case "medico":
                            utente = new Medico();
                            break;
                        case "segretaria":
                            utente = new Segretaria();
                            break;
                        case "paziente":
                            utente = new Paziente();
                            break;
                    }
                    Objects.requireNonNull(utente).setId(rs.getInt("id"));
                    utente.setEmail(rs.getString("email"));
                    utente.setCf(rs.getString("codice_fiscale"));
                    utente.setNome(rs.getString("nome"));
                    utente.setCognome(rs.getString("cognome"));
                    utente.setPassword("password");
                    utente.setTelefono(rs.getString("telefono"));
                    utente.setLuogoDiNascita(rs.getString("luogo_di_nascita"));
                    utente.setDataDiNascita(rs.getDate("data_di_nascita"));
                    if (utente instanceof Medico){
                        //((Medico) utente).setSpecializzazione(utenteservice.findSpecializzazionebyId(rs.getInt("id_specializzazione")));
                        //((Medico) utente).setContratto(utenteservice.findContrattobyId(rs.getInt("id_contratto")));
                        //((Medico) utente).setListaTurni("query su turni in base al medico");
                        ((Medico) utente).setNumeropresenze(rs.getInt("numeropresenze"));
                        ((Medico) utente).setNumerovisite(rs.getInt("numeroprestazioni"));
                        listaMedici.add((Medico) utente);
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return listaMedici;
    }



    // query che dato un Medico restituisce la lista dei turni di quel Medico
    @Override
    public List<Turno> getTurniByMedico(String nome, String cognome){
        String query = "select id from utenti where nome=? and cognome=? and ruolo=?";
        String query2 = "select * from turni where id_medico=? and accettato=1";
        Integer id_medico = null;
        List<Turno> listaTurni = new LinkedList<>();
        try (PreparedStatement s = con.prepareStatement(query)) {
            s.setString(1, nome);
            s.setString(2, cognome);
            s.setString(3, "medico");
            try (ResultSet rs = s.executeQuery()) {
                while (rs.next()) {
                    id_medico = rs.getInt("id");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }

        try (PreparedStatement s2 = con.prepareStatement(query2)) {
            s2.setInt(1, id_medico);
            try (ResultSet rs = s2.executeQuery()) {
                while (rs.next()) {
                   Turno turno = new Turno();
                   turno.setId(rs.getInt("id"));
                   turno.setData(rs.getDate("data").toLocalDate());
                   turno.setOrainizio(rs.getTime("ora_inizio").toLocalTime());
                   turno.setOrafine(rs.getTime("ora_fine").toLocalTime());
                   turno.setAccettato(rs.getBoolean("accettato"));
                   turno.setIncorso(rs.getBoolean("in_corso"));
                   listaTurni.add(turno);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return listaTurni;
    }

    // query che dato un Turno di un Medico restituisce la lista delle Prenotazioni effettuate in quel Turno
    @Override
    public List<Prenotazione> getPrenotazioniByIdTurno(int id_turno) {
        String query = "select * from prenotazioni where id_turno=?";
        List<Prenotazione> listaPrenotazioni = new LinkedList<>();
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_turno);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Prenotazione prenotazione = new Prenotazione();
                    prenotazione.setId(rs.getInt("id"));
                    prenotazione.setPaziente(getPazienteById(rs.getInt("id_paziente")));
                    prenotazione.setOrainizio(rs.getTime("inizio").toLocalTime());
                    prenotazione.setOrafine(rs.getTime("fine").toLocalTime());
                    prenotazione.setVisita(getVisitaById(rs.getInt("id_tipo_visita")));
                    listaPrenotazioni.add(prenotazione);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return listaPrenotazioni;
    }

    public Visita getVisitaById(int id_tipo_visita) {
        String query = "select * from tipi_visita where id=?";
        Visita visita = new Visita();
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_tipo_visita);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    Objects.requireNonNull(visita).setId(rs.getInt("id"));
                    visita.setNome(rs.getString("tipo"));
                    switch (rs.getString("durata")) {
                        case "30:00:00":
                            visita.setDurata(Duration.ofMinutes(30));
                            break;
                        case "60:00:00":
                            visita.setDurata(Duration.ofMinutes(60));
                            break;
                        case "120:00:00":
                            visita.setDurata(Duration.ofMinutes(120));
                            break;
                    }
                    visita.setPrezzo(rs.getFloat("prezzo"));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return visita;

    }

    public Utente getPazienteById(int id_paziente) {
        String query = "select * from utenti where id=?"; /*and ruolo=?*/
        Utente paziente = new Utente();
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_paziente);
            //st.setString(2,"paziente");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    Objects.requireNonNull(paziente).setId(rs.getInt("id"));
                    paziente.setEmail(rs.getString("email"));
                    paziente.setCf(rs.getString("codice_fiscale"));
                    paziente.setNome(rs.getString("nome"));
                    paziente.setCognome(rs.getString("cognome"));
                    paziente.setPassword("password");
                    paziente.setTelefono(rs.getString("telefono"));
                    paziente.setLuogoDiNascita(rs.getString("luogo_di_nascita"));
                    paziente.setDataDiNascita(rs.getDate("data_di_nascita"));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return paziente;
    }

    @Override
    public List<Medico> effettuaRicercaPrenotazione (String specializzazione){
    	return getMediciByIdSpecializzazione(getIdSpecializzazione(specializzazione));
    }


    public int getIdSpecializzazione(String specializzazione) {
            String query = "select * from specializzazioni where tipologia=?";
            Integer idSpecializzazione = null;
            try(PreparedStatement st = con.prepareStatement(query)){
                st.setString(1,specializzazione.toString());
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()){
                        idSpecializzazione = rs.getInt("id");
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return idSpecializzazione;
    }

    @Override
    public List<Prenotazione> getPrenotazioniByIdPaziente(int id_paziente) {
        String query = "select * from prenotazioni where id_paziente=?";
        List<Prenotazione> listaPrenotazioni = new LinkedList<>();
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_paziente);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Prenotazione prenotazione = new Prenotazione();
                    prenotazione.setId(rs.getInt("id"));
                    prenotazione.setMedico(getMedicoById(rs.getInt("id_medico")));
                    prenotazione.setPaziente(getPazienteById(rs.getInt("id_paziente")));
                    prenotazione.setOrainizio(rs.getTime("inizio").toLocalTime());
                    prenotazione.setOrafine(rs.getTime("fine").toLocalTime());
                    prenotazione.setVisita(getVisitaById(rs.getInt("id_tipo_visita")));
                    prenotazione.setTurno(getTurnoById(rs.getInt("id_turno")));
                    listaPrenotazioni.add(prenotazione);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return listaPrenotazioni;
    }

    @Override
    public Visita getVisita(String nomevisita) {
        String query = "select * from tipi_visita where tipo=?";
        Visita visita = new Visita();
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setString(1,nomevisita);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    visita.setId(rs.getInt("id"));
                    visita.setNome(rs.getString("tipo"));
                    switch (rs.getString("durata")) {
                        case "30:00:00":
                            visita.setDurata(Duration.ofMinutes(30));
                            break;
                        case "60:00:00":
                            visita.setDurata(Duration.ofMinutes(60));
                            break;
                        case "120:00:00":
                            visita.setDurata(Duration.ofMinutes(120));
                            break;
                    }
                    visita.setPrezzo(rs.getFloat("prezzo"));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return visita;
    }

    @Override
    public List<String> getVisiteByIdTurno(Integer id_turno) {
        Integer id_medico = null;
        List<String> listaVisite = new LinkedList<>();
        String query = "select id_medico from turni where id=?";
        String query2 = "select tipo from tipi_visita where specializzazione_di_competenza=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_turno);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    id_medico = rs.getInt("id_medico");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }

        try(PreparedStatement st2 = con.prepareStatement(query2)){
            st2.setInt(1,getIdSpecializzazionebyIdMedico(id_medico));
            try (ResultSet rs = st2.executeQuery()) {
                while (rs.next()){
                    String s = null;
                    s = rs.getString("tipo");
                    listaVisite.add(s);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return listaVisite;
    }

    @Override
    public void annullaPrenotazione(Integer id_prenotazione) {
        String query = "DELETE FROM prenotazioni WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, id_prenotazione);
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prenotaVisita(Prenotazione prenotazionestore) {
        String query = "insert into prenotazioni(id_medico,id_paziente,inizio,fine,id_tipo_visita,id_turno)" + "values(?,?,?,?,?,?)";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, getTurnoById(prenotazionestore.getTurno().getId()).getId_Medico());
            st.setInt(2, prenotazionestore.getPaziente().getId());
            st.setTime(3, Time.valueOf(prenotazionestore.getOrainizio()));
            st.setTime(4, Time.valueOf(prenotazionestore.getOrafine()));
            st.setInt(5, getIdVisita(prenotazionestore.getVisita().getNome()));
            st.setInt(6,prenotazionestore.getTurno().getId());
            int res = st.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Turno> getTurniByNow(LocalDate now) {
        List<Turno> listaTurni = new LinkedList<>();
        String query = "select * from turni where data=?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setDate(1, Date.valueOf(now));
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Turno turno = new Turno();
                    turno.setId(rs.getInt("id"));
                    turno.setMedico(getMedicoById(rs.getInt("id_medico")));
                    turno.setData(rs.getDate("data").toLocalDate());
                    turno.setOrainizio(rs.getTime("ora_inizio").toLocalTime());
                    turno.setOrafine(rs.getTime("ora_fine").toLocalTime());
                    turno.setAccettato(rs.getBoolean("accettato"));
                    turno.setIncorso(rs.getBoolean("in_corso"));
                    listaTurni.add(turno);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return listaTurni;
    }


    public Integer getIdVisita(String nomevisita) {
        String query = "select id from tipi_visita where tipo=?";
        Integer id_visita = null;
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setString(1,nomevisita);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    id_visita = rs.getInt("id");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return id_visita;
    }

    private int getIdSpecializzazionebyIdMedico(Integer id_medico) {
        Integer id_spec = null;
        String query = "select id_specializzazione from utenti where id=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_medico);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    id_spec = rs.getInt("id_specializzazione");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return id_spec;
    }


    public Turno getTurnoById(int id_turno) {
        String query = "select * from turni where id=?";
        Turno turno = new Turno();
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_turno);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    Objects.requireNonNull(turno).setId(rs.getInt("id"));
                    turno.setData(rs.getDate("data").toLocalDate());
                    turno.setId_Medico(rs.getInt("id_medico"));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return turno;
    }

    private Medico getMedicoById(int id_medico) {
        String query = "select * from utenti where id=? and ruolo=?";
        Medico medico = new Medico();
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_medico);
            st.setString(2,"medico");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    Objects.requireNonNull(medico).setId(rs.getInt("id"));
                    medico.setNome(rs.getString("nome"));
                    medico.setCognome(rs.getString("cognome"));
                    medico.setCf(rs.getString(("codice_fiscale")));
                    medico.setSpecializzazione(findSpecializzazionebyId(rs.getInt("id_specializzazione")));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return medico;
    }

    public Specializzazione findSpecializzazionebyId(int id_specializzazione) {
        String query = "select * from specializzazioni where id=?";
        Specializzazione specializzazione = null;
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_specializzazione);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    specializzazione = Specializzazione.valueOf(rs.getString("tipologia"));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return specializzazione;
    }
}