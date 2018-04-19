package fr.igestion.crm.bean;

import java.util.Comparator;

import org.apache.log4j.Logger;

import fr.igestion.crm.IContacts;

public class ComparateurSalarie implements Comparator<Salarie> {

    private static final Logger LOGGER = Logger.getLogger(ComparateurSalarie.class);
    
    private int _ordre;
    private String _colonne;

    public ComparateurSalarie(String asc_desc, String colonne) {
    
        this._colonne = colonne;

        if (IContacts._ASC.equalsIgnoreCase(asc_desc)) {
            this._ordre = 1;
        } else {
            this._ordre = -1;
        }
    }

    public int compare(Salarie salarie1, Salarie salarie2) {
        
        if ("ACTIF".equals(_colonne)) {
            return this._ordre
                    * (salarie1.getActif().compareToIgnoreCase(salarie2
                            .getActif()));
        } else if ("CIVILITE".equals(_colonne)) {
            return this._ordre
                    * (salarie1.getCivilite().compareToIgnoreCase(salarie2
                            .getCivilite()));
        } else if ("NOM".equals(_colonne)) {
            return this._ordre
                    * (salarie1.getNom().compareToIgnoreCase(salarie2.getNom()));
        } else if ("PRENOM".equals(_colonne)) {
            return this._ordre
                    * (salarie1.getPrenom().compareToIgnoreCase(salarie2
                            .getPrenom()));
        } else if ("NUMADHERENT".equals(_colonne)) {
            return this._ordre
                    * (salarie1.getNumeroAdherent()
                            .compareToIgnoreCase(salarie2.getNumeroAdherent()));
        } else if ("QUALITE".equals(_colonne)) {
            return this._ordre
                    * (salarie1.getQualite().compareToIgnoreCase(salarie2
                            .getQualite()));
        } else if ("NUMSECU".equals(_colonne)) {
            return this._ordre
                    * (salarie1.getNumeroSecu().compareToIgnoreCase(salarie2
                            .getNumeroSecu()));
        } else if ("DATENAISSANCE".equals(_colonne)) {
            return this._ordre
                    * (salarie1.getDateNaissance().compareTo(salarie2
                            .getDateNaissance()));
        } else if ("CODEGROUPEASSURES".equals(_colonne)) {
            return this._ordre
                    * (salarie1.getCodeGroupeAssures().compareTo(salarie2
                            .getCodeGroupeAssures()));
        } else {
            LOGGER.warn("Colonne non trouvée " + this._colonne);
            return 1;
        }

    }

}
