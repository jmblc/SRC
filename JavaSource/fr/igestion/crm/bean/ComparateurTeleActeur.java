package fr.igestion.crm.bean;

import java.util.Comparator;

import org.apache.log4j.Logger;

import fr.igestion.crm.config.IContacts;

public class ComparateurTeleActeur implements Comparator<TeleActeur> {

    private static final Logger LOGGER = Logger.getLogger(ComparateurTeleActeur.class); 
    
    private int _ordre;
    private String _colonne;
    
    public ComparateurTeleActeur(String asc_desc, String colonne) {
       
        this._colonne = colonne;

        if (IContacts._ASC.equalsIgnoreCase(asc_desc)) {
            this._ordre = 1;
        } else {
            this._ordre = -1;
        }
    }

    public int compare(TeleActeur teleacteur1, TeleActeur teleacteur2) {
        
        if ("NOM".equals(_colonne)) {
            return this._ordre
                    * (teleacteur1.getNom().compareToIgnoreCase(teleacteur2
                            .getNom()));
        } else if ("PRENOM".equals(_colonne)) {
            return this._ordre
                    * (teleacteur1.getPrenom().compareToIgnoreCase(teleacteur2
                            .getPrenom()));
        } else if ("SOCIETE".equals(_colonne)) {
            return this._ordre
                    * (teleacteur1.getSociete().compareToIgnoreCase(teleacteur2
                            .getSociete()));
        } else if ("SERVICE".equals(_colonne)) {
            return this._ordre
                    * (teleacteur1.getService().compareToIgnoreCase(teleacteur2
                            .getService()));
        } else if ("POLE".equals(_colonne)) {
            return this._ordre
                    * (teleacteur1.getPole().compareToIgnoreCase(teleacteur2
                            .getPole()));
        } else if ("IDHERMES".equals(_colonne)) {
            return this._ordre
                    * (teleacteur1.getIdHermes()
                            .compareToIgnoreCase(teleacteur2.getIdHermes()));
        }

        else {
            LOGGER.warn("Colonne non trouvée " + this._colonne);
            return 1;
        }

    }

}
