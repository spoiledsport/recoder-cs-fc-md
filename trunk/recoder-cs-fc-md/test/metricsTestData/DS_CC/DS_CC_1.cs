/*
<EXPECTED_METRICS>
DS_CC:[[5,2,5,0,4,0,1,1],[2,0,1,1,0],[0],[0,2],[1],[0]]
</EXPECTED_METRICS>
 */
using System;

namespace metricTests
{
	
	
public class DSCCTest {
	
	public int i;
	
	/**
	 * +called by callConstructurs from the class DSCCTest1
	 * Result:5
	 */
	public DSCCTest(string name){
		this.name=name;
	}
	
	/**
	 * +called by callConstructurs from the class DSCCTest1
	 * Result:1
	 */
	public DSCCTest(string name,string foreigname){
		this.name=name+" "+ foreigname;
	}
	
	/**
	 * +called by callMethod1 from the class DSCCTest1
	 * +called by callMethod2 from the class DSCCTest1
	 * +called by constructur from the class DSCCTest1
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
	 * +called by callMethod2 from the class DSCCTest1
	 * Result:1
	 */
	public void method2(){
		Console.WriteLine("CMTEST");
	}
	
	private static string name="CM";

	/**
	 * +called by printname() in the same class.
	 * +called by method3() in the same class.
	 * Result:2
	 */
	private static string getName() {
		return name;
	}
	
	/**
	 * +called by callMethod3 from the class DSCCTest1.
	 * -method is calling the method getname() in this class.
	 * Result:1
	 */
	public static void printName(){
		Console.WriteLine(getName());
	}
	
	/**
	 * +called by callMethod3 from the class DSCCTest1
	 * -method is calling the method getname() in this class
	 *  Result:1
	 */
	public static int method3(int k){
		return k*this.getName().Length;
	}
}

/**
 * Result CC:[2]
 */
public class DSCCTest1{
	DSCCTest a= new DSCCTest("CCTEST1");
	
	string name;
	/**
	 * -method is calling the method "getI()" from the class DSCCTest.
	 * Result:0
	 * 
	 */
	public DSCCTest1() {
		this.name=" Test1"+a.getI();
	}
	/**
	 * -method is calling the construkturs from the class DSCCTest.
	 * Result:0
	 */
	private void callConstructurs(){
		DSCCTest constr= new DSCCTest("Test1");
		DSCCTest constr1= new DSCCTest("Test1","CM");
	}
	
	/**
	 * -method is calling the method getI() from the class DSCCTest.
	 * + is called from DSCCTest2
	 * Result:0
	 */
	public int callMethod1(){
		return a.getI();
	}
	
	/**
	 * -method is calling the method getI() and method2() from the class DSCCTest.
	 * + is called from DSCCTest3
	 * Result:1
	 */
	public int callMethod2(){
		a.method2();
		return a.getI();
	}
	
	/**
	 * method is calling the method "method3()" from the class DSCCTest.
	 * method is calling the method "printName" from the class DSCCTest.
	 * Result:0
	 */
	public static int callMethod3(int k){
		DSCCTest.printName();
		return DSCCTest.method3(k)*2;
	}	
}
/**
 * Result CC:[0]
 */
public abstract class DSCCTest2{
	
	
	/**
	 * -method is calling the construkturs from the class DSCCTest.
	 * Result:0
	 */
	private void callConstructurs(){
		DSCCTest1 a= new DSCCTest1();
		DSCCTest constr= new DSCCTest("Test1");
		DSCCTest constr1= new DSCCTest("Test1","CM");
		int i= a.callMethod1()*constr.getI();
	}
}
/**
 * Result CC:[2]
 */
public class DSCCTest3{
	DSCCTest1 test1= new DSCCTest1();
	DSCCTest a= new DSCCTest("Test3");
	/**
	 * -method is calling the method getI() and method2() from the class DSCCTest.
	 * Result:0
	 */
	private int callMethod2(){
		
		a.method2();
		return a.getI()*test1.callMethod2();
	}
	
	/**
	 * +called in class DSCCTest4 and in DSCCTest5
	 * @return
	 */
	public int method1(){
		return 12;
	}
}
/**
 * Result CC:[1]
 */
public class DSCCTest4{
	DSCCTest a= new DSCCTest("Test4");
	DSCCTest3 test3= new DSCCTest3();
	/**
	 * -method is calling the method getI() and method2() from the class DSCCTest.
	 * +called from class DSCCTest5
	 * Result:0
	 */
	public int callMethod2(){
		
		a.method2();
		return a.getI()*test3.method1();
	}
}
/**
 * Result CC:[0]
 */
public class DSCCTest5{
	DSCCTest a= new DSCCTest("Test5");
	DSCCTest3 test3= new DSCCTest3();
	DSCCTest4 test4= new DSCCTest4();
	/**
	 * -method is calling the methods getI() and method2() from the class DSCCTest.
	 * -method is calling the method method1() from the class DSCCTest3 and callMethod2() from the class DSCCTest4.
	 * Result:0
	 */
	public int callMethod2(){
		
		a.method2();
		return a.getI()*test3.method1()*test4.callMethod2();
	}
}
}
