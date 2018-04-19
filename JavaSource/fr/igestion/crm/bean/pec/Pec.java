package fr.igestion.crm.bean.pec;

import java.sql.Timestamp;
import java.util.Date;

public class Pec extends BasePEC{
    
    private String adresseMailEnvoi;
    private String autreFrais;
    private String chambreParticuliereLimiteJours;
    private String chambreParticulierePlafondJournalier;
    private Date dateEntreeHospitalisation;
    private String forfaitJournalierLimiteJours;
    private String hospitalisation;
    private String idAdherent;
    private String idAdherentHorsBase;
    private String idAdresse;
    private String idAppel;
    private String idBeneficiaire;
    private String idCreateur;
    private String idEntiteGestion;
    private String idEtablissement;
    private String idMutuelle;
    private String idPersonne;
    private String idProfessionnelSante;
    private String media;
    private String motif_id;
    private String numeroEntreeHospitalisation;
    private String numFiness;
    private String raisonSociale;
    private String sous_motif_id;
    private String statut_id;
    private String type;
    private String typeEtablissement;
    private String urgence;
    private String codePostalDestinataire;
    private String ligne_1Destinataire;
    private String ligne_2Destinataire;
    private String ligne_3Destinataire;
    private String localiteDestinataire;
    private String telecopieDestinataire;
    private String telephone_fixeDestinataire;

    private String nomBeneficiaireHorsBase;
    private String prenomBeneficiaireHorsBase;
    private String numSecuBeneficiaireHorsBase;
    private String cleSecuBeneficiaireHorsBase;

    private String nomAdherentHorsBase;
    private String prenomAdherentHorsBase;
    private String numSecuAdherentHorsBase;
    private String cleSecuAdherentHorsBase;
    private Date dateNaissance;
    private Timestamp dateCreation;

    public Pec() {
    }
    
    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public String getNomAdherentHorsBase() {
        return nomAdherentHorsBase;
    }

    public void setNomAdherentHorsBase(String nomAdherentHorsBase) {
        this.nomAdherentHorsBase = nomAdherentHorsBase;
    }

    public String getPrenomAdherentHorsBase() {
        return prenomAdherentHorsBase;
    }

    public void setPrenomAdherentHorsBase(String prenomAdherentHorsBase) {
        this.prenomAdherentHorsBase = prenomAdherentHorsBase;
    }

    public String getNumSecuAdherentHorsBase() {
        return numSecuAdherentHorsBase;
    }

    public void setNumSecuAdherentHorsBase(String numSecuAdherentHorsBase) {
        this.numSecuAdherentHorsBase = numSecuAdherentHorsBase;
    }

    public String getCleSecuAdherentHorsBase() {
        return cleSecuAdherentHorsBase;
    }

    public void setCleSecuAdherentHorsBase(String cleSecuAdherentHorsBase) {
        this.cleSecuAdherentHorsBase = cleSecuAdherentHorsBase;
    }

    public String getAdresseMailEnvoi() {
        return adresseMailEnvoi;
    }

    public void setAdresseMailEnvoi(String adresseMailEnvoi) {
        this.adresseMailEnvoi = adresseMailEnvoi;
    }

    public String getAutreFrais() {
        return autreFrais;
    }

    public void setAutreFrais(String autreFrais) {
        this.autreFrais = autreFrais;
    }

    public String getChambreParticuliereLimiteJours() {
        return chambreParticuliereLimiteJours;
    }

    public void setChambreParticuliereLimiteJours(
            String chambreParticuliereLimiteJours) {
        this.chambreParticuliereLimiteJours = chambreParticuliereLimiteJours;
    }

    public String getChambreParticulierePlafondJournalier() {
        return chambreParticulierePlafondJournalier;
    }

    public void setChambreParticulierePlafondJournalier(
            String chambreParticulierePlafondJournalier) {
        this.chambreParticulierePlafondJournalier = chambreParticulierePlafondJournalier;
    }

    public Date getDateEntreeHospitalisation() {
        return dateEntreeHospitalisation;
    }

    public void setDateEntreeHospitalisation(Date dateEntreeHospitalisation) {
        this.dateEntreeHospitalisation = dateEntreeHospitalisation;
    }

    public String getForfaitJournalierLimiteJours() {
        return forfaitJournalierLimiteJours;
    }

    public void setForfaitJournalierLimiteJours(
            String forfaitJournalierLimiteJours) {
        this.forfaitJournalierLimiteJours = forfaitJournalierLimiteJours;
    }

    public String getHospitalisation() {
        return hospitalisation;
    }

    public void setHospitalisation(String hospitalisation) {
        this.hospitalisation = hospitalisation;
    }

    public String getIdAdherent() {
        return idAdherent;
    }

    public void setIdAdherent(String idAdherent) {
        this.idAdherent = idAdherent;
    }

    public String getIdAdherentHorsBase() {
        return idAdherentHorsBase;
    }

    public void setIdAdherentHorsBase(String idAdherentHorsBase) {
        this.idAdherentHorsBase = idAdherentHorsBase;
    }

    public String getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(String idAdresse) {
        this.idAdresse = idAdresse;
    }

    public String getIdAppel() {
        return idAppel;
    }

