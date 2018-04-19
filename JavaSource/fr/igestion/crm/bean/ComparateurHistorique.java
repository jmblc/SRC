package fr.igestion.crm.bean;

import java.util.Comparator;

import org.apache.log4j.Logger;

import fr.igestion.crm.IContacts;
import fr.igestion.crm.bean.evenement.Historique;

public class ComparateurHistorique implements Comparator<Historique> {

    private static final Logger LOGGER = Logger.getLogger(ComparateurHistorique.class);
    
    private static final String _CRT_DATEEVENEMENT = "DATEEVENEMENT";
    private static final String _CRT_TYPE = "TYPE";
    private static final String _CRT_OBJET = "OBJET";
    private static final String _CRT_DETAIL = "DETAIL";
    private static final String _CRT_QUALITE = "QUALITE";
    private static final String _CRT_STATUT = "STATUT";
    private static final String _CRT_COMMENTAIRE = "COMMENTAIRE";
    
    private int _ordre;
    private String _colonne;
  
    public ComparateurHistorique(String asc_desc, String colonne) {
        
        this._colonne = colonne;

        if (asc_desc.equalsIgnoreCase(IContacts._ASC)) {
            this._ordre = 1;
        } else {
            this._ordre = -1;
        }
    }

    private int triParDateEvenement(Historique histo1, Historique histo2){
        return this._ordre
                * (histo1.getDateEvenementTri().compareToIgnoreCase(histo2
                        .getDateEvenementTri()));    
    }
    
    private int triParType(Historique histo1, Historique histo2){
        return this._ordre
            * (histo1.getType().compareToIgnoreCase(histo2.getType()));    
    }
    
    private int triParObjet(Historique histo1, Historique histo2){
        return this._ordre
                * (histo1.getObjet().compareToIgnoreCase(histo2.getObjet()));
    }
    
    private int triParDetail(Historique histo1, Historique histo2){
        return this._ordre
                * (histo1.getDetail().compareToIgnoreCase(histo2
                        .getDetail()));
    }
    
    private int triParQualite(Historique histo1, Historique histo2){
        return this._ordre
                * (histo1.getQualite().compareToIgnoreCase(histo2
                        .getQualite()));
    }
    
    private int triParStatut(Historique histo1, Historique histo2){
        return this._ordre
                * (histo1.getStatut().compareToIgnoreCase(histo2
                        .getStatut()));
    }
    
    private int triParCommentaire(Historique histo1, Historique histo2){
        return this._ordre
                * (histo1.getDebutCommentaire().compareToIgnoreCase(histo2
                        .getDebutCommentaire()));    
    }
    
    public int compare(Historique histo1, Historique histo2) {

        if (_CRT_DATEEVENEMENT.equals(_colonne)) {
            return triParDateEvenement(histo1, histo2);
        } else if (_CRT_TYPE.equals(_colonne)) {
            return triParType(histo1, histo2);
        } else if (_CRT_OBJET.equals(_colonne)) {
            return triParObjet(histo1, histo2);
        } else if (_CRT_DETAIL.equals(_colonne)) {
            return triParDetail(histo1, histo2);
        } else if (_CRT_QUALITE.equals(_colonne)) {
            return triParQualite(histo1, histo2);
        } else if (_CRT_STATUT.equals(_colonne)) {
            return triParStatut(histo1, histo2);
        } else if (_CRT_COMMENTAIRE.equals(_colonne)) {
            return triParCommentaire(histo1, histo2);
        } else {
            LOGGER.warn("Colonne non trouvée " + this._colonne);
            return 1;
        }

    }

}
