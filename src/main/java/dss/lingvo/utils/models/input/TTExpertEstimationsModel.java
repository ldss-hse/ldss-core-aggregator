package dss.lingvo.utils.models.input;

import java.util.List;

public class TTExpertEstimationsModel {
    private String alternativeID;
    private List<TTCriteriaEstimationsModel> criteria2Estimation;

    public String getAlternativeID() {
        return alternativeID;
    }

    public void setAlternativeID(String alternativeID) {
        this.alternativeID = alternativeID;
    }

    public List<TTCriteriaEstimationsModel> getCriteria2Estimation() {
        return criteria2Estimation;
    }

    public void setCriteria2Estimation(List<TTCriteriaEstimationsModel> criteria2Estimation) {
        this.criteria2Estimation = criteria2Estimation;
    }
}