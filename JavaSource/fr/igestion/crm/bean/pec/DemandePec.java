package fr.igestion.crm.bean.pec;

import fr.igestion.crm.bean.LigneDVS;

public class DemandePec extends BasePEC{
    
    public static final String _medecine  = "2";
    public static final String _chirurgie = "1";
    public static final String _psychiatrie = "4";
    public static final String _maternite = "3";
    public static final String _maisonreposconvalescence = "5";
    
    public static final String _hospitalisationComplete = "1";
    public static final String _hospitalisationDeJour = "2";
    public static final String _hospitalisationAmbulatoire = "3";
    public static final String _hospitalisationDomicile = "4";
    
    
    private String createur;
    private String nom_prenom_adherent;
    private String numSS_adherent;
    private String cleSS_adherent;
    private String strDnai_adherent;
    private String nom_prenom_beneficiaire;
    private String numSS_beneficiaire;
    private String cleSS_beneficiaire;
    private String strDnai_beneficiaire;
    private String etablissementRS_appelant;
    private String numFiness_appelant;
    private String adresse1_appelant;
    private String adresse2_appelant;
    private String adresse3_appelant;
    private String codepostal_appelant;
    private String ville_appelant;
    private String tel_appelant_fixe;
    private String fax_appelant;
    private String strDateEntree;
    private String numEntree;
    private String typeHospitalisation;
    private String modeTraitementHospitalisation;
    private String codeDMT;
    private String commentaire;
    private String dateCreation;
    private String operateur;
    private String organisme;
    private String canal;
    private String adrOperateur;
    private String lotGED;
    private LigneDVS document;
    private String documentHisto;
   
    
    public String getDocumentHisto() {
        return documentHisto;
    }

    public void setDocumentHisto(String documentHisto) {
        this.documentHisto = documentHisto;
    }

    public String getLotGED() {
        return lotGED;
    }

    public void setLotGED(String lotGED) {
        this.lotGED = lotGED;
    }
    
    public void setDocument(LigneDVS document) {
        this.document = document;
    }

    public LigneDVS getDocument() {
        return document;
    }

    public String getStrDnai_adherent() {
        return strDnai_adherent;
    }

    public void setStrDnai_adherent(String dnai_adherent) {
        this.strDnai_adherent = dnai_adherent.substring(6, 8) + "/"
                + dnai_adherent.substring(4, 6) + "/"
                + dnai_adherent.substring(0, 4);
    }

    public String getStrDnai_beneficiaire() {
        return strDnai_beneficiaire;
    }

