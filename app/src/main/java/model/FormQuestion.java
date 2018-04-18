package model;

import java.io.Serializable;

/**
 * Created by Tiago on 28/10/17.
 */

public class FormQuestion implements Serializable {
    private String idInServer, category,headerText;
    private FormOption selectedOption;

    public FormQuestion(String idInServer, String category, String headerText) {
        this.idInServer = idInServer;
        this.category = category;
        this.headerText = headerText;
        selectedOption = null;
    }

    public String getIdInServer() {
        return idInServer;
    }

    public void setIdInServer(String idInServer) {
        this.idInServer = idInServer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public FormOption getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(FormOption selectedOption) {
        this.selectedOption = selectedOption;
    }
}
