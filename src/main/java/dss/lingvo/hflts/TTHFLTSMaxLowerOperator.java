package dss.lingvo.hflts;

import java.util.ArrayList;

public class TTHFLTSMaxLowerOperator {
    public String calculate(TTHFLTSAlternativeEstimation altEstimates, TTHFLTSScale scale) {
        String maxMin = "vl";
        for (TTHFLTS criteriaOption : altEstimates.getMyEstimates()) {
            final String candidateForMin = criteriaOption.getTerms()[0];
            if (scale.termPosition(candidateForMin) > scale.termPosition(maxMin)) {
                maxMin = candidateForMin;
            }
        }
        return maxMin;
    }
}
