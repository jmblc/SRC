package fr.igestion.crm.bean.contrat;

import java.util.Date;

public class Prestation {

    private String beneficiaire = "";
    private String libelle = "";
    private String code_acte = "";
    private String destpaiement = "";

    private Date date_soin = null;
    private Date date_remboursement = null;

    private float depense;
    private float remboursement_secu;
    private float remboursement_mutuelle;

    public Prestation() {
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public String getCodeActe() {
        return code_acte;
    }

    public void setCodeActe(String code_acte) {
        this.code_acte = code_acte;
    }

    public Date getDateRemboursement() {
        return date_remboursement;
    }

    public void setDateRemboursement(Date date_remboursement) {
        this.date_remboursement = date_remboursement;
    }

    public Date getDateSoin() {
        return date_soin;
    }

    public void setDateSoin(Date date_soin) {
        this.date_soin = date_soin;
    }

    public float getDepense() {
        return depense;
    }

    public void setDepense(float depense) {
        this.depense = depense;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public float getRemboursementMutuelle() {
        return remboursement_mutuelle;
    }

    public void setRemboursementMutuelle(float remboursement_mutuelle) {
        this.remboursement_mutuelle = remboursement_mutuelle;
    }

    public float getRemboursementSecu() {
        return remboursement_secu;
    }

    public void setRemboursementSecu(float remboursement_secu) {
        this.remboursement_secu = remboursement_secu;
    }

    public String getDestPaiement() {
        return destpaiement;
    }

    public void setDestPaiement(String destpaiement) {
        this.destpaiement = destpaiement;
    }

}
