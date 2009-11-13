/*
<EXPECTED_METRICS>
DS_WMC:[14,null]
</EXPECTED_METRICS>
 */
using System;
using System.Collections.Generic;


namespace metricTests
{
	

	/**
 * Testclass for Weighted Method Count(WMC)
 *
 * Category/Partition Method:
 * 
 * Class
 *   {abstract ; not abstract}
 *   
 * Method
 *   Visibility: private, protected, public
 * 	 Get/Set: getter/setter 
 * 	 Constructor: constructor 
 * 	 Static: static methods
 *   Abstract: abstract methods
 *
 * The reason of Statical Complexity
 * for,while,do,if,else,case,else-if,Conditional,try,catch
 * 
 */


/**
 *Configuration: not abstract class
 *Complexity: 26
 *
 */
	public class DS_WMC_1
	{
		
			String name;
	
	/**
	 * Constructur
	 * This constructur doesnt have a Statical Complexty. 
	 * Complexity=1
	 */
	public DS_WMC_1(String name){
		this.name=name;
	}
	
	/**
	 * +private
	 * Statical Complexity:if,else,for,while,else-if.
	 * Complexity=5+1=6
	 */
	private int method2(int k){
		int i=3;
		
		if(k==0 && i==3){ //this is one: if 
			k=1;
		}else 
			if(k==2 && i==3){ //this is one:if 
			
			for(int cnt=0; cnt<100;cnt++){   //this is one:for
				
				i= (int) (3*cnt+1);
				
				while(true){   //this is one:while
					Console.WriteLine("");
				}
			}
		}else 
			if(k==4 || k==6){ //this is one: if
			
			return k;
			
		}else {return 12;} 
		return i;
	}
	
	/**
	 * +protected
	 * Statical Complexity:switch and cases
	 * Complexity=1 +1 = 2
	 */
	protected void printMonth(int month){
	        switch (month) {
	            case 1:  Console.WriteLine("January"); break;
	            case 2:  Console.WriteLine("February"); break;
	            case 3:  Console.WriteLine("March"); break;
	            case 4:  Console.WriteLine("April"); break;
	            case 5:  Console.WriteLine("May"); break;
	            case 6:  Console.WriteLine("June"); break;
	            case 7:  Console.WriteLine("July"); break;
	            case 8:  Console.WriteLine("August"); break;
	            case 9:  Console.WriteLine("September"); break;
	            case 10: Console.WriteLine("October"); break;
	            case 11: Console.WriteLine("November"); break;
	            case 12: Console.WriteLine("December"); break;
	            default: Console.WriteLine("Invalid month.");break;
	        }
	 }
	/**
	 * +public
	 * +static
	 * Statical Complexity:do, while, try and catch clausel.
	 * Complexity= 4 +1 =5
	 */
	public static int method3(int myList){
		
		int[] fibarray = new int[] { 0, 1, 2, 3, 5, 8, 13 };
        foreach (int i in fibarray)
        {
            System.Console.WriteLine(i);
        }
        
		
		int i=0;
		do{ // this is one:do-while loop
			try{ // this is one:try
			i=i+2;
			}catch (Exception e) { // this is one catch.

			}
		}while(i<myList);
		return i;
	}


}
	/**
 *Configuration: abstract class
 *Complexity: null
 *
 */
	public abstract class DS_WMC_2
	{
			/**
	 * +public, abstract
	 * this method doesnt have a Statical Complexty.
	 * Normally Complexity=0
	 */
	public abstract void method1();
	
	/**
	 * +private
	 * Statical Complexity:if,else,for,while,else-if,logical-OR,logical-AND.
	 * Normally Complexity=5+1=6
	 */
	private int method2(int k){
		int i=3;
		
		if(k==0){ //this is one: if
			k=1;
		}else 
			if(k==2 && i==3){ //this is one:else-if and logical-AND
			
			for(int cnt=0; cnt<100;cnt++){   //this is one:for
				
				i= (int) (4*cnt+1);
				
				while(true){   //this is one:while
					Console.WriteLine("");
				}
			}
		}else 
			if(k==4){ //this is one:if
			return k;
		}
		return i;
	}
	}
}

