package dss.lingvo.utils.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class TTJSONModel {
    private List<TTCriteriaModel> criteria;
    private List<TTAlternativeModel> alternatives;
    private List<TTScaleModel> scales;
    private List<TTAbstractionLevelModel> abstractionLevels;
    private List<TTExpertModel> experts;
    @JsonProperty("estimations")
    private Map<String, List<TTExpertEstimationsModel>> estimations;

    public List<TTCriteriaModel> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<TTCriteriaModel> criteria) {
        this.criteria = criteria;
    }

    public List<TTAlternativeModel> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<TTAlternativeModel> alternatives) {
        this.alternatives = alternatives;
    }

    public List<TTScaleModel> getScales() {
        return scales;
    }

    public void setScales(List<TTScaleModel> scales) {
        this.scales = scales;
    }

    public List<TTAbstractionLevelModel> getAbstractionLevels() {
        return abstractionLevels;
    }

    public void setAbstractionLevels(List<TTAbstractionLevelModel> abstractionLevels) {
        this.abstractionLevels = abstractionLevels;
    }

    public List<TTExpertModel> getExperts() {
        return experts;
    }

    public void setExperts(List<TTExpertModel> experts) {
        this.experts = experts;
    }

    public Map<String, List<TTExpertEstimationsModel>> getEstimations() {
        return estimations;
    }

    public void setEstimations(Map<String,  List<TTExpertEstimationsModel>> estimations) {
        this.estimations = estimations;
    }
}