    public void setStrDnai_beneficiaire(String dnai_beneficiaire) {
        this.strDnai_beneficiaire = dnai_beneficiaire.substring(6, 8) + "/"
                + dnai_beneficiaire.substring(4, 6) + "/"
                + dnai_beneficiaire.substring(0, 4);
    }
    
    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }

    public String getOrganisme() {
        return organisme;
    }

    public void setOrganisme(String organisme) {
        this.organisme = organisme;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getAdrOperateur() {
        return adrOperateur;
    }

    public void setAdrOperateur(String adrOperateur) {
        this.adrOperateur = adrOperateur;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }

    public String getNom_prenom_adherent() {
        return nom_prenom_adherent;
    }

    public void setNom_prenom_adherent(String nom_prenom_adherent) {
        this.nom_prenom_adherent = nom_prenom_adherent;
    }

    public String getNumSS_adherent() {
        return numSS_adherent;
    }

    public void setNumSS_adherent(String numSS_adherent) {
        this.numSS_adherent = numSS_adherent;
    }

    public String getCleSS_adherent() {
        return cleSS_adherent;
    }

    public void setCleSS_adherent(String cleSS_adherent) {
        this.cleSS_adherent = cleSS_adherent;
    }

    public String getNom_prenom_beneficiaire() {
        return nom_prenom_beneficiaire;
    }

    public void setNom_prenom_beneficiaire(String nom_prenom_beneficiaire) {
        this.nom_prenom_beneficiaire = (nom_prenom_beneficiaire != null) ? nom_prenom_beneficiaire
                : "";
    }

    public String getNumSS_beneficiaire() {
        return numSS_beneficiaire;
    }

    public void setNumSS_beneficiaire(String numSS_beneficiaire) {
        this.numSS_beneficiaire = (numSS_beneficiaire != null) ? numSS_beneficiaire
                : "";
    }

    public String getCleSS_beneficiaire() {
        return cleSS_beneficiaire;
    }

    public void setCleSS_beneficiaire(String cleSS_beneficiaire) {
        this.cleSS_beneficiaire = (cleSS_beneficiaire != null) ? cleSS_beneficiaire
                : "";
    }

    public String getEtablissementRS_appelant() {
        return etablissementRS_appelant;
    }

    public void setEtablissementRS_appelant(String etablissementRS_appelant) {
        this.etablissementRS_appelant = (etablissementRS_appelant != null) ? etablissementRS_appelant
                : "";
    }

    public String getNumFiness_appelant() {
        return numFiness_appelant;
    }

    public void setNumFiness_appelant(String numFiness_appelant) {
        this.numFiness_appelant = (numFiness_appelant != null) ? numFiness_appelant
                : "";
    }

    public String getAdresse1_appelant() {
        return adresse1_appelant;
    }

    public void setAdresse1_appelant(String adresse1_appelant) {
        this.adresse1_appelant = (adresse1_appelant != null) ? adresse1_appelant
                : "";
    }

    public String getAdresse2_appelant() {
        return adresse2_appelant;
    }

    public void setAdresse2_appelant(String adresse2_appelant) {
        this.adresse2_appelant = (adresse2_appelant != null) ? adresse2_appelant
                : "";
    }

    public String getAdresse3_appelant() {
        return adresse3_appelant;
    }

    public void setAdresse3_appelant(String adresse3_appelant) {
        this.adresse3_appelant = (adresse3_appelant != null) ? adresse3_appelant
                : "";
    }

    public String getCodepostal_appelant() {
        return codepostal_appelant;
    }

    public void setCodepostal_appelant(String codepostal_appelant) {
        this.codepostal_appelant = codepostal_appelant;
    }

    public String getVille_appelant() {
        return ville_appelant;
    }

    public void setVille_appelant(String ville_appelant) {
        this.ville_appelant = ville_appelant;
    }

    public String getTel_appelant_fixe() {
        return tel_appelant_fixe;
    }

    public void setTel_appelant_fixe(String tel_appelant_fixe) {
        this.tel_appelant_fixe = (tel_appelant_fixe != null) ? tel_appelant_fixe
                : "";
    }

    public String getFax_appelant() {
        return fax_appelant;
    }

    public void setFax_appelant(String fax_appelant) {
        this.fax_appelant = (fax_appelant != null) ? fax_appelant : "";
    }

    public String getStrDateEntree() {
        return strDateEntree;
    }

    public void setStrDateEntree(String strDateEntree) {
        this.strDateEntree = strDateEntree.substring(6, 8) + "/"
                + strDateEntree.substring(4, 6) + "/"
                + strDateEntree.substring(0, 4);
    }

    public String getNumEntree() {
        return numEntree;
    }

    public void setNumEntree(String numEntree) {
        this.numEntree = (numEntree != null) ? numEntree : "";
    }
    
    public String getTypeHospitalisation() {
        return typeHospitalisation;
    }

    public void setTypeHospitalisation(String typeHospitalisation) {
        this.typeHospitalisation = typeHospitalisation;
    }

    public String getCodeDMT() {
        return codeDMT;
    }

    public void setCodeDMT(String codeDMT) {
        this.codeDMT = codeDMT;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getModeTraitementHospitalisation() {
        return modeTraitementHospitalisation;
    }

    public void setModeTraitementHospitalisation(
            String modeTraitementHospitalisation) {
        this.modeTraitementHospitalisation = modeTraitementHospitalisation;
    }

}
