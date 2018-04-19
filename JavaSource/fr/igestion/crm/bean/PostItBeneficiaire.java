package fr.igestion.crm.bean;

public class PostItBeneficiaire extends PostIt{

    private String BENEFICIAIRE_ID = "";
    
    public PostItBeneficiaire() {
    }

    public String getBENEFICIAIRE_ID() {
        return BENEFICIAIRE_ID;
    }

    public void setBENEFICIAIRE_ID(String beneficiaire_id) {
        BENEFICIAIRE_ID = beneficiaire_id;
    }

}
