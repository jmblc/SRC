package fr.igestion.crm.bean;

import java.util.Comparator;

import org.apache.log4j.Logger;

import fr.igestion.crm.IContacts;
import fr.igestion.crm.bean.contrat.EntiteGestion;

public class ComparateurEntiteGestion implements Comparator<EntiteGestion> {

    private static final Logger LOGGER = Logger
            .getLogger(ComparateurEntiteGestion.class);

    private static final String _MutelleColonne = "MUTUELLE";
    private static final String _CodeColonne = "CODE";
    private static final String _LibelleColonne = "LIBELLE";
    private static final String _NbTeleActeursActifs = "NOMBRETELEACTEURSACTIFS";

    private int _ordre;
    private String _colonne;

    public ComparateurEntiteGestion(String asc_desc, String colonne) {

        this._colonne = colonne;

        if (IContacts._ASC.equalsIgnoreCase(asc_desc)) {
            this._ordre = 1;
        } else {
            this._ordre = -1;
        }
    }

    public int compare(EntiteGestion entite1, EntiteGestion entite2) {

        if (_MutelleColonne.equals(_colonne)) {
            return this._ordre
                    * entite1.getMutuelle().compareToIgnoreCase(
                            entite2.getMutuelle());
        } else if (_CodeColonne.equals(_colonne)) {
            return this._ordre
                    * entite1.getCode().compareToIgnoreCase(entite2.getCode());
        } else if (_LibelleColonne.equals(_colonne)) {
            return this._ordre
                    * entite1.getLibelle().compareToIgnoreCase(
                            entite2.getLibelle());
        } else if (_NbTeleActeursActifs.equals(_colonne)) {
            return this._ordre
                    * entite1.getNbrTeleacteursActifsHabilites()
                            .compareToIgnoreCase(
                                    entite2.getNbrTeleacteursActifsHabilites());
        }

        else {
            LOGGER.warn("Colonne non trouvée " + this._colonne);
            return 1;
        }

    }

}
