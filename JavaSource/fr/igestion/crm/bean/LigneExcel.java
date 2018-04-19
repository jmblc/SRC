package fr.igestion.crm.bean;

import java.util.Date;

public class LigneExcel {

    private String SITE;
    private String CLIENT;
    private String TYPEFICHE;
    private String TYPEAPPELANT;
    private String NOMAPPELANT;
    private String CODE_APPELANT;

    private Date DATE_APPEL;
    private Date DATE_CLOTURE;

    private String CREATEUR;
    private String CLOTUREUR;

    private String STATUT;
    private String IDFICHE;
    
    private String RESOLU;
    private String COMMENTAIRE;
    private String motif;
    private String sous_motif;
    private String lib_entite_gestion;
    
    private String campagne;
    
    private String nomben;
    private String codeben;
    private String qualite;

    public LigneExcel() {
    }
    
    public String getRESOLU() {
        return RESOLU;
    }

    public void setRESOLU(String rESOLU) {
        RESOLU = rESOLU;
    }

    public String getCOMMENTAIRE() {
        return COMMENTAIRE;
    }

    public void setCOMMENTAIRE(String cOMMENTAIRE) {
        COMMENTAIRE = cOMMENTAIRE;
    }

    public String getSITE() {
        return SITE;
    }

    public void setSITE(String sITE) {
        SITE = sITE;
    }

    public String getCLIENT() {
        return CLIENT;
    }

    public void setCLIENT(String cLIENT) {
        CLIENT = cLIENT;
    }

    public String getTYPEFICHE() {
        return TYPEFICHE;
    }

    public void setTYPEFICHE(String tYPEFICHE) {
        TYPEFICHE = tYPEFICHE;
    }

    public String getTYPEAPPELANT() {
        return TYPEAPPELANT;
    }

    public void setTYPEAPPELANT(String tYPEAPPELANT) {
        TYPEAPPELANT = tYPEAPPELANT;
    }

    public String getNOMAPPELANT() {
    	
    	if (NOMAPPELANT != null && !NOMAPPELANT.equals(nomben)) {
    		return NOMAPPELANT;
    	} else {
    		return "";
    	}
    }

    public void setNOMAPPELANT(String nOMAPPELANT) {
        NOMAPPELANT = nOMAPPELANT;
    }

    public String getCODE_APPELANT() {
    	if (CODE_APPELANT != null) {
    		if (CODE_APPELANT.equals(codeben)) {
    			return "-";
    		}
    		return CODE_APPELANT;
    	}
   		return "";
    }

    public void setCODE_APPELANT(String cODEAPPELANT) {
        CODE_APPELANT = cODEAPPELANT;
    }

    public Date getDATE_APPEL() {
        return DATE_APPEL;
    }

    public void setDATE_APPEL(Date dATEAPPEL) {
        DATE_APPEL = dATEAPPEL;
    }

    public Date getDATE_CLOTURE() {
        return DATE_CLOTURE;
    }

    public void setDATE_CLOTURE(Date dATECLOTURE) {
        DATE_CLOTURE = dATECLOTURE;
    }

    public String getCREATEUR() {
        return CREATEUR;
    }

    public void setCREATEUR(String cREATEUR) {
        CREATEUR = cREATEUR;
    }

    public String getCLOTUREUR() {
        return CLOTUREUR;
    }

    public void setCLOTUREUR(String cLOTUREUR) {
        CLOTUREUR = cLOTUREUR;
    }

    public String getSTATUT() {
        return STATUT;
    }

    public void setSTATUT(String sTATUT) {
        STATUT = sTATUT;
    }

    public String getIDFICHE() {
        return IDFICHE;
    }

    public void setIDFICHE(String iDFICHE) {
        IDFICHE = iDFICHE;
    }

	public String getSous_motif() {
		return sous_motif;
	}

	public void setSous_motif(String sous_motif) {
		this.sous_motif = sous_motif;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public String getLib_entite_gestion() {
		return lib_entite_gestion;
	}

	public void setLib_entite_gestion(String lib_entite_gestion) {
		this.lib_entite_gestion = lib_entite_gestion;
	}

	/**
	 * @return the campagne
	 */
	public String getCampagne() {
		return campagne;
	}

	/**
	 * @param campagne the campagne to set
	 */
	public void setCampagne(String campagne) {
		this.campagne = campagne;
	}

	/**
	 * @return the nomben
	 */
	public String getNomben() {
		return nomben;
	}

	/**
	 * @param nomben the nomben to set
	 */
	public void setNomben(String nomben) {
		this.nomben = nomben;
	}

	/**
	 * @return the codeben
	 */
	public String getCodeben() {
		return codeben;
	}

	/**
	 * @param codeben the codeben to set
	 */
	public void setCodeben(String codeben) {
		this.codeben = codeben;
	}

	/**
	 * @return the qualite
	 */
	public String getQualite() {
		return qualite;
	}

	/**
	 * @param qualite the qualite to set
	 */
	public void setQualite(String qualite) {
		this.qualite = qualite;
	}

}