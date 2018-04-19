package fr.igestion.crm.bean.scenario;

import java.util.Collection;

import fr.igestion.crm.bean.IdLibelleCode;
import fr.igestion.crm.bean.ModeleProcedureMail;
import fr.igestion.crm.bean.pec.ModelePEC;

public class ItemScenario extends IdLibelleCode{
    
    private String CONSIGNES = "";
    private String DISCOURS = "";
    private String REPONSE = "";
    private String REGIME_CODE = "";
    private String ACTIF = "";
    private String decisionnel = "";
    private ModelePEC pec = null;
    private Collection<ModeleProcedureMail> proceduresMail = null;
    private String mailResiliation = "";

    public ItemScenario() {
    }

    public String getREPONSE() {
        return REPONSE;
    }

    public void setREPONSE(String rEPONSE) {
        REPONSE = rEPONSE;
    }
    
    public Collection<ModeleProcedureMail> getProceduresMail() {
        return proceduresMail;
    }

    public void setProceduresMail(Collection<ModeleProcedureMail> procedureMail) {
        this.proceduresMail = procedureMail;
    }

    public ModelePEC getPec() {
        return pec;
    }

    public void setPec(ModelePEC pec) {
        this.pec = pec;
    }

    public String getCONSIGNES() {
        return CONSIGNES;
    }

    public void setCONSIGNES(String consignes) {
        CONSIGNES = consignes;
    }

    public String getDISCOURS() {
        return DISCOURS;
    }

    public void setDISCOURS(String discours) {
        DISCOURS = discours;
    }

    public String getREGIME_CODE() {
        return REGIME_CODE;
    }

    public void setREGIME_CODE(String rc) {
        REGIME_CODE = rc;
    }

    public String getACTIF() {
        return ACTIF;
    }

    public void setACTIF(String actif) {
        ACTIF = actif;
    }

    public String getDecisionnel() {
        return decisionnel;
    }

    public void setDecisionnel(String decisionnel) {
        this.decisionnel = decisionnel;
    }

    public String getMailResiliation() {
        return mailResiliation;
    }

    public void setMailResiliation(String mailResiliation) {
        this.mailResiliation = mailResiliation;
    }
    
}
