package creazioneDiscussione.controller;

import creazioneDiscussione.Discussione;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import registrazione.Utente;
import storage.DiscussioneDAO;
import storage.FacadeDAO;

import java.io.IOException;

/**
 * La classe permette la creazione di una discussione tramite
 * una servlet che viene richiamata dal bottone della
 * funzione di creazione discussione
 * @author Raffaele Aviello
 */

@WebServlet("/creazione-discussione-servlet")
public class CreazioneDiscussioneServlet extends HttpServlet {

    /**
     * Il metodo permette di gestire la richiesta del client tramite una response che
     * permette la creazione di una discussione tramite la classe DAO relativa alla
     * classe Discussione
     * @param request oggetto che modella una richiesta HTTP
     * @param response oggetto che modella una risposta HTTP
     * */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Utente user = (Utente) httpSession.getAttribute("Utente");
        String title = request.getParameter("topic-title");

        FacadeDAO facadeDAO = new FacadeDAO();

        Discussione topic = new Discussione();
        topic.setIdUtente(user.getIdUtente());
        topic.setTitolo(title);

        facadeDAO.doSave(Discussione.class, topic);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/discussione.jsp");
        requestDispatcher.forward(request, response);
    }
}
