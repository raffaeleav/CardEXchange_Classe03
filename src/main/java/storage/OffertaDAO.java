package storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import acquisto.Offerta;
import storage.ConPool;

/*
La classe OffertaDAO rappresenta il Data Access Object (DAO) per le entità Offerta. Si tratta di una classe che definisce i metodi per gestire le operazioni CRUD (create, read, update, delete) sulle offerta nel database.

In particolare, la classe include i seguenti metodi:
addOfferta
getOffertaById
getAllOfferte
deleteOfferta
Per la connessione al database viene utilizzata la classe ConPool.
*/

public class OffertaDAO {

    //elenco delle query
    private static final String INSERT_OFFERTA_QUERY = "INSERT INTO Offerta(condizione, prezzo, idUtente, idCarta) VALUES (?, ?, ?, ?)";
    private static final String SELECT_OFFERTA_BY_ID_QUERY = "SELECT * FROM Offerta WHERE idOfferta = ?";

    /*
    la query seleziona tutte le colonne (*) dalla tabella "Offerta" o,
    che vengono unite alle colonne della tabella "CarrelloContieneOfferta" cco sulla base dell'uguaglianza dell'idOfferta,
    che a sua volta vengono unite alle colonne della tabella "Carrello" c sulla base dell'uguaglianza dell'idCarrello.
    Infine, la query filtra i risultati utilizzando il parametro idUtente fornito.
     */
    private static final String SELECT_OFFERTE_BY_ID_UTENTE_QUERY =
            "SELECT * FROM Offerta o\n" +
                    "JOIN CarrelloContieneOfferta cco ON o.idOfferta = cco.idOfferta\n" +
                    "JOIN Carrello c ON cco.idCarrello = c.idCarrello\n" +
                    "WHERE c.idUtente = ?;";
    private static final String SELECT_ALL_OFFERTE_QUERY = "SELECT * FROM Offerta";
    //private static final String SELECT_OFFERTE_BY_ID_CARRELLO_QUERY = "SELECT * FROM Offerta o INNER JOIN CarrelloContieneOfferta cco ON o.idOfferta = cco.idOfferta WHERE cco.idCarrello = ?";

    private static final String DELETE_OFFERTA_QUERY = "DELETE FROM Offerta WHERE idOfferta = ?";

    /*
    addOfferta(Offerta offerta): inserisce una nuova offerta nel database
    questo metodo prende in input un oggetto di tipo Offerta e lo inserisce nel database, utilizzando la query 'INSERT_OFFERTA_QUERY'.
    I valori dei campi vengono impostati utilizzando i metodi get dell'oggetto Offerta.
    */
    public void addOfferta(Offerta offerta) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement statement = con.prepareStatement(INSERT_OFFERTA_QUERY);
            statement.setString(1, offerta.getCondizione());
            statement.setDouble(2, offerta.getPrezzo());
            statement.setInt(3, offerta.getIdUtente());
            statement.setInt(4, offerta.getIdCarta());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    getOffertaById(int id): recupera un'offerta dal database in base all'id specificato
    questo metodo prende in input un intero id e restituisce l'offerta presente nel database con quell'id, utilizzando la query 'SELECT_OFFERTA_BY_ID_QUERY'.
    I risultati vengono estratti dal ResultSet e utilizzati per costruire un nuovo oggetto Offerta da restituire.
    */
    public static Offerta getOffertaById(int idOfferta) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement statement = con.prepareStatement(SELECT_OFFERTA_BY_ID_QUERY);
            statement.setInt(1, idOfferta);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String condizione = resultSet.getString("condizione");
                double prezzo = resultSet.getDouble("prezzo");
                int idUtente = resultSet.getInt("idUtente");
                int idCarta = resultSet.getInt("idCarta");
                return new Offerta(idOfferta, condizione, prezzo, idUtente, idCarta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    getAllOfferte(): recupera tutte le offerta presenti nel database.
    questo metodo restituisce la lista di tutte le offerte presenti nel database, utilizzando la query 'SELECT_ALL_OFFERTE_QUERY'.
    I risultati vengono estratti dal ResultSet e utilizzati per costruire una lista di oggetti Offerta da restituire.
    */
    public List < Offerta > getAllOfferte() {
        List < Offerta > offerte = new ArrayList < > ();
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement statement = con.prepareStatement(SELECT_ALL_OFFERTE_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idOfferta = resultSet.getInt("idOfferta");
                String condizione = resultSet.getString("condizione");
                double prezzo = resultSet.getDouble("prezzo");
                int idUtente = resultSet.getInt("idUtente");
                int idCarta = resultSet.getInt("idCarta");
                Offerta offerta = new Offerta(idOfferta, condizione, prezzo, idUtente, idCarta);
                offerte.add(offerta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offerte;
    }

    public static List<Offerta> getOfferteByIdUtente(int idUtente) {
        List<Offerta> offerte = new ArrayList<>();
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement statement = con.prepareStatement(SELECT_OFFERTE_BY_ID_UTENTE_QUERY);
            statement.setInt(1, idUtente);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idOfferta = resultSet.getInt("idOfferta");
                String condizione = resultSet.getString("condizione");
                double prezzo = resultSet.getInt("prezzo");
                int idCarta = resultSet.getInt("idCarta");
                Offerta offerta = new Offerta(idOfferta, condizione, prezzo, idUtente, idCarta);
                offerte.add(offerta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offerte;
    }

    public static List<Offerta> getOfferteByIdOrdine(int idOrdine) throws SQLException {
        // Crea una lista vuota di offerte
        List<Offerta> offerte = new ArrayList<>();

        // Apre una connessione al database
        Connection conn = ConPool.getConnection();

        // Crea una stringa con la query SQL per recuperare le offerte di un ordine specifico utilizzando una JOIN con la tabella OrdineContieneOfferte
        String query = "SELECT o.* FROM Offerta o JOIN OrdineContieneOfferte oco ON o.idOfferta = oco.idOfferta WHERE oco.idOrdine = ?";

        // Crea un PreparedStatement utilizzando la query creata in precedenza
        PreparedStatement stmt = conn.prepareStatement(query);

        // Imposta il parametro della query con l'id dell'ordine passato come argomento
        stmt.setInt(1, idOrdine);

        // Esegue la query e ottiene il risultato
        ResultSet rs = stmt.executeQuery();

        // Itera sui risultati della query e crea un oggetto Offerta per ogni riga del risultato
        while (rs.next()) {
            int idOfferta = rs.getInt("idOfferta");
            String condizione = rs.getString("condizione");
            double prezzo = rs.getDouble("prezzo");
            int idUtente = rs.getInt("idUtente");
            int idCarta = rs.getInt("idCarta");


            Offerta offerta = new Offerta(idOfferta, condizione, prezzo, idUtente, idCarta);
            offerte.add(offerta);
        }

        return offerte;
    }

    /*
    deleteOfferta(int id): elimina un'offerta dal database in base all'id specificato.
    questo metodo prende in input un intero id e cancella dal database l'offerta con quell'id, utilizzando la query 'DELETE_OFFERTA_QUERY'.
    */
    public void deleteOfferta(int idOfferta) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement statement = con.prepareStatement(DELETE_OFFERTA_QUERY);
            statement.setInt(1, idOfferta);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}





