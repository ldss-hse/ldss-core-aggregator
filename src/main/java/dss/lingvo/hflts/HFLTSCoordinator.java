package dss.lingvo.hflts;

import dss.lingvo.utils.TTUtils;

public class HFLTSCoordinator {
    private TTUtils log = TTUtils.getInstance();

    public void go() {
        log.info("Step 1. Gather feedback and parse to HFLTS...");
        log.info("Step 1. Done.");
        log.info("Step 2. Aggregate HFLTS for each alternative...");
        log.info("Step 2. Done.");
        log.info("Step 3. Find Pareto frontier...");
        log.info("Step 3. Done.");
    }
}
