package fr.igestion.crm.bean.contrat;

import java.util.Collection;

public class ContratEtablissement extends Contrat {

    private String idEtablissement;
    private String idContratEtablissement;
    private Collection<DetailContratEtablissement> detailsContrat;
 
    public ContratEtablissement() {
    }

    public String getIdContratEtablissement() {
        return idContratEtablissement;
    }

    public void setIdContratEtablissement(String idContratEtablissement) {
        this.idContratEtablissement = idContratEtablissement;
    }

    
    public Collection<DetailContratEtablissement> getDetailsContrat(){
        return detailsContrat;
    }
    public void setDetailsContrat(
            Collection<DetailContratEtablissement> detailsContrat) {
        this.detailsContrat = detailsContrat;
    }

    public String getIdEtablissement() {
        return idEtablissement;
    }

    public void setIdEtablissement(String idEtablissement) {
        this.idEtablissement = idEtablissement;
    }
  
}
