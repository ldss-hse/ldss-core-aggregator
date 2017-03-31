package dss.lingvo.utils.models;

public class TTCriteriaModel {
    private String criteriaID;
    private String criteriaName;
    private boolean qualitative;

    public String getCriteriaID() {
        return criteriaID;
    }

    public void setCriteriaID(String criteriaID) {
        this.criteriaID = criteriaID;
    }

    public String getCriteriaName() {
        return criteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public boolean isQualitative() {
        return qualitative;
    }

    public void setQualitative(boolean qualitative) {
        this.qualitative = qualitative;
    }
}
