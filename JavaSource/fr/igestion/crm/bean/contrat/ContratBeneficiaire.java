package fr.igestion.crm.bean.contrat;

import java.util.Collection;

public class ContratBeneficiaire extends Contrat{

    private String idBeneficiaire;
    private String idContratAdherent;
    private String idContratBeneficiaire;
    
    private Collection<DetailContratBeneficiaire> detailsContrat;
    
    public ContratBeneficiaire() {
    }

    public String getIdContratAdherent() {
        return idContratAdherent;
    }
    public void setIdContratAdherent(String idContratAdherent) {
        this.idContratAdherent = idContratAdherent;
    }

    public Collection<DetailContratBeneficiaire> getDetailsContrat() {
        return detailsContrat;
    }

    public void setDetailsContrat(
            Collection<DetailContratBeneficiaire> detailsContrat) {
        this.detailsContrat= detailsContrat;
    }

    public String getIdBeneficiaire() {
        return idBeneficiaire;
    }
    public void setIdBeneficiaire(String idBeneficiaire) {
        this.idBeneficiaire = idBeneficiaire;
    }

    public String getIdContratBeneficiaire() {
        return idContratBeneficiaire;
    }
    public void setIdContratBeneficiaire(String idContratBeneficiaire) {
        this.idContratBeneficiaire = idContratBeneficiaire;
    }

}
