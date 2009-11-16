/*
<EXPECTED_METRICS>
DS_CM:[[2,2,3,1,1,2,1,1],[0,0,0,0,0],[0]]
</EXPECTED_METRICS>
 */
using System;

namespace metricTests
{
	
	
/**
 * Testclass for Changing Methods (CM)
 *
 * Category/Partition Method:
 *       
 * Method
 *   Visibility: private, protected internal, public
 * 	 Get/Set: getter/setter 
 * 	 Constructor: constructor 
 * 	 Static: static methods
 * 
 * 
 */

/**
 * Result CM:[2,2,3,1,1,2,1,1]
 */
public class DSCMTest {
	
	public int i;
	
	/**
	 * +called by callConstructurs from the class DSCMTest1 and DSCMTest2
	 * Result:2
	 */
	public DSCMTest(String name){
		DSCMTest.name=name;
	}
	
	/**
	 * +called by callConstructurs from the class DSCMTest1 and DSCMTest2
	 * Result:2
	 */
	protected internal DSCMTest(String name,String foreigname){
		DSCMTest.name=name+" "+ foreigname;
	}
	
	/**
	 * +called by callMethod1 from the class DSCMTest1
	 * +called by callMethod2 from the class DSCMTest1
	 * +called by constructur from the class DSCMTest1
	 * 
	 * -method is calling the method "method1()" in the same class.
	 * 
	 * Result:3
	 */
	public int getI() {
		return method1(i);
	}
	
	/**
	 * 
	 * +called by getI from this class.
	 * Result:1
	 */
	private int method1(int i){
		return this.i*i+2;
	}
	
	/**
	 * +called by callMethod2 from the class DSCMTest1
	 * Result:1
	 */
	protected internal void method2(){
		Console.WriteLine("CMTEST");
	}
	
	private static String name="CM";

	/**
	 * +called by printname() in the same class.
	 * +called by method3() in the same class.
	 * Result:2
	 */
	private static String getName() {
		return name;
	}
	
	/**
	 * +called by callMethod3 from the class DSCMTest1.
	 * -method is calling the method getname() in this class.
	 * Result:1
	 */
	protected internal static void printName(){
		Console.WriteLine(getName());
	}
	
	/**
	 * +called by callMethod3 from the class DSCMTest1
	 * -method is calling the method getname() in this class
	 *  Result:1
	 */
	public static int method3(int k){
		getName();
			return k*42;
	}
}



/**
 * Result:[0,0,0,0,0]
 */
class DSCMTest1{
	
	DSCMTest a= new DSCMTest("CMTEST1");
	
	String name;
	/**
	 * -method is calling the method "getI()" from the class DSCMTest.
	 * Result:0
	 * 
	 */
	public DSCMTest1() {
		this.name=" Test1"+a.getI();
	}
	/**
	 * -method is calling the construkturs from the class DSCMTest.
	 * Result:0
	 */
	private void callConstructurs(){
		DSCMTest constr= new DSCMTest("Test1");
		DSCMTest constr1= new DSCMTest("Test1","CM");
	}
	
	/**
	 * -method is calling the method getI() from the class DSCMTest.
	 * Result:0
	 */
	public int callMethod1(){
		return a.getI();
	}
	
	/**
	 * -method is calling the method getI() and method2() from the class DSCMTest.
	 * Result:0
	 */
	protected internal int callMethod2(){
		a.method2();
		return a.getI();
	}
	
	/**
	 * method is calling the method "method3()" from the class DSCMTest.
	 * method is calling the method "printName" from the class DSCMTest.
	 * Result:0
	 */
	public static int callMethod3(int k){
		DSCMTest.printName();
		return DSCMTest.method3(k)*2;
	}	
}


/**
 * Result:[0]
 */
abstract class DSCMTest2{
	/**
	 * -method is calling the construkturs from the class DSCMTest.
	 * Result:0
	 */
	private void callConstructurs(){
		DSCMTest constr= new DSCMTest("Test1");
		DSCMTest constr1= new DSCMTest("Test1","CM");
	}
}
}
