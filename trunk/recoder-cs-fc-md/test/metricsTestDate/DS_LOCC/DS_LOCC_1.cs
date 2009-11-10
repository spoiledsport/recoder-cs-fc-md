/*
<EXPECTED_METRICS>
DS_LOCM:[[6,9,11,5,1,6,3,5]]
DS_LOCC:[46]
</EXPECTED_METRICS>
 */
using System;

namespace metricTests
{
	
	
	public class DS_LOCC_1
	{
			
		private int x = 1;
		
		// la
		// Constructor 1
		// la2
		public DS_LOCC_1(){
			x=1;
			x++;
			//comment
		}
		
		/// <summary>
		/// doc
		/// </summary>
		public void method1(){
			x++;
			x=3;
			
			//comment
			
			int y=5;
			y+=x;
		}
		
		/// <summary>
		/// doc
		/// </summary>
		protected void method2(){
			x++;
			x=3;
			method1(
					
			);
			//comment
			
			int y=5;
			y+=x;
		}//comment
		//comment
		
		/// <summary>
		/// doc
		/// </summary>
		private void method3()
		{
			x++;
			int y=5;
			y+=x;}//comment
		//comment
		
		private int method4(int z){return z+x;}
		
		//Constructor 2
		public DS_LOCC_1(int x){
			this.x=x;
			x++;
			//comment
		}
		
		public int getX(){
			return x;
		}
		
		//comment
		static void Main(string[] args)
		{
			DS_LOCC_1 t = new DS_LOCC_1();
			int x = t.getX();
		}
	}
}
