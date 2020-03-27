package com.connect.mssql.Variables;

public class Global {
    private String csmNo;
    private String artNo;
    private String ordLineNo;

    public Global() {

    }
    public String getCsmNo() {
        return csmNo;
    }
    public String getArtNo() {
        return artNo;
    }
    public String getOrdLineNo() {
        return ordLineNo;
    }
    public void setCsmNo(String csmNo) {
        this.csmNo = csmNo;
    }
    public void setArtNo(String artNo) {
        this.artNo = artNo;
    }
    public void setOrdLineNo(String ordLineNo) {
        this.ordLineNo = ordLineNo;
    }
}
