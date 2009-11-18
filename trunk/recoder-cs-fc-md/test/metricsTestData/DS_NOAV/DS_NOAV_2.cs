/*
<EXPECTED_METRICS>
DS_NOAV:[[6,0,1],[]]
</EXPECTED_METRICS>
 */
using System;

namespace metricTests
{
	
	



/**
 * Test case for Number of Accessed Variables (NOAV)
 * 
 *  C/P Method
 *  
 *  accessed variables: none, some, lots
 *  
 *  types of varibles:
 *  - locally defined including parameters
 *  - class variables
 *  - variables from other classes (public, protected, static, !readonly)
 *
 *  types of accesses:
 *  - one time 
 *  - multiple times
 */

abstract public class DSNOAVTest {

	private int memberPrivate = 0;
	
	private static int memberStatic = 0;
	
	public static readonly int memberStaticFinal = 0;
	
	public readonly int memberFinal = 0;
	
	/**
	 * Accesses:
	 * 1 parameter access: param1
	 * 1 class variable: memberPrivate
	 * 2 local variables: s,foo
	 * 2 variables from another class
	 * 
	 * Sum: 6
	 */
	public DSNOAVTest(int param1, int param2){
		
		this.memberPrivate = param1;
		
		//local variable access
		string s="blubb";
		
		//PrintStream out is readonly
		Console.Write(s);
		
		DSNOAVTest2 foo = new DSNOAVTest2();
		foo.member2++;
		
		DSNOAVTest2.member++;

	}
	
	/**
	 * Abstract method result: null
	 */
	public abstract void methodAbstract();
	
	/**
	 * no accesses: 0
	 */
	public void noAccesses(){
		Console.Write("" +
			"blobb");
	}
	
	/**
	 * Multiple accesses to same variable
	 * Sum:1
	 */
	
	public void multipleAccesses(int i){
		i=i+i*i/i;
		
		Console.Write(i);
	}
}

/**
 * no methods: empty list
 * 
 *
 */
class DSNOAVTest2 {
	
	public static int member = 5;
	public int member2 = 6;
}

}
