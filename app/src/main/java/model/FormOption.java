package model;

import java.io.Serializable;

/**
 * Created by Tiago on 28/10/17.
 */

public class FormOption implements Serializable{
    private String idInServer,label;
    private boolean checked;
    private int value;

    public FormOption(String idInServer, String label, int value) {
        this.idInServer = idInServer;
        this.label = label;
        this.value = value;
        checked = false;
    }

    public String getIdInServer() {
        return idInServer;
    }

    public void setIdInServer(String idInServer) {
        this.idInServer = idInServer;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
