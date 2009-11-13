/*
<EXPECTED_METRICS>
DS_ATFD:[0,0,6,6]
</EXPECTED_METRICS>
 */

/**
 * Testclass for Access to Foreign Data(ATFD)
 *
 * Category/Partition Method:
 * 
 * REMEMBER:
 * JAVA: protected
 * C#: protected internal
 * 
 * Class
 *   {abstract ; not abstract}
 *   {related  ; unrelated   }
 *   
 * Method
 *   Visibility: private, protected internal, public
 * 	 Get/Set: getter/setter 
 * 	 Constructor: constructor 
 * 	 Static: static methods
 *
 * Attributes
 *  Visibility: private, protected internal, public, static, readonly  
 * 
 */


/**
 * This class is a utily class to test another classes in this same file.
 * Result: 0
 */
using System;

namespace metricTests
{
	public class DSATFDTest {

		/**
			* This attribute may not count, if it is called.
			* Cause: readonly attribute
			*/
			public static readonly int f = 1;


		/**
			* This attribute may not count, if it is called.
			* Cause: readonly attribute
			*/
			protected internal readonly int i4 = 4;

		/**
			* This attribute may not count, if it is called.
			* Cause: private and static attribute
			*/
			private static int x=1;

		/**
			* This method may not count, if it is called.
			* Cause: private and static method
			*/	 
		private static int getX() {
			return x;
		}

		/**
			* This method may not count, if it is called..
			* Cause: private and static method
			*/
		private static void setX(int x) {
			DSATFDTest.x = x;
		}

		/**
			* This attribute must be counted, if it is called.
			* Cause: protected internal and static attribute.
			*/
			protected internal static int x1=2;

		/**
			* This method must be counted, if it is called.
			* Cause: protected internal and static method
			*/
		protected internal static int getX1() {
			return x1;
		}

		/**
			* This method must be counted, if it is called.
			* Cause: protected internal and static method
			*/
		protected internal static void setX1(int x1) {
			DSATFDTest.x1 = x1;
		}

		/**
			* This attribute must be counted, if it is called.
			* Cause: public and static attribute
			*/
			public static string name= "FC";

		/**
			* This method may not count, if it is called.
			* Cause: public static and not a getter method
			*/

		public static string CompanyName() {
			return name;
		}

		/**
			* Constructurs may not count,if they are called,
			*
			*/
			public DSATFDTest()	{ }

		/**
			* This attribute must be counted, if it is called.
			* Cause: public attribute
			*/
			public string testName;
		/**
			* Constructurs may not count,if they are called,
			*
			*/
		public DSATFDTest(string test){
			this.testName=test;
		}

		/**
			* This attribute may not count, if it is called.
			* Cause: private attribute
			*/
			private int i = 2;

		/**
			* This method must be counted, if it is called.
			* Cause: public and a getter method
			*/
		public int getI() {
			return i;
		}

		/**
			* This method must be counted, if it is called.
			* Cause: public and a setter method
			*/
		public void setI(int i) {
			this.i = i;
		}

		/**
			* This attribute must be counted, if it is called.
			* Cause: protected internal attribute
			*/
			protected internal int i5;

		/**
			* This attribute must be counted, if it is called.
			* Cause: public attribute
			*/
			public int i3;

		/**
			* This method must be counted, if it is called.
			* Cause: public and a getter method
			*/
		public int getI3() {
			return i3;
		}

		/**
			* This method must be counted, if it is called.
			* Cause: public and a setter method
			*/
		public void setI3(int i3) {
			this.i3 = i3;
		}

		/**
			* This attribute must be counted, if it is called.
			* Cause: public and static attribute
			*/
			public static int i2 = 3;

		/**
			* This method may not count, if it is called.
			* Cause: static getter method
			*/
		public static int getI2() {
			return i2;
		}

		/**
			* This method may not count, if it is called.
			* Cause: static and a setter method
			*/
		public static void setI2(int i2) {
			DSATFDTest.i2 = i2;
		}

		/**
			* This attribute must be counted, if it is called.
			* Cause: protected internal attribute
			*/	
			protected internal string s2;

		/**
			* This attribute must be counted, if it is called.
			* Cause: A getter method and protected internal
			*/
		protected internal string getS2() {
			return s2;
		}

