package recodercs.convenience.csharp;

import recodercs.util.Debug;

/**
 * @author orosz
 *
 * This class contains some useful functions for manipulating C#'s multi
 * dimensional arrays.
 * 
 */
public class MultidimArrayUtils {

	/** Puts the approtiate dimension indicator ( [,,] ) to the end of the string. 
	 */
	public static String appendDimensions(String base, int dimension) {
		Debug.asserta(dimension > 0);
		StringBuffer sb = new StringBuffer(base).append('[');
		for (int i = dimension - 1; i > 0; i--)
			sb.append(',');
		sb.append(']');
		return sb.toString();
	}

	/** Puts the approtiate dimension indicator ( [,,] ) to the end of the string. 
	 */
	public static void appendDimensions(StringBuffer base, int dimension) {
		Debug.asserta(dimension > 0);
		base.append('[');
		for (int i = dimension - 1; i > 0; i--)
			base.append(',');
		base.append(']');
	}


	/** Puts the approtiate dimensions to the end of a string. */
	public static String appendMultiDimensions(String base, int[] dimensions) {
		StringBuffer sb = new StringBuffer(base);
		for (int i= 0 ; i< dimensions.length; i++) 
		  appendDimensions(sb,dimensions[i]);
		return sb.toString(); 
	}

	/** Puts the approtiate dimensions to the end of a string. */
	public static void appendMultiDimensions(StringBuffer base, int[] dimensions) {
		for (int i= 0 ; i< dimensions.length; i++) appendDimensions(base,dimensions[i]);
	}

	/** Returns the dimensions of the array type (or 0, if it is not an array
	 * type).
	 */
	public static int getDimensions(String type) {
		int dimension = 0;

		if (type.endsWith("]")) {
			dimension = 1;
			String dimensions = type.substring(type.lastIndexOf("[") + 1);
			char[] chrs = dimensions.toCharArray();
			for (int i = 0; i < chrs.length - 1; i++)
				if (chrs[i] == ',')
					dimension++;
		}

		return dimension;
		
	}

	/** Returns base of the array type (the type without the last []).
	 */
	public static String getBase(String type) {
		if (type.endsWith("]")) {
			return type.substring(0,type.lastIndexOf("["));
		}

		return type;
		
	}

	/** Returns the non-array basetype of the array type (the type without all the indices []).
	 */
	public static String getBaseTypeName(String type) {
		if (type.endsWith("]")) {
			return type.substring(0,type.indexOf("["));
		}

		return type;
		
	}

	/** Returns the non-array basetype of the array type (the type without all the indices []).
	 */
	public static String getIndexExpressions(String type) {
		if (type.endsWith("]")) {
			return type.substring(type.indexOf("["));
		}

		return type;
		
	}


	// Main method for testing purposes only.
	public static void main(String[] args) {
		System.out.println(getDimensions("a[,][, ,,]"));
		System.out.println(getDimensions("a[, ,,  , ,]"));
		System.out.println(getDimensions("a[]"));
		System.out.println(getBase("a[,][, ,,]"));
		System.out.println(getBaseTypeName("a[,][, ,,]"));
		System.out.println(getIndexExpressions("a[,][, ,,]"));
		System.out.println(getIndexExpressions("a[]"));
	}

}
