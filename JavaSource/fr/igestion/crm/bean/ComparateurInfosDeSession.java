package fr.igestion.crm.bean;

import java.util.Comparator;

import org.apache.log4j.Logger;

import fr.igestion.crm.IContacts;

public class ComparateurInfosDeSession implements Comparator<InfosDeSession> {

    private static final Logger LOGGER = Logger
            .getLogger(ComparateurInfosDeSession.class);

    private int _ordre;
    private String _colonne;

    public ComparateurInfosDeSession(String asc_desc, String colonne) {

        this._colonne = colonne;

        if (IContacts._ASC.equalsIgnoreCase(asc_desc)) {
            this._ordre = 1;
        } else {
            this._ordre = -1;
        }
    }

    public int compare(InfosDeSession a1, InfosDeSession a2) {

        InfosDeSession item1 = (InfosDeSession) a1;
        InfosDeSession item2 = (InfosDeSession) a2;

        if ("DATE_CONNEXION".equals(_colonne)) {
            return this._ordre
                    * item1.getDateConnexion().compareTo(
                            item2.getDateConnexion());
        }
        if ("DATE_DERNIER_ACCES".equals(_colonne)) {
            return this._ordre
                    * item1.getDateDernierAcces().compareTo(
                            item2.getDateDernierAcces());
        } else if ("UTILISATEUR".equals(_colonne)) {
            return this._ordre
                    * item1.getNomUser()
                            .compareToIgnoreCase(item2.getNomUser());
        } else if ("ENTREPRISE".equals(_colonne)) {
            return this._ordre
                    * item1.getEntreprise().compareToIgnoreCase(
                            item2.getEntreprise());
        } else if ("DUREE_CONNEXION".equals(_colonne)) {
            return this._ordre
                    * new Long(item1.getDureeConnexion()).compareTo(new Long(
                            item2.getDureeConnexion()));
        } else if ("DUREE_INACTIVITE".equals(_colonne)) {
            return this._ordre
                    * new Long(item1.getDureeInactivite()).compareTo(new Long(
                            item2.getDureeInactivite()));
        } else if ("IP".equals(_colonne)) {
            return this._ordre
                    * item1.getIP().compareToIgnoreCase(item2.getIP());
        } else if ("INSTANCE".equals(_colonne)) {
            return this._ordre
                    * item1.getInstance().compareToIgnoreCase(
                            item2.getInstance());
        } else {
            LOGGER.warn("Colonne non trouvée " + this._colonne);
            return 1;
        }

    }

}
