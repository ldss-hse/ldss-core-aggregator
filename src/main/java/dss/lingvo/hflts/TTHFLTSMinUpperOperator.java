package dss.lingvo.hflts;

public class TTHFLTSMinUpperOperator {
    public String calculate(TTHFLTSAlternativeEstimation altEstimates, TTHFLTSScale scale) {
        String minMax = "p";
        for (TTHFLTS criteriaOption : altEstimates.getMyEstimates()) {
            final int last = criteriaOption.getTerms().length - 1;
            final String candidateForMin = criteriaOption.getTerms()[last];
            if (scale.termPosition(candidateForMin) < scale.termPosition(minMax)) {
                minMax = candidateForMin;
            }
        }
        return minMax;
    }
}
