package dss.lingvo.utils.models.input.singlelevel;

import com.fasterxml.jackson.annotation.JsonProperty;
import dss.lingvo.utils.models.input.*;
import dss.lingvo.utils.models.input.common.TTCommonInputModel;

import java.util.List;
import java.util.Map;

public class TTJSONInputModel extends TTCommonInputModel{
    private List<TTCriteriaModel> criteria;

    public List<TTCriteriaModel> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<TTCriteriaModel> criteria) {
        this.criteria = criteria;
    }
}
