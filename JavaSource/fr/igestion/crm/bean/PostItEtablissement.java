package fr.igestion.crm.bean;


public class PostItEtablissement extends PostIt{

    private String ETABLISSEMENT_ID = "";

    public PostItEtablissement() {
    }

    public String getETABLISSEMENT_ID() {
        return ETABLISSEMENT_ID;
    }

    public void setETABLISSEMENT_ID(String etablissement_id) {
        ETABLISSEMENT_ID = etablissement_id;
    }

}
