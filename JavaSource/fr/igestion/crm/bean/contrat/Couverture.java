package fr.igestion.crm.bean.contrat;

import java.util.Date;

public class Couverture {

    private String codeRisque;
    private String codeRisqueOption;
    private Date dateSouscription;
    private Date dateRadiation;

    public Couverture() {

    }

    public String getCodeRisqueOption() {
        return codeRisqueOption;
    }

    public void setCodeRisqueOption(String codeRisqueOption) {
        this.codeRisqueOption = codeRisqueOption;
    }

    public Date getDateSouscription() {
        return dateSouscription;
    }

    public void setDateSouscription(Date dateSouscription) {
        this.dateSouscription = dateSouscription;
    }

    public String getCodeRisque() {
        return codeRisque;
    }

    public void setCodeRisque(String codeRisque) {
        this.codeRisque = codeRisque;
    }

    public Date getDateRadiation() {
        return dateRadiation;
    }

    public void setDateRadiation(Date dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

}
