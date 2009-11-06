package metrics;

import java.util.LinkedList;

//import dataSystem.metrics.AbstractMetricAttribute;

/**
 * A data class that defines all used metrics. Each metric is associated with an
 * static attribute of the class. Whenever other classes are using metrics these
 * attributes should be used to set or determine the type of the metric.
 */
public abstract class MetricNamesInterface {

	/**
	 * Detection Strategy Framework metrics
	 */
	// public static final int DS_ATFD = 0;
	// public static final int DS_WMC = 1;
	// public static final int DS_TCC = 2;
	// public static final int DS_SHOTGUNSURGERY = 3;
	// public static final int DS_NOPA = 4;
	// public static final int DS_NOM = 5;
	// public static final int DS_NOAM = 6;
	// public static final int DS_NProtM = 7;
	// public static final int DS_WOC = 8;
	// public static final int DS_CINT = 9;
	// public static final int DS_LOCM = 10;
	public static final int DS_LOCC = 11;
	// public static final int DS_AMW = 12;
	// public static final int DS_BOVR = 13;
	// public static final int DS_BUR = 14;
	// public static final int DS_CC = 15;
	// public static final int DS_CDISP = 16;
	// public static final int DS_CM = 17;
	// public static final int DS_CYCLO = 18;
	// public static final int DS_FDP = 19;
	// public static final int DS_LAA = 20;
	// public static final int DS_MAXNESTING = 21;
	// public static final int DS_NAS = 22;
	// public static final int DS_NOAV = 23;
	// public static final int DS_PNAS = 24;
	// public static final int DS_GODCLASS = 25;
	// public static final int DS_BRAINCLASS = 26;
	// public static final int DS_DATACLASS = 27;
	// public static final int DS_DISPERSEDCOUPLING = 28;
	// public static final int DS_FEATUREENVY = 29;
	// public static final int DS_INTENSIVECOUPLING = 30;
	// public static final int DS_REFUSEDPARENTBEQUEST = 31;
	// public static final int DS_TRADITIONBREAKER = 32;
	// public static final int DS_BRAINMETHOD = 33;
	// public static final int DS_ATFDM = 34;
	// public static final int DS_AMWP=35;
	// public static final int DS_NOMP=36;
	// public static final int DS_WMCP=37;
	// public static final int DS_CNM=38;
	// public static final int DS_MNM=40;

	/**
	 * static array with info about: - unique identifier (TODO: this is not
	 * always the same as the static attributes) - short description (to be
	 * shown in GUI) - long description (metric definition) - database name
	 * (name in the cache database) - metric group (for logical grouping, i.e.
	 * to be shown in GUI)
	 * 
	 * Attention: the index of the array entry must correlate with the unique
	 * integer index of the attributes!
	 */

	/**
	 * Positions of different information in metricNames array. To access a
	 * metric and a field only the static identifiers should be used: e.g.
	 * 
	 * import static ...MetricNamesInterface.*; ... String locDescription =
	 * metricNames[LOC][DESCRIPTION];
	 */
	public static int SHORTCUT = 0;
	public static int FULLNAME = 1;
	public static int DESCRIPTION = 2;
	// not in use at the moment: names in database are SHORTCUT
	public static int DBNAME = 3;
	// Groups for better GUI visibility
	public static int GROUP = 4;
	public static int TYPE = 5;
	public static int BOUND_UNBOUND = 6;