    public void setIdAppel(String idAppel) {
        this.idAppel = idAppel;
    }

    public String getIdBeneficiaire() {
        return idBeneficiaire;
    }

    public void setIdBeneficiaire(String idBeneficiaire) {
        this.idBeneficiaire = idBeneficiaire;
    }

    public String getIdCreateur() {
        return idCreateur;
    }

    public void setIdCreateur(String idCreateur) {
        this.idCreateur = idCreateur;
    }

    public String getIdEntiteGestion() {
        return idEntiteGestion;
    }

    public void setIdEntiteGestion(String idEntiteGestion) {
        this.idEntiteGestion = idEntiteGestion;
    }

    public String getIdEtablissement() {
        return idEtablissement;
    }

    public void setIdEtablissement(String idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    public String getIdMutuelle() {
        return idMutuelle;
    }

    public void setIdMutuelle(String idMutuelle) {
        this.idMutuelle = idMutuelle;
    }

    public String getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
    }

    public String getIdProfessionnelSante() {
        return idProfessionnelSante;
    }

    public void setIdProfessionnelSante(String idProfessionnelSante) {
        this.idProfessionnelSante = idProfessionnelSante;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getMotif_id() {
        return motif_id;
    }

    public void setMotif_id(String motif_id) {
        this.motif_id = motif_id;
    }

    public String getNumeroEntreeHospitalisation() {
        return numeroEntreeHospitalisation;
    }

    public void setNumeroEntreeHospitalisation(
            String numeroEntreeHospitalisation) {
        this.numeroEntreeHospitalisation = numeroEntreeHospitalisation;
    }

    public String getNumFiness() {
        return numFiness;
    }

    public void setNumFiness(String numFiness) {
        this.numFiness = numFiness;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getSous_motif_id() {
        return sous_motif_id;
    }

    public void setSous_motif_id(String sous_motif_id) {
        this.sous_motif_id = sous_motif_id;
    }

    public String getStatut_id() {
        return statut_id;
    }

    public void setStatut_id(String statut_id) {
        this.statut_id = statut_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeEtablissement() {
        return typeEtablissement;
    }

    public void setTypeEtablissement(String typeEtablissement) {
        this.typeEtablissement = typeEtablissement;
    }

    public String getUrgence() {
        return urgence;
    }

    public void setUrgence(String urgence) {
        this.urgence = urgence;
    }

    public String getCleSecuBeneficiaireHorsBase() {
        return cleSecuBeneficiaireHorsBase;
    }

    public void setCleSecuBeneficiaireHorsBase(
            String cleSecuBeneficiaireHorsBase) {
        this.cleSecuBeneficiaireHorsBase = cleSecuBeneficiaireHorsBase;
    }

    public String getCodePostalDestinataire() {
        return codePostalDestinataire;
    }

    public void setCodePostalDestinataire(String codePostalDestinataire) {
        this.codePostalDestinataire = codePostalDestinataire;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLigne_1Destinataire() {
        return ligne_1Destinataire;
    }

    public void setLigne_1Destinataire(String ligne_1Destinataire) {
        this.ligne_1Destinataire = ligne_1Destinataire;
    }

    public String getLigne_2Destinataire() {
        return ligne_2Destinataire;
    }

    public void setLigne_2Destinataire(String ligne_2Destinataire) {
        this.ligne_2Destinataire = ligne_2Destinataire;
    }

    public String getLigne_3Destinataire() {
        return ligne_3Destinataire;
    }

    public void setLigne_3Destinataire(String ligne_3Destinataire) {
        this.ligne_3Destinataire = ligne_3Destinataire;
    }

    public String getLocaliteDestinataire() {
        return localiteDestinataire;
    }

    public void setLocaliteDestinataire(String localiteDestinataire) {
        this.localiteDestinataire = localiteDestinataire;
    }

    public String getNomBeneficiaireHorsBase() {
        return nomBeneficiaireHorsBase;
    }

    public void setNomBeneficiaireHorsBase(String nomBeneficiaireHorsBase) {
        this.nomBeneficiaireHorsBase = nomBeneficiaireHorsBase;
    }

    public String getNumSecuBeneficiaireHorsBase() {
        return numSecuBeneficiaireHorsBase;
    }

    public void setNumSecuBeneficiaireHorsBase(
            String numSecuBeneficiaireHorsBase) {
        this.numSecuBeneficiaireHorsBase = numSecuBeneficiaireHorsBase;
    }

    public String getPrenomBeneficiaireHorsBase() {
        return prenomBeneficiaireHorsBase;
    }

    public void setPrenomBeneficiaireHorsBase(String prenomBeneficiaireHorsBase) {
        this.prenomBeneficiaireHorsBase = prenomBeneficiaireHorsBase;
    }

    public String getTelecopieDestinataire() {
        return telecopieDestinataire;
    }

    public void setTelecopieDestinataire(String telecopieDestinataire) {
        this.telecopieDestinataire = telecopieDestinataire;
    }

    public String getTelephone_fixeDestinataire() {
        return telephone_fixeDestinataire;
    }

    public void setTelephone_fixeDestinataire(String telephone_fixeDestinataire) {
        this.telephone_fixeDestinataire = telephone_fixeDestinataire;
    }

}
