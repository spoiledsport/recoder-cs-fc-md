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
	DS_CC_Test.class
})
public class MetricsTestSuite {
    // why on earth I need this class, I have no idea! 
}