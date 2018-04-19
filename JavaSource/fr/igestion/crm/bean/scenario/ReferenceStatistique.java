package fr.igestion.crm.bean.scenario;

import java.util.Collection;

public class ReferenceStatistique {

    private String id = "";
    private String libelle = "";
    private String actif = "";
    private String rstId = "";

    public ReferenceStatistique() {
    }
    
    public static ReferenceStatistique getById(Collection<ReferenceStatistique> liste, String id) {
    	
    	ReferenceStatistique result = null;
    	for(ReferenceStatistique ref : liste) {
    		if (id.equals(ref.getId())) {
    			result = ref;
    			break;
    		}
    	}
    	return result;
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }
    
    public String getRstId() {
        return rstId;
    }

    public void setRstId(String rstId) {
        this.rstId = rstId;
    }

}
