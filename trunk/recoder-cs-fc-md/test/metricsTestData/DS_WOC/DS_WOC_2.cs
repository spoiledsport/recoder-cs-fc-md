/*
<EXPECTED_METRICS>
DS_WOC:[1.0,2.0,0.5,1.0,1.0,1.0,0.5,1.0,0.8,1.0,1.0]
</EXPECTED_METRICS>
 */
using System;

namespace metricTests
{
	
	



/**
 * Test class for WOC.
 * 
 * Category Partition Method.
 * 
 * WOC = # functional public methods / # accessible attributes
 *
 * Category/Partition Method
 * 
 * Top-Level: fraction test cases
 * # functional public methods
 *    {none, one or more}
 * # accessible attributes
 *    {none, one or more}
 * class type
 *    {regular, abstract, interface}
 * 
 * Low-Level: numerator, denominator test cases
 * 
 * functional public methods:
 * visibility
 *   {+public, private, protected}
 * Get/Set
 *   {getter/setter, +no getter/setter}
 * Constructor
 *   {one or more constructors, +no constructor}
 * static
 *   {static, +non static}
 *   
 * accessible attributes
 * 
 * getters/setters
 *   {g/s present, g/s not present}
 * attribute visibility 
 *   {+public, protected, private}
 * static attributes
 *   {+static, +non static}
 * 
 * 
 */

/**
 * Configuration:
 * Top Level:
 *   functional public methods: none
 *   accessible attributes: none
 *   
 * expected result: 1/1 = 1
 */
public class DSWOCTest {
	

}

/**
 * Configuration:
 * Top Level:
 *   functional public methods: one or more
 *   accessible attributes: none
 * 
 * expected result: 2/1
 */
class DSWOCTest2 {
	
	public void method1(){
		
	}
}

/**
 * Configuration:
 * Top Level:
 *   functional public methods: none
 *   accessible attributes: one or more
 *   
 * expected result: 1/2
 */
class DSWOCTest3 {
	
	public int i;
}

/**
 * Configuration:
 * Top Level:
 *   functional public methods: one or more
 *   accessible attributes: one or more
 *   
 * expected result: 2/2
 */
class DSWOCTest4 {
	
	public int i;
	
	public void method1(){
		
	}
}

/**
	functional public methods:
	 * visibility
	 *   {+public, private, protected}
	 * Get/Set
	 *   {+no getter/setter}
	 * Constructor
	 *   {+no constructor}
	 * static
	 *   {+non static}
	 *   
	 *   expected result: 2/2
*/
class DSWOCTest5 {
	
	public int i;
	
	public void method1(){
		
	}
	
	protected void method2(){
		
	}
	
	private void method3(){
		
	}
}

/**
functional public methods:
 * visibility
 *   {+public}
 * Get/Set
 *   {getter/setter, +no getter/setter}
 * Constructor
 *   {+no constructor}
 * static
 *   {+non static}
 *   
 *   expected result: 2/2
*/
class DSWOCTest6 {

	private int i;

	public int getI(){
		return i;
	}
	
	public void setI(int i){
		this.i = i;
	}
	
	public void method1(){
		
	}
	
}

/**
functional public methods:
 * visibility
 *   {private}
 * Get/Set
 *   {+no getter/setter}
 * Constructor
 *   {one or more constructors}
 * static
 *   {+non static}
 *   
 * expected result: 1/2
*/
class DSWOCTest7 {

	public int i;
	
	public DSWOCTest7(int j){
		i=j;
	}
}

/**
functional public methods:
 * visibility
 *   {private}
 * Get/Set
 *   {+no getter/setter}
 * Constructor
 *   {+no constructor}
 * static
 *   {+static, non static}
 *   
 *   expected result: 2/2
*/
class DSWOCTest8 {

	public int i;

	public static void method1(){
		
	}
}

/**
* accessible attributes
* 
* getters/setters
*   {g/s present, g/s not present}
* attribute visibility 
*   {private}
* static attributes
*   {+non static}
* 
* expected result: 2/2.5
*/
class DSWOCTest9 {

	private int i;
	private Double d;
	
	public int getI(){
		return i;
	}
	
	public void setI(int i){
		this.i=i;
	}
	
	public void setD(Double d){
		this.d=d;
	}
	
	public static void method1(){
		
	}
}

/**
* accessible attributes
* 
* getters/setters
*   {g/s not present}
* attribute visibility 
*   {public, private, protected}
* static attributes
*   {+non static}
*   
*   expected result: 2/2
*/
class DSWOCTest10 {

	private int i;
	protected Double d;
	public String s;
	
	
	public static void method1(){
		
	}
}

/**
* accessible attributes
* 
* getters/setters
*   {g/s not present}
* attribute visibility 
*   {public}
* static attributes
*   {+non static}
*   
*   expected result: 2/2
*/
class DSWOCTest11 {

	
	static public String s;
		
	public static void method1(){
		
	}
}

}