		/**
			* This attribute must be counted, if it is called.
			* Cause: A setter method and protected internal
			*/
		protected internal void setS2(string s2) {
			this.s2 = s2;
		}

		/**
			* This method may not count, if it is called from another classes.
			* Cause:not a getter method.
			*/
		public int getCla(){
			return 0;
		}

		/**
			* This method may not count, if it is called from another classes.
			* Cause:not a setter method.
			*/
		public int setCla(int i){
			int cnt=i;
			return cnt;
		}

		/**
			* This attribute may not count, if it is called.
			* Cause: readonly attribute
			*/
			public readonly int pubfin=10;

		/**
			* This attribute may not count, if it is called.
			* Cause: readonly attribute
			*/
			protected internal static readonly int prostafin=11;



		//	 Test cases start here for this class

		/**
			* Configuration:
		*   +public
			*   +constructur
			*   -accesses methods in the same class
			* Expected: no, the accesses methods are public and protected internal but in the same class.
			* Result ATFD:0
			* Result FDP:0
			*/
		public DSATFDTest(int att1){//, string att2){
			this.setI(att1);
			//this.setS2(att2);
		}

		/**
			* Configuration:
		*   +protected internal
			*   -accesses a method in the same class
			* Expected: no, the accesses method is protected internal but in the same class.
			* Result ATFD:0
			* Result FDP:0
			*/
		protected internal string method2(string s){
			s=getS2();
			return s;
		}
		/**
			* Configuration:
		*   +public
			*   -accesses a method in the same class
			* Expected: no, the accesses method is protected internal and public but in the same class.
			* Result ATFD:0
			* Result FDP:0
			*/
		public string method3(string s)	{
			s=getS2();
			int ge= getI3();
			return s+ge;
		}

		/**
			* Configuration:
		*   +public
			*   -accesses the methods in the same class
			* Expected: no, the accesses method is public and static but in the same class.
			* Result:0
			* Result FDP:0
			*/
		public static int method4(){
			int i= getX();
			setX(0);
			return i;
		}

		/**
			* Configuration:
		*  -protected internal
			*  -accesses a method in the same class
			* Expected: no, the accesses method is public and static but in the same class.
			* Result ATFD:0
			* Result FDP:0
			*/
		protected internal static int method5(){
			int i= getI2();
			return i;
		}

		/**
			* Configuration:
		*   +private
			*   -accesses a method in the same class
			* Expected: no, the accesses method is protected internal and public but in the same class.
			* Result ATFD:0
			* Result FDP:0
			*/
		private static int method6(){
			int i= getX1();
			return i;
		}

	}



	/**
		*Configuration: "related class", so we do not count this.
		*Result ATFD: 0
		*
		*/
	class DSATFDTest2 : DSATFDTest{
		public static readonly int k = 1;
		public int i;
	
		/**
			* 
			* Result FDP: 0
			*/
		public int method1(){
	
			i=getI(); // normally this is one, is public and getter. 
	
			string me2=name; // normally this is one, name is public and static. 
	
			return i;
		}
	
	
	}
	
	/**
		* 
		*Configuration: unrelated and abstract class
		* +Accesses Attributes: name,i2,i3,x1,i5.
		* +calls methods: getI,setI,getI3.
		*Result: 6
		*
		*/
	public abstract class DSATFDTest3 {
	
		/**
			* Configuration:
		* +accsses a public static attribute from foreign class.
			* Expected: yes, the the accesses attribute is public and static.
			* Result ATFD:1
			* Result FDP:1
			*/
		public DSATFDTest3(string na){
	
			na=DSATFDTest.name;
		}
		/**
			* Configuration:
		* - calls a constructur.
			* Expected: no, the called constructurs dont count.
			* Result ATFD:0
			* Result FDP: this does not count because no method.
			*/
			DSATFDTest atfdTest= new DSATFDTest();
	
		/**
			* Configuration:
		*  +accsses a public static attribute from foreign class.
			*  +accsses attribute is public.
			*  +calls a public getter method from foreign class.
			* Result ATFD:5
			* Result FDP: 1 (all calls to the same class)
			*/
		public void method1(){
	
			int i=DSATFDTest.i2; // this is one
	
			i=atfdTest.i3*3; // this is one
	
			Console.WriteLine(atfdTest.getI()); // this is one
	
			i=DSATFDTest.x1;     // this is one.
	
			float f= DSATFDTest.f; // this isn't one, because readonly.
	
			i= atfdTest.i5; // this is one.
	
		}	
	
