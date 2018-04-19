package fr.igestion.crm.bean;

import java.util.Comparator;

import org.apache.log4j.Logger;

import fr.igestion.crm.config.IContacts;


public class ComparateurTransfert implements Comparator<Transfert> {

    private static final Logger LOGGER = Logger.getLogger(ComparateurTransfert.class);

    private static final String _LIBELLE="LIBELLE";
    private static final String _EMAIL="EMAIL";
    
    private int _ordre;
    private String _colonne;

    public ComparateurTransfert(String asc_desc, String colonne) {

        this._colonne = colonne;

        if (IContacts._ASC.equalsIgnoreCase(asc_desc)) {
            this._ordre = 1;
        } else {
            this._ordre = -1;
        }
    }
    
    private int triParLibelle(Transfert objet1, Transfert objet2){
        return this._ordre
                * (objet1.getTRA_LIBELLE().compareToIgnoreCase(objet2
                        .getTRA_LIBELLE()));
    }

    private int triParEMail(Transfert objet1, Transfert objet2){
        return this._ordre
                * (objet1.getTRA_EMAIL().compareToIgnoreCase(objet2
                        .getTRA_EMAIL()));    
    }
    
    public int compare(Transfert objet1, Transfert objet2) {

        if (_LIBELLE.equals(_colonne)) {
            return triParLibelle(objet1, objet2);
        } else if (_EMAIL.equals(_colonne)) {
            return triParEMail(objet1, objet2);
        }

        else {
            LOGGER.warn("Colonne non trouvée " + this._colonne);
            return 1;
        }

    }

}
