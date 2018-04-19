package fr.igestion.crm.bean;

public class InfosBDD {

    private String version = "";
    private String base = "";
    private String instance = "";

    public InfosBDD() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

}
