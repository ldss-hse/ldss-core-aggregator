package dss.lingvo.utils.models.input;

public class TTCriteriaModel {
    private String criteriaID;
    private String criteriaName;
    private boolean qualitative;
    private boolean isBenefit;
    private String units;

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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public boolean isBenefit() {
        return isBenefit;
    }

    public void setBenefit(boolean isBenefit) {
        this.isBenefit = isBenefit;
    }
}
