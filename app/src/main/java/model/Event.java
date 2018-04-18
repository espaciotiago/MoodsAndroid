package model;

import java.io.Serializable;

/**
 * Created by Tiago on 26/10/17.
 */

public class Event implements Serializable{

    private String idInServer;
    private String label;
    private String extraText;
    private boolean checked;

    public Event(String idInServer, String label) {
        this.idInServer = idInServer;
        this.label = label;
        extraText = "";
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getExtraText() {
        return extraText;
    }

    public void setExtraText(String extraText) {
        this.extraText = extraText;
    }
}
