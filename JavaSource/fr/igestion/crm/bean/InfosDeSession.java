package fr.igestion.crm.bean;

import java.util.Date;

import javax.servlet.http.HttpSession;

import fr.igestion.annuaire.bean.Utilisateur;
import fr.igestion.crm.UtilDate;
import fr.igestion.crm.config.IContacts;

public class InfosDeSession {

    private String utl_id = "", nom_user = "", entreprise = "";
    private String ip = "", instance = "", id_session = "";
    private long duree_inactivite = 0, duree_connexion = 0;
    private Date date_connexion, date_dernier_acces;

    public InfosDeSession(HttpSession session) {

        Date now = new Date();
        this.id_session = session.getId();
        this.date_connexion = new Date(session.getCreationTime());
        this.date_dernier_acces = new Date(session.getLastAccessedTime());
        TeleActeur teleActeur = (TeleActeur) session.getAttribute(IContacts._var_session_teleActeur);
        Utilisateur utilisateur = (Utilisateur) session
                .getAttribute("utilisateur");

        if (teleActeur != null) {
            this.nom_user = teleActeur.getNomPrenom();
            this.utl_id = teleActeur.getUtl_Id();
        }

        if (utilisateur != null) {
            fr.igestion.annuaire.bean.Personne personne = (fr.igestion.annuaire.bean.Personne) utilisateur
                    .getPersonne();
            if (personne != null) {
                this.entreprise = personne.getEntreprise();
            }

        }

        this.ip = (String) session.getAttribute("IP");
        this.instance = (String) session.getAttribute("INSTANCE");

        this.duree_connexion = now.getTime() - session.getCreationTime();
        this.duree_inactivite = now.getTime() - session.getLastAccessedTime();
    }

    public Date getDateConnexion() {
        return this.date_connexion;
    }

    public String getDateConnexionFormatee() {
        return UtilDate.fmtDDMMYYYYHHMMSS(this.date_connexion);
    }

    public Date getDateDernierAcces() {
        return this.date_dernier_acces;
    }

    public String getDateDernierAccesFormatee() {
        return UtilDate.fmtDDMMYYYYHHMMSS(this.date_dernier_acces);
    }

    public long getDureeInactivite() {
        return this.duree_inactivite;
    }

    public long getDureeConnexion() {
        return this.duree_connexion;
    }

    public String getDureeInactiviteFormatee() {
        return UtilDate.formatHHMMSS(new Date(this.duree_inactivite));
    }

    public String getDureeConnexionFormatee() {
        return UtilDate.formatHHMMSS(new Date(this.duree_connexion));
    }

    public String getIdSession() {
        return this.id_session;
    }

    public String getIP() {
        return this.ip;
    }

    public String getNomUser() {
        return this.nom_user;
    }

    public String getUTL_ID() {
        return this.utl_id;
    }

    public String getInstance() {
        return this.instance;
    }

    public String getEntreprise() {
        return this.entreprise;
    }

}
