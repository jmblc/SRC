package fr.igestion.crm.bean;

public class Transfert {

    String TRA_ID;
    String TRA_LIBELLE;
    String TRA_EMAIL;

    public Transfert() {

    }

    public String getTRA_ID() {
        return TRA_ID;
    }

    public void setTRA_ID(String tRAID) {
        TRA_ID = tRAID;
    }

    public String getTRA_LIBELLE() {
        return TRA_LIBELLE;
    }

    public void setTRA_LIBELLE(String tRALIBELLE) {
        TRA_LIBELLE = tRALIBELLE;
    }

    public String getTRA_EMAIL() {
        return TRA_EMAIL;
    }

    public void setTRA_EMAIL(String tRAEMAIL) {
        TRA_EMAIL = tRAEMAIL;
    }

}
