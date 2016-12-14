package dss.lingvo.hflts;

/**
 * Created by demidovs on 14.12.16.
 */
public class TTHFLTSAlternativeEstimation {
    private TTHFLTS[] myEstimates;

    public TTHFLTSAlternativeEstimation(TTHFLTS [] estimates){
        this.myEstimates = estimates;
    }

    public TTHFLTS[] getMyEstimates() {
        return myEstimates;
    }

    @Override
    public String toString(){
        String finalString = "";
        for (TTHFLTS i : myEstimates){
            finalString += i.toString();
        }
        return finalString;
    }
}
