package dss.lingvo.hflts;

public class TTHFLTSConcatBoundsOperator {
    public String[] calculate(String top, String bottom,
                              TTHFLTSScale scale) {
            String [] res;
            if (top.equals(bottom)) {
                res = new String[]{top};
            }else if(scale.termPosition(top) < scale.termPosition(bottom)) {
                res = new String[]{top, bottom};
            } else {
                res = new String[]{bottom, top};
            }
            return res;
    }
}