		/**
			* Configuration:
		*  +calls a public setter method from foreign class.
			*  +calls a public static setter method from foreign class.
			*  +calls a public static setter method from foreign class.
			*  +calls a public getter method from foreign class.
			*  Result ATFD:2
			*  Result FDP:1
			*/
		protected internal int callMethods(){
	
			atfdTest.setI(0); // this is one
	
			DSATFDTest.setI2(12); // this is not one, the called method is static!
	
			int i=DSATFDTest.getI2(); // this is not one, the called method is static!
	
			DSATFDTest.setX1(i);  //this is not one, the called method is protected internal and static.
	
			i=atfdTest.getI3(); // this is one.
	
			i=DSATFDTest.getX1();  //this is not one, the called method is protected internal and static. 
	
			return i;	
		}
	
	}
	
	/**
		*Configuration: unrelated class
		* +Accesses Attributes: i2,name,x1,i5,s2
		* +calls methods: setS2,setI,getS2.
		*Result: 6
		*/
	class DSATFDTest4 {
	
		/**
			* -Called a Constructur
			* Result ATFD: 0
			*/
			DSATFDTest a= new DSATFDTest();
	
		int test;
	
		/**
			* Configuration:
		* +accsses a static attribute from foreign class.
			* Expected: yes, the the accesses attribute is public and static.
			* Result ATFD:1
			* Result FDP:1
			*/
		public DSATFDTest4(){
	
			this.test= DSATFDTest.i2; //this is one
		}
	
		/**
			* Configuration:
		*  +accsses a public static attribute from foreign class.
			*  +calls a public static method from foreign class.
			* Result ATFD:3
			* Result FDP: 1
			*/
		private int method4(int i){
	
			string me2=DSATFDTest.name; //this is one
	
			string me3=DSATFDTest.CompanyName(); // this is not one: not getter method
	
			i=a.i4; // this is not one, because f is readonly.
	
			i=a.pubfin; // this is not one, because f is readonly.
	
			i=DSATFDTest.x1; // this is one.
	
			i=DSATFDTest.prostafin; //this is not one, because is readonly.
	
			return a.i5; // this is one.
		}
	
		/**
			* Configuration:
		* +accesses attribute is public static.
			* Result ATFD:1
			* Result FDP: 1
			*/
		private void method5(int fal){
	
			string me2=DSATFDTest.name; //this is one
	
			a.setCla(fal); // not a setter method.
	
			int i=a.getCla(); // not a getter method.
	
		}
	
		/**
			* Configuration:
		* + called a public setter method
			* -The accesses attribute and called methods are protected internal.
			* Result ATFD:2
			* Result FDP:1
			*/
		public string method6(string i){
			
			i=a.s2; // this is one
			
			a.setI(0); // this is one
			
			return a.getS2(); 
		}
	
		/**
			* Configuration:
		* -The accesses attribute is public and static.
			* Result ATFD:0
			* Result FDP: 0
			*/
		private static string method7(){
			string me3=DSATFDTest.CompanyName(); // this is not one: static and not getter method
			return me3;
		}

	
		/**
			* Configuration:
		* +The accesses attribute is public and static.
			* Result ATFD:1
			* Result FDP:1
			*/
		protected internal static string method8(){
			return DSATFDTest.name; // this is one
		}
	
		/**
			* Configuration:
		* +The accesses attribute is public and static.
			* Result ATFD:1
			* Result FDP:1
			*/
		public static string method9(){
			string me3=DSATFDTest.name; // this is one
			return me3;
		}
	
		/**
			* Configuration:
		* +accees two different data provider classes
			* Result ATFD:1
			* Result FDP:3
			*/
		public static string method10(){
			string me3=DSATFDTest.name; // this is one
			string me2=DSATFDTest.name; // this is not one since name has already been accessed
			Console.WriteLine(DSATFDTest2.k); //this are two foreign accesses: one to System.out and one to f.
			return me3;
		}
	}
}