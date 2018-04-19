package fr.igestion.crm.bean.contrat;

import java.util.Collection;
import java.util.Date;

public class DetailContrat {

    private String id;
    private String numeroContrat;
    private String codeGroupeAssures;
    private String libelleGroupeAssures;
    private Date dateSouscription;
    private Date dateRadiation;
    private Collection<Couverture> couvertures;

    public DetailContrat() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeroContrat() {
        return numeroContrat;
    }

    public void setNumeroContrat(String numeroContrat) {
        this.numeroContrat = numeroContrat;
    }

    public String getCodeGroupeAssures() {
        return codeGroupeAssures;
    }

    public void setCodeGroupeAssures(String codeGroupeAssures) {
        this.codeGroupeAssures = codeGroupeAssures;
    }

    public String getLibelleGroupeAssures() {
        return libelleGroupeAssures;
    }

    public void setLibelleGroupeAssures(String libelleGroupeAssures) {
        this.libelleGroupeAssures = libelleGroupeAssures;
    }

    public Date getDateSouscription() {
        return dateSouscription;
    }

    public void setDateSouscription(Date dateSouscription) {
        this.dateSouscription = dateSouscription;
    }

    public Date getDateRadiation() {
        return dateRadiation;
    }

    public void setDateRadiation(Date dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

    public Collection<Couverture> getCouvertures() {
        return couvertures;
    }

    public void setCouvertures(Collection<Couverture> couvertures) {
        this.couvertures = couvertures;
    }

}