	public static String[][] metricNames = {


			// ###### DS FRAMEWORK METRICS #####

			{ "DS_ATFD", "Access to Foreign Data", " ", "DS_ATFD",
					"Coupling Metrics",
					"dataSystem.metrics.IntegerArrayValueMetric", "bound" },

			{
					"DS_WMC",
					"Weighted Method Count",
					"The sum of the statical complexity of all methods of a class. The CYCLO metric is used to quantify the method's complexity.",
					"DS_WMC", "Size and Complexity",
					"dataSystem.metrics.IntegerArrayValueMetric", "bound" },

			{
					"DS_TCC",
					"Tight Class Cohesion",
					"The relative number of method pairs of a class that access in common at least one attribute of the measured class.",
					"DS_TCC", "Other Metrics",
					"dataSystem.metrics.DoubleArrayValueMetric", "bound" },

			{ "DS_SHOTGUNSURGERY", "Shotgun Surgery",
					"Shotgun Surgery Code Smell ", "DS_SHOTGUNSURGERY",
					"Code Smells",
					"dataSystem.metrics.IntegerArray2ValueMetric", "unbound" },

			{
					"DS_NOPA",
					"Number of Public Attributes",
					"The Number of Public Attributes, which are not static and constant, of a class.Don't measured for Abstract classes.",
					"DS_NOPA", "Other Metrics",
					"dataSystem.metrics.IntegerArrayValueMetric", "bound" },

			{
					"DS_NOM",
					"Number of Methods",
					"NOM: Number of methods of a class. Measured Entity Abstract(+). Here is summed the number of get/set methods,Constructur,static methods and abstract methods with all visibility",
					"DS_NOM", "Size and Complexity",
					"dataSystem.metrics.IntegerArrayValueMetric", "bound" },

			{
					"DS_NOAM",
					"Number of Accessor Methods",
					"NOAM: Number of get/set methods of a class.Don't measured for Abstract classes. Here is summed the number of only public get/set methods(Constructur(-),Static(-), Abstract(-) ",
					"DS_NOAM", "Size and Complexity",
					"dataSystem.metrics.IntegerArrayValueMetric", "bound" },

			{
					"DS_NProtM",
					"Number of Protected Members",
					"The number of protected methods and attributes of a class",
					"DS_NProtM", "Size and Complexity",
					"dataSystem.metrics.IntegerArrayValueMetric", "bound" },

			{
					"DS_WOC",
					"Weight of a Class",
					"Number of functional public methods divided by the total number of public members.",
					"DS_WOC", "Size and Complexity",
					"dataSystem.metrics.DoubleArrayValueMetric", "bound" },
			{
					"DS_CINT",
					"Coupling Intensity",
					"The number of distinct operations called by the measured operation.",
					"DS_CINT", "Coupling",
					"dataSystem.metrics.IntegerArray2ValueMetric", "bound" },

			{
					"DS_LOCM",
					"Lines of Code (Methods)",
					"Number of lines of code of an operation, including blank lines, comments, parenthesis, and method header. But without java doc header.",
					"DS_LOCM", "Size and Complexity",
					"dataSystem.metrics.IntegerArray2ValueMetric", "bound" },

			{ "DS_LOCC", "Lines of Code (Class)", "", "DS_LOCC",
					"Size and Complexity",
					"dataSystem.metrics.IntegerArrayValueMetric", "bound" },

			{
					"DS_AMW",
					"Average method weight",
					"The avarage static complexity of all Methods in a class: WMC/NOM",
					"DS_AMW", "Size and Complexity",
					"dataSystem.metrics.DoubleArrayValueMetric", "bound" },

			{
					"DS_BOVR",
					"Base Class Overriding Ratio",
					"The number of methods of the measured class that override methods from the base class divided by the total number of methods in the class",
					"DS_BOVR", "Size and Complexity",
					"dataSystem.metrics.DoubleArrayValueMetric", "unbound" },

			{
					"DS_BUR",
					"Base Class Usage Ratio",
					"The number of inheritance-specific members used by the measured class divided by the total number of inheritance-specific members from the base class.",
					"DS_BOVR", "Size and Complexity",
					"dataSystem.metrics.DoubleArrayValueMetric", "unbound" },

			{
					"DS_CC",
					"Changing Classes",
					"The number of classes in which the methods that call the measured method are defined in.",
					"DS_CC", "Size and Complexity",
					"dataSystem.metrics.IntegerArray2ValueMetric", "unbound" },

			{
					"DS_CDISP",
					"Coupling Dispersion",
					"The number of classes in which the operation called from the measured operation are defined, divided by CINT",
					"DS_CDISP", "Size and Complexity",
					"dataSystem.metrics.DoubleArray2ValueMetric", "bound" },

			{
					"DS_CM",
					"Changing Methods",
					"The number of dinstinct methods that call the measured method.",
					"DS_CM", "Size and Complexity",
					"dataSystem.metrics.IntegerArray2ValueMetric", "unbound" },

			{ "DS_CYCLO", "MCCabe Cyclomatic Complexity", "", "DS_CYCLO",
					"Size and Complexity",
					"dataSystem.metrics.IntegerArray2ValueMetric", "bound" },

			{ "DS_FDP", "Foreign Data Providers",
					"The number of classes in which the attributes accessed",
					"DS_FDP", "Size and Complexity",
					"dataSystem.metrics.IntegerArray2ValueMetric", "bound" },

			{
					"DS_LAA",
					"Locality of Attribute Accesses",
					"The Number of attributes from the methods definition class, "
							+ "divided by the total number of variables accessed,"
							+ "whereby the number of local attributes accessed is computed in conformity with the LAA spetifications",
					"DS_LAA", "Size and Complexity",
					"dataSystem.metrics.DoubleArray2ValueMetric", "bound" },

			{
					"DS_MAXNESTING",
					"Maximum Nesting Level",
					"The maximum nesting level of control structures within an operation",
					"DS_MAXNESTING", "Size and Complexity",
					"dataSystem.metrics.IntegerArray2ValueMetric", "bound" },

			{
					"DS_NAS",
					"Number of Added Services",
					"Number of public methods of a class that are not overriden or"
							+ " specialized from the ancestor classes",
					"DS_NAS", "Size and Complexity",
					"dataSystem.metrics.IntegerArrayValueMetric", "unbound" },

			{
					"DS_NOAV",
					"Number of Accessed Variables",
					"The total number of variables accessed directly from the measured operation",
					"DS_NOAV", "Size and Complexity",
					"dataSystem.metrics.IntegerArray2ValueMetric", "bound" },

			{
					"DS_PNAS",
					"Percentage of Newly Added Services",
					"Number of public methods of a class that are not overriden or"
							+ " specialized from the ancestor classes, divided by the total number of public methods",
					"DS_PNAS", "Size and Complexity",
					"dataSystem.metrics.DoubleArrayValueMetric", "unbound" },

			{ "DS_GODCLASS", "God Class", "God Class Code Smell",
					"DS_GODCLASS", "Code Smells",
					"dataSystem.metrics.IntegerArrayValueMetric", "bound" },

			{ "DS_BRAINCLASS", "Brain Class", "Brain Class Code Smell",
					"DS_BRAINCLASS", "Code Smells",
					"dataSystem.metrics.IntegerArrayValueMetric", "bound" },

			{ "DS_DATACLASS", "Data Class", "Data Class Code Smell",
					"DS_DATACLASS", "Code Smells",
					"dataSystem.metrics.IntegerArrayValueMetric", "bound" },

			{ "DS_DISPERSEDCOUPLING", "Dispersed Coupling ",
					"Dispersed Coupling Code Smell", "DS_DATACLASS",
					"Code Smells",
					"dataSystem.metrics.IntegerArray2ValueMetric", "bound" },

			{ "DS_FEATUREENVY", "Feature Envy", "Feature Envy Code Smell",
					"DS_FEATUREENVY", "Code Smells",
					"dataSystem.metrics.IntegerArray2ValueMetric", "bound" },

			{ "DS_INTENSIVECOUPLING", "Intensive Coupling",
					"Intensive Coupling Code Smell", "DS_INTENSIVECOUPLING",
					"Code Smells",
					"dataSystem.metrics.IntegerArray2ValueMetric", "bound" },

			{ "DS_REFUSEDPARENTBEQUEST", "Refused Parent Bequest",
					"Refused Parent Bequest Code Smell",
					"DS_REFUSEDPARENTBEQUEST", "Code Smells",
					"dataSystem.metrics.IntegerArrayValueMetric", "unbound" },

			{ "DS_TRADITIONBREAKER", "Tradition Breaker",
					"Tradition Breaker Code Smell", "DS_TRADITIONBREAKER",
					"Code Smells",
					"dataSystem.metrics.IntegerArrayValueMetric", "unbound" },

			{ "DS_BRAINMETHOD", "Brain Method", "Brain Method Code Smell",
					"DS_BRAINMETHOD", "Code Smells",
					"dataSystem.metrics.IntegerArray2ValueMetric", "bound" },
			{ "DS_ATFDM", "Access to Foreign Data (Method level)", " ",
					"DS_ATFDM", "Coupling Metrics",
					"dataSystem.metrics.IntegerArray2ValueMetric", "bound" },
			{
					"DS_AMWP",
					"Average method weight of the parent classes",
					"The avarage static complexity of all Methods in a class: WMCP/NOMP",
					"DS_AMWP", "Size and Complexity",
					"dataSystem.metrics.DoubleArrayValueMetric", "bound" },
			{
					"DS_NOMP",
					"Number of Methods of the parent class",
					"NOMP: Number of methods of a class. Measured Entity Abstract(+). Here is summed the number of get/set methods,Constructur,static methods and abstract methods with all visibility",
					"DS_NOMP", "Size and Complexity",
					"dataSystem.metrics.IntegerArrayValueMetric", "bound" },

			{
					"DS_WMCP",
					"Weighted Method Count of the parent methods",
					"The sum of the statical complexity of all methods of a class. The CYCLO metric is used to quantify the method's complexity.",
					"DS_WMCP", "Size and Complexity",
					"dataSystem.metrics.IntegerArrayValueMetric", "bound" },

			{
					"DS_CNM",
					"Class name Metric",
					"All name of classes in a class file,so the inner classes are included too",
					"DS_CNM", "Other",
					"dataSystem.metrics.StringArrayValueMetric", "bound" },

			{ "DS_MNM", "Method names Metric",
					"All names of methods and constucturs in a class file",
					"DS_MNM", "Other",
					"dataSystem.metrics.StringArray2ValueMetric", "bound" } };

	/**
	 * Returns all metric groups.
	 * 
	 * @return An array of metric groups.
	 */
	public static LinkedList<String> getGroups() {
		LinkedList<String> groups = new LinkedList<String>();
		for (String[] metric : metricNames) {
			if (!groups.contains(metric[GROUP])) {
				groups.add(metric[GROUP]);
			}
		}
		return groups;
	}

	public static LinkedList<String[]> getMetricsByGroup(String group) {
		LinkedList<String[]> result = new LinkedList<String[]>();
		for (String[] metric : metricNames) {
			if (metric[GROUP].equals(group)) {
				result.add(metric);
			}
		}
		return result;
	}

	/**
	 * Returns the Integer value for the metric specified by shortcut. Or null
	 * if a metric with this name cannot be found.
	 * 
	 * @param shortcut
	 * @return
	 */
	public static Integer shortcutToInt(String shortcut) {
		for (int i = 0; i < metricNames.length; i++) {
			if (metricNames[i][MetricNamesInterface.SHORTCUT].equals(shortcut)) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Returns the shortcut as a String.
	 * 
	 * @param shortcut
	 * @return
	 */
	public static String intToShortcut(int shortcut) {
		return MetricNamesInterface.metricNames[shortcut][0];
	}

}
