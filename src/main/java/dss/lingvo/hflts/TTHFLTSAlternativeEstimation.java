package dss.lingvo.hflts;

public class TTHFLTSAlternativeEstimation {
    private TTHFLTS[] myEstimates;

    public TTHFLTSAlternativeEstimation(TTHFLTS[] estimates) {
        this.myEstimates = estimates;
    }

    public TTHFLTS[] getMyEstimates() {
        return myEstimates;
    }

    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        for (TTHFLTS i : myEstimates) {
            bld.append(i.toString());
        }
        return bld.toString();
    }
}
