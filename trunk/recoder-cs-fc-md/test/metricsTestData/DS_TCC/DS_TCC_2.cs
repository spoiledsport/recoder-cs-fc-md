/*
<EXPECTED_METRICS>
DS_TCC:[0.5714285714285714,null]
</EXPECTED_METRICS>
 */

/**
 * Testclass for Tight Class Cohesion(TCC)
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
 *
 * Attributes
 *  Visibility: private, protected, public, static, final  
 * 
 */

using System;

namespace metricTests
{
	/**
	 * Class: not abstract
	 * Methods size:7
	 * Connected methods: 12
	 * 
	 */
	public class DSTCCTest {

		private int i;
		public static int cnt;
		private readonly int number=13;
		protected static readonly int number1=11;
	
		/**
		 * -Consturtur
		 * -dont counted.
		 * @param i
		 */
		public DSTCCTest(int i){
			this.i=i;
		}
	
		/**
		 * +public
		 * +accesses Attributes: i(private), number(private)
		 * +Connected Methods: setI,method1,connectAllMethods.
		 * 
		 */
		public int getI(){
			return i*number;
		}
		
		/**
		 * +public
		 * +accesses Attributes: i(private)
		 * +Connected Methods: getI,method1,connectAllMethods.
		 * 
		 */
		public void setI(int i){
			this.i = i;
		}
	
		/**
		 * +private
		 * +accesses Attributes: i(private)
		 * +Connected Methods: getI,setI,connectAllMethods.
		 * 
		 */
		private void method1(){
			Console.WriteLine(i);
		}
	
		/**
		 * +protected, static
		 * +accesses Attributes: cnt(public static)
		 * +Connected Methods: calculate1,calculate3,connectAllMethods.
		 * 
		 */
		protected static int calculate(){
			return cnt*cnt*cnt;
		}
	
		/**
		 * +public, static
		 * +accesses Attributes: cnt(public static)
		 * +Connected Methods: calculate,calculate3,connectAllMethods.
		 * 
		 */
		public static int calculate1(){
			return cnt*cnt*cnt;
		}
	
		/**
		 * +private, static
		 * +accesses Attributes: cnt(public static), number1(protected, static, final)
		 * +Connected Methods: calculate1,calculate,connectAllMethods.
		 * 
		 */
		private static int calculate3(){
			return cnt*number1;
		}
	
		/**
		 * +private
		 * +accesses Attributes: i(private), cnt(public static), number1(protected, static, final), number(private)
		 * +Connected Methods: getI,setI,method1,calculate,calculate1,calculate3.
		 * 
		 */
		private void connectAllMethods(){
			Console.WriteLine(i*cnt*number*number1);
		}

	}

	/**
	 * -Class: abstract
	 * -the abstract classes wont be counted.
	 * Result: null
	 */

	abstract class DSTCCTest1{
		private int i;

		public int getI(){
			return i;
		}
	
		public void setI(int i){
			this.i = i;
		}
	
		private void method1(){
			Console.WriteLine(i);
		}
	}
}
