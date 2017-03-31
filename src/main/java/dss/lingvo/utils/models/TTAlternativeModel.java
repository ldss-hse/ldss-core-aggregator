package dss.lingvo.utils.models;

public class TTAlternativeModel {
    private String alternativeID;
    private String alternativeName;
    private boolean abstractionLevelID;

    public String getAlternativeID() {
        return alternativeID;
    }

    public void setAlternativeID(String alternativeID) {
        this.alternativeID = alternativeID;
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    public void setAlternativeName(String alternativeName) {
        this.alternativeName = alternativeName;
    }

    public boolean isAbstractionLevelID() {
        return abstractionLevelID;
    }

    public void setAbstractionLevelID(boolean abstractionLevelID) {
        this.abstractionLevelID = abstractionLevelID;
    }
}
