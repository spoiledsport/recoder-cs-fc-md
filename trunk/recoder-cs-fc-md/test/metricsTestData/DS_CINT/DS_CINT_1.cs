/*
<EXPECTED_METRICS>
DS_CINT:[[4,0,0,0],[0,null],[3,0,0]]
</EXPECTED_METRICS>
 */

// DS_CDISP:[[0.5,0.0,0.0,0.0],[0.0,null],[0.3333333333333333,0.5,0.0]]

using System;

namespace metricTests
{
	
	



/**
 * Test case for coupling intensity
 * and coupling dispersion.
 * 
 * C/P:
 * 
 * amount of coupling intensity:
 * - no foreign class calls (only inner class and same hierarchy calls)
 * - 1 or more foreign class calls
 */

public class DSCINTTest : DSCINTTestBase {

	public int member1 = 5;
	
	public DSCINTTest(int i){
		DSCINTTest2 foo = new DSCINTTest2(5);
		foo.toString();
		foo.toString();
		DSCINTTest2.myStaticMethod();
		m2();
		Console.WriteLine();
		m1();
	}
	
	public void m2() {
		//m1();
	}
	
	public int getMember(){
		return member1;
	}
	
	public void setMember(int i){
		this.member1=i;
	}
	
	
	
}

public abstract class DSCINTTestBase {
	public void m1(){
		//foo
	}
	
	public abstract void m22();
}

/**
 * first foreign class
 *
 */
class DSCINTTest2 {
	
	
	public DSCINTTest2(int i){
		DSCINTTest foo = new DSCINTTest(10);
		foo.setMember(foo.getMember());
	}
	
	public String toString(){
		return "foobar";
	}
	
	public  static void myStaticMethod(){
		DSCINTTest2 foo = new DSCINTTest2(5);
		foo.toString();
	}
	
}
}
