package dss.lingvo.utils.models;

import java.util.List;

public class TTCriteriaEstimationsModel {
    private String criteriaID;
    private String scaleID;
    private List<String> estimation;

    public String getCriteriaID() {
        return criteriaID;
    }

    public void setCriteriaID(String criteriaID) {
        this.criteriaID = criteriaID;
    }

    public String getScaleID() {
        return scaleID;
    }

    public void setScaleID(String scaleID) {
        this.scaleID = scaleID;
    }

    public List<String> getEstimation() {
        return estimation;
    }

    public void setEstimation(List<String> estimation) {
        this.estimation = estimation;
    }
}