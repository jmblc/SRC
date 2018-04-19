package fr.igestion.crm.bean.pec;

public class ModelePEC {

    private String id;
    private String libelle;

    private String operateur;
    private String organisme;

    private Boolean emissionFax;
    private String fax;
    private Boolean emissionCourriel;
    private String courriel;

    private Boolean appelantBeneficiairePermis;
    private Boolean appelantFournisseurPermis;

    @Override
    public int hashCode() {
        return Long.valueOf(id).intValue();
    }
    
    @Override
    public boolean equals(Object obj) {
        if( obj instanceof ModelePEC && ((ModelePEC)obj).getId().equals(this.id) ){
            return true;
        }
        else{
            return false;
        }
    }
    
    public Boolean getAppelantBeneficiairePermis() {
        return appelantBeneficiairePermis;
    }

    public void setAppelantBeneficiairePermis(Boolean beneficiairePermis) {
        this.appelantBeneficiairePermis = beneficiairePermis;
    }

    public Boolean getAppelantFournisseurPermis() {
        return appelantFournisseurPermis;
    }

    public void setAppelantFournisseurPermis(Boolean fournisseurPermis) {
        this.appelantFournisseurPermis = fournisseurPermis;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean getEmissionFax() {
        return emissionFax;
    }

    public void setEmissionFax(Boolean emissionFax) {
        this.emissionFax = emissionFax;
    }

    public Boolean getEmissionCourriel() {
        return emissionCourriel;
    }

    public void setEmissionCourriel(Boolean emissionCourriel) {
        this.emissionCourriel = emissionCourriel;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

}
