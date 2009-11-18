package fcMDtests;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import fcMDtests.metricTests.*;

/**
 * A test suite to run all metrics unit tests.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	DS_LOCC_Test.class,
	DS_WMC_Test.class,
	DS_ATFD_Test.class,
	DS_TCC_Test.class,
	DS_CYCLO_Test.class,
	DS_CC_Test.class, 
	DS_NOPA_Test.class,
	DS_NOAM_Test.class, 
	DS_MAXNESTING_Test.class,
	DS_WOC_Test.class,
	DS_CINT_Test.class,
	DS_CM_Test.class,
	DS_NOAV_Test.class
	
})
public class MetricsTestSuite {
    // why on earth I need this class, I have no idea! 
}