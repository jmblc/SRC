package fr.igestion.crm.bean.pec;

public class InfoPec extends BasePEC{
    
    private String createur;
    private String nom_prenom_adherent;
    private String numSS_adherent;
    private String cleSS_adherent;
    private String nom_prenom_beneficiaire;
    private String numSS_beneficiaire;
    private String cleSS_beneficiaire;
    private String etablissementRS_appelant;
    private String numFiness_appelant;
    private String typeEtablissement;
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
    private String nbrJourLimiteForfaitJournalier;
    private String plafondChambre;
    private String limiteChambre;
    private String precision;
    private String precisionAutre;
    private String adresseEnvoiMail;
    private String dateCreation;

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

    public String getTypeEtablissement() {
        return typeEtablissement;
    }

    public void setTypeEtablissement(String typeEtablissement) {
        if (BasePEC._TYPE_ETAB_PUBLIC
                .equalsIgnoreCase(typeEtablissement)){
            this.typeEtablissement = "0";
        } else if (BasePEC._TYPE_ETAB_PRIVE
                .equalsIgnoreCase(typeEtablissement)) {
            this.typeEtablissement = "1";
        } else {
            this.typeEtablissement = typeEtablissement;
        }    
        this.typeEtablissement = (this.typeEtablissement != null) ? this.typeEtablissement
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

    public String getNbrJourLimiteForfaitJournalier() {
        return nbrJourLimiteForfaitJournalier;
    }

    public void setNbrJourLimiteForfaitJournalier(
            String nbrJourLimiteForfaitJournalier) {
        this.nbrJourLimiteForfaitJournalier = (nbrJourLimiteForfaitJournalier != null) ? nbrJourLimiteForfaitJournalier
                : "";
    }

    public String getPlafondChambre() {
        return plafondChambre;
    }

    public void setPlafondChambre(String plafondChambre) {
        this.plafondChambre = (plafondChambre != null) ? plafondChambre : "";
    }

    public String getLimiteChambre() {
        return limiteChambre;
    }

    public void setLimiteChambre(String limiteChambre) {
        this.limiteChambre = (limiteChambre != null) ? limiteChambre : "";
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = (precision != null) ? precision : "";
    }

    public String getPrecisionAutre() {
        return precisionAutre;
    }

    public void setPrecisionAutre(String precisionAutre) {
        this.precisionAutre = (precisionAutre != null) ? precisionAutre : "";
    }

    public String getAdresseEnvoiMail() {
        if ("OK".equalsIgnoreCase(this.getEnvoiMail())){
            return adresseEnvoiMail;
        } else {
            return "";
        }    
    }

    public void setAdresseEnvoiMail(String adresseEnvoiMail) {
        this.adresseEnvoiMail = (adresseEnvoiMail != null) ? adresseEnvoiMail
                : "";
    }


}
