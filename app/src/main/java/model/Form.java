package model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tiago on 28/10/17.
 */

public class Form implements Serializable{
    private String idInServer,name,closeDate,observations,openQuestionLabel;
    private boolean sended,alreadySended;
    private boolean openQuestionNeeded;
    private int totalQuestions;
    private List<FormQuestion> questions;

    public Form(String idInServer, String name, String closeDate, int totalQuestions,boolean openQuestionNeeded,List<FormQuestion> questions,String openQuestionLabel) {
        this.idInServer = idInServer;
        this.name = name;
        this.closeDate = closeDate;
        this.observations = "";
        this.totalQuestions = totalQuestions;
        this.questions = questions;
        this.openQuestionNeeded = openQuestionNeeded;
        sended = false;
        this.openQuestionLabel = openQuestionLabel;
        this.alreadySended = false;
    }

    public String getIdInServer() {
        return idInServer;
    }

    public void setIdInServer(String idInServer) {
        this.idInServer = idInServer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public List<FormQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<FormQuestion> questions) {
        this.questions = questions;
    }

    public boolean isSended() {
        return sended;
    }

    public void setSended(boolean sended) {
        this.sended = sended;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public boolean isOpenQuestionNeeded() {
        return openQuestionNeeded;
    }

    public void setOpenQuestionNeeded(boolean openQuestionNeeded) {
        this.openQuestionNeeded = openQuestionNeeded;
    }

    public String getOpenQuestionLabel() {
        return openQuestionLabel;
    }

    public void setOpenQuestionLabel(String openQuestionLabel) {
        this.openQuestionLabel = openQuestionLabel;
    }

    public boolean isAlreadySended() {
        return alreadySended;
    }

    public void setAlreadySended(boolean alreadySended) {
        this.alreadySended = alreadySended;
    }
}
