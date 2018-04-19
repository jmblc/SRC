package fr.igestion.crm.bean;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import fr.igestion.crm.IContacts;
import fr.igestion.crm.UtilDate;


public class ComparateurMessage implements Comparator<Message> {

    private static final Logger LOGGER = Logger
            .getLogger(ComparateurMessage.class);
    
    private static Date _finDesTemps = null;
    static{
        try{
            _finDesTemps = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE).parse("3000/01/01");
        }
        catch(Exception e){
            LOGGER.error("Initialisation fin des temps",e);
        }
    }
    
    private static final String _DATE_DEBUT="DATE_DEBUT";
    private static final String _DATE_FIN="DATE_FIN";
    private static final String _ACTIF="ACTIF";
    private static final String _TITRE="TITRE";
    private static final String _CAMPAGNE="CAMPAGNE";
    private static final String _CONTENU="CONTENU";
    
    private int _ordre;
    private String _colonne;

    public ComparateurMessage(String asc_desc, String colonne) {
        this._colonne = colonne;

        if (IContacts._ASC.equalsIgnoreCase(asc_desc)) {
            this._ordre = 1;
        } else {
            this._ordre = -1;
        }
    }

    private int triParDateDebut(Message objet1, Message objet2){
        return this._ordre
                * (UtilDate.fmtUSyyyyMMddHHmmss(objet1.getDATEDEBUT())
                        .compareToIgnoreCase(UtilDate
                                .fmtUSyyyyMMddHHmmss(objet2.getDATEDEBUT())));    
    }
    
    private int triParDateFin(Message objet1, Message objet2){
        return this._ordre
                * (UtilDate.fmtUSyyyyMMddHHmmss( (objet1.getDATEFIN()==null)?_finDesTemps : objet1.getDATEFIN() )
                        .compareToIgnoreCase(UtilDate
                                .fmtUSyyyyMMddHHmmss( (objet2.getDATEFIN()==null)?_finDesTemps : objet2.getDATEFIN()  )));    
    }
    
    private int triParActif(Message objet1, Message objet2){
        return this._ordre
                * (objet1.getActif().compareToIgnoreCase(objet2.getActif()));    
    }
    
    private int triParTitre(Message objet1, Message objet2){
        return this._ordre
                * (objet1.getTITRE().compareToIgnoreCase(objet2.getTITRE()));    
    }
    
    private int triParCampagne(Message objet1, Message objet2){
        return this._ordre
                * (objet1.getCampagne()).compareToIgnoreCase(objet2
                        .getCampagne());    
    }
    
    private int triParContenu(Message objet1, Message objet2){
        return this._ordre
                * (objet1.getCONTENU()).compareToIgnoreCase(objet2
                        .getCONTENU());    
    }
    
    
    public int compare(Message objet1, Message objet2) {
        
        if (_DATE_DEBUT.equals(_colonne)) {
            return triParDateDebut(objet1, objet2);
        } else if (_DATE_FIN.equals(_colonne)) {
            return triParDateFin(objet1, objet2);
        } else if (_ACTIF.equals(_colonne)) {
            return triParActif(objet1, objet2);
        } else if (_TITRE.equals(_colonne)) {
            return triParTitre(objet1, objet2);
        } else if (_CAMPAGNE.equals(_colonne)) {
            return triParCampagne(objet1, objet2);
        } else if (_CONTENU.equals(_colonne)) {
            return triParContenu(objet1, objet2);
        } else {
            LOGGER.warn("Colonne non trouvée " + this._colonne);
            return 1;
        }
    }

}
