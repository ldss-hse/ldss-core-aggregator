package dss.lingvo.utils.models.input.multilevel;

import com.fasterxml.jackson.annotation.JsonProperty;
import dss.lingvo.utils.models.input.TTCriteriaModel;
import dss.lingvo.utils.models.input.common.TTCommonInputModel;

import java.util.List;
import java.util.Map;

public class TTJSONMultiLevelInputModel extends TTCommonInputModel{
    @JsonProperty("criteria")
    private Map<String, List<TTCriteriaModel>> criteria;

    public Map<String, List<TTCriteriaModel>> getCriteria() {
        return criteria;
    }

    public void setCriteria(Map<String, List<TTCriteriaModel>> criteria) {
        this.criteria = criteria;
    }
}
