package fr.igestion.crm.bean.contrat;

import java.util.Collection;
import java.util.Date;

import fr.igestion.crm.bean.GarantieRecherche;

public class Contrat {

    private String typeContrat;
    private String codeRT;
    private String libelleRT = "";
    private Date dateSouscription;
    private Date dateRadiation;
    private Date dateFinDroit;
    private Date dateDebutDroit;
    private boolean hasBlob = false;
    private String idGarantie;
    private String idMutuelle;
    private Collection<GarantieRecherche> garanties;
    private String modePaiement;
    private String frequenceAppel;
    private String numero_contrat;
    private String courtier;
    private String contentieux;
    private String contentieux_raison;
    private Date contentieux_date;
    private String info_contrat_collectif;
    
    public Contrat() {
    }
    
    public String getFrequenceAppel() {
        return frequenceAppel;
    }
    public void setFrequenceAppel(String frequenceAppel) {
        this.frequenceAppel = frequenceAppel;
    }

    public String getModePaiement() {
        return modePaiement;
    }
    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }
   
    public String getIdMutuelle() {
        return idMutuelle;
    }

    public void setIdMutuelle(String idMutuelle) {
        this.idMutuelle = idMutuelle;
    }

    public String getCodeRT() {
        return codeRT;
    }
    public void setCodeRT(String codeRT) {
        this.codeRT = codeRT;
    }

    public String getIdGarantie() {
        return idGarantie;
    }
    public void setIdGarantie(String idGarantie) {
        this.idGarantie = idGarantie;
    }

    public Date getDateDedutDroit() {
        return dateDebutDroit;
    }
    public void setDateDedutDroit(Date dateDedutDroit) {
        this.dateDebutDroit = dateDedutDroit;
    }

    public Date getDateFinDroit() {
        return dateFinDroit;
    }
    public void setDateFinDroit(Date dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public Date getDateRadiation() {
        return dateRadiation;
    }
    public void setDateRadiation(Date dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

    public Date getDateSouscription() {
        return dateSouscription;
    }
    public void setDateSouscription(Date dateSouscription) {
        this.dateSouscription = dateSouscription;
    }

    public String getLibelleRT() {
        return libelleRT;
    }
    public void setLibelleRT(String libelleRT) {
        this.libelleRT = libelleRT;
    }

    public String getTypeContrat() {
        return typeContrat;
    }
    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
    }

    public boolean isHasBlob() {
        return hasBlob;
    }
    public void setHasBlob(boolean hasBlob) {
        this.hasBlob = hasBlob;
    }

    public String getNumeroContrat() {
        return numero_contrat;
    }
    public void setNumeroContrat(String numero_contrat) {
        this.numero_contrat = numero_contrat;
    }

    public String getCourtier() {
        return courtier;
    }
    public void setCourtier(String courtier) {
        this.courtier = courtier;
    }

    public String getContentieux() {
        return contentieux;
    }
    public void setContentieux(String contentieux) {
        this.contentieux = contentieux;
    }

    public String getContentieuxRaison() {
        return contentieux_raison;
    }
    public void setContentieuxRaison(String contentieuxRaison) {
        contentieux_raison = contentieuxRaison;
    }

    public Date getContentieuxDate() {
        return contentieux_date;
    }
    public void setContentieuxDate(Date contentieuxDate) {
        contentieux_date = contentieuxDate;
    }

    public Collection<GarantieRecherche> getGaranties() {
        return garanties;
    }
    public void setGaranties(Collection<GarantieRecherche> garanties) {
        this.garanties = garanties;
    }
    
    public String getInfoContraCollectif() {
        return info_contrat_collectif;
    }

    public void setInfoContraCollectif(String infoContratCollectif) {
        info_contrat_collectif = infoContratCollectif;
    }

    
}
