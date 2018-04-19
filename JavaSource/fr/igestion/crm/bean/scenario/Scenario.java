package fr.igestion.crm.bean.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.igestion.crm.bean.ModeleProcedureMail;
import fr.igestion.crm.bean.pec.ModelePEC;

public class Scenario {
   
    private String ID = "";
    private String LIBELLE = "";
    private String CONSIGNES = "";
    private String DISCOURS = "";
    private String MUTUELLE_ID = "";
    private String CAMPAGNE_ID = "";
    private String REPONSE = "";
    private Collection<Motif> motifs = new ArrayList<Motif>();
    private Collection<ModeleProcedureMail> proceduresMail = null;
    private Collection<ModelePEC> modelesPEC = null;
    private Map<ModelePEC,Object> rattachementsPEC = new HashMap<ModelePEC,Object>();
    
    public Scenario() {
    }
    
    public Collection<ModeleProcedureMail> getProceduresMail() {
        return proceduresMail;
    }

    public void setProceduresMail(Collection<ModeleProcedureMail> proceduresMail) {
        this.proceduresMail = proceduresMail;
    }

    public Collection<ModelePEC> getModelesPEC() {
        return modelesPEC;
    }

    public void setModelesPEC(Collection<ModelePEC> modelesPEC) {
        this.modelesPEC = modelesPEC;
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

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getREPONSE() {
        return REPONSE;
    }

    public void setREPONSE(String rEPONSE) {
        REPONSE = rEPONSE;
    }

    public String getMUTUELLE_ID() {
        return MUTUELLE_ID;
    }

    public void setMUTUELLE_ID(String mUTUELLEID) {
        MUTUELLE_ID = mUTUELLEID;
    }

    public String getCAMPAGNE_ID() {
        return CAMPAGNE_ID;
    }

    public void setCAMPAGNE_ID(String cAMPAGNEID) {
        CAMPAGNE_ID = cAMPAGNEID;
    }

    public Collection<Motif> getMotifs() {
        return motifs;
    }

    public void setMotifs(Collection<Motif> motifs) {
        this.motifs = motifs;
    }
    
    public String getLIBELLE() {
        return LIBELLE;
    }

    public void setLIBELLE(String libelle) {
        LIBELLE = libelle;
    }

    public Object getPEC_RATTACHEMENT(ModelePEC unModelePEC){
        return rattachementsPEC.get(unModelePEC);
    }

    public void addRattachement( ModelePEC unModelePEC, Object item){
        rattachementsPEC.put(unModelePEC, item);
    }
    
}
