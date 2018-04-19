package fr.igestion.crm.bean;

import java.util.Comparator;

import org.apache.log4j.Logger;

import fr.igestion.crm.IContacts;
import fr.igestion.crm.UtilDate;

public class ComparateurObjetRecherche implements Comparator<ObjetRecherche> {

    private static final Logger LOGGER = Logger.getLogger( ComparateurObjetRecherche.class );
    
    private int _ordre;
    private String _colonne;
    
    public ComparateurObjetRecherche(String asc_desc, String colonne) {
    
        this._colonne = colonne;

        if (IContacts._ASC.equalsIgnoreCase(asc_desc)) {
            this._ordre = 1;
        } else {
            this._ordre = -1;
        }
    }

    public int compare(ObjetRecherche objet1, ObjetRecherche objet2) {
        
        if ("DATENAISSANCE".equals(_colonne)) {
            return this._ordre
                    * (UtilDate.fmtUSyyyyMMddHHmmss(objet1.getDateNaissance())
                            .compareToIgnoreCase(UtilDate
                                    .fmtUSyyyyMMddHHmmss(objet2
                                            .getDateNaissance())));
        } else if ("ID".equals(_colonne)) {
            return this._ordre
                    * (objet1.getId().compareToIgnoreCase(objet2.getId()));
        } else if ("CODEADHERENTNUMEROCONTRAT".equals(_colonne)) {
            return this._ordre
                    * (objet1.getCodeAdherentNumeroContrat())
                            .compareToIgnoreCase(objet2
                                    .getCodeAdherentNumeroContrat());
        } else if ("MUTUELLE".equals(_colonne)) {
            return this._ordre
                    * (objet1.getMutuelle().compareToIgnoreCase(objet2
                            .getMutuelle()));
        } else if ("ENTITEGESTION".equals(_colonne)) {
            return this._ordre
                    * (objet1.getEntiteGestion().compareToIgnoreCase(objet2
                            .getEntiteGestion()));
        } else if ("CIVILITE".equals(_colonne)) {
            return this._ordre
                    * (objet1.getCivilite().compareToIgnoreCase(objet2
                            .getCivilite()));
        } else if ("CODEPOSTAL".equals(_colonne)) {
            return this._ordre
                    * (objet1.getCodePostal().compareToIgnoreCase(objet2
                            .getCodePostal()));
        } else if ("LOCALITE".equals(_colonne)) {
            return this._ordre
                    * (objet1.getLocalite().compareToIgnoreCase(objet2
                            .getLocalite()));
        } else if ("NOM".equals(_colonne)) {
            return this._ordre
                    * (objet1.getNom().compareToIgnoreCase(objet2.getNom()));
        } else if ("PRENOM".equals(_colonne)) {
            return this._ordre
                    * (objet1.getPrenom().compareToIgnoreCase(objet2
                            .getPrenom()));
        } else if ("QUALITE".equals(_colonne)) {
            return this._ordre
                    * (objet1.getQualite().compareToIgnoreCase(objet2
                            .getQualite()));
        } else if ("TYPEAPPELANT".equals(_colonne)) {
            return this._ordre
                    * (objet1.getTypeAppelant().compareToIgnoreCase(objet2
                            .getTypeAppelant()));
        } else if ("ETABLISSEMENTRS".equals(_colonne)) {
            return this._ordre
                    * (objet1.getEtablissementRS().compareToIgnoreCase(objet2
                            .getEtablissementRS()));
        } else if ("NUMEROSECU".equals(_colonne)) {
            return this._ordre
                    * (objet1.getNumeroSecu().compareToIgnoreCase(objet2
                            .getNumeroSecu()));
        } else if ("NUMEROSIRET".equals(_colonne)) {
            return this._ordre
                    * (objet1.getNumeroSiret().compareToIgnoreCase(objet2
                            .getNumeroSiret()));
        } else if ("NUMEROFINESS".equals(_colonne)) {
            return this._ordre
                    * (objet1.getNumeroFiness().compareToIgnoreCase(objet2
                            .getNumeroFiness()));
        } else if ("RADIE".equals(_colonne)) {
            return this._ordre
                    * (objet1.getRadie().compareToIgnoreCase(objet2.getRadie()));
        } else if ("OUTILGESTION".equals(_colonne)) {
            return this._ordre
                    * (objet1.getOutilGestion().compareToIgnoreCase(objet2
                            .getOutilGestion()));
        } else if ("CODEENTREPRISE".equals(_colonne)) {
            return this._ordre
                    * (objet1.getCodeEntreprise().compareToIgnoreCase(objet2
                            .getCodeEntreprise()));
        } else if ("CODEETABLISSEMENT".equals(_colonne)) {
            return this._ordre
                    * (objet1.getCodeEtablissement().compareToIgnoreCase(objet2
                            .getCodeEtablissement()));
        } else {
            LOGGER.warn("Colonne non trouvée " + this._colonne);
            return 1;
        }

    }

}
