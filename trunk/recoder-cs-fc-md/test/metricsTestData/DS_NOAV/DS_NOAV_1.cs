/*
<EXPECTED_METRICS>
DS_NOAV:[[2,1],[0,6]]
</EXPECTED_METRICS>
 */
using System;

namespace metricTests
{
	public class ClassExamle
	{
		
		int name;
		
		public ClassExamle(int name)
		{
			this.name = name;
		}
		
		public int getName() {
			return name;
		}
	}
	
	public class DS_NOAV0
	{
		
		public DS_NOAV0()
		{
		}
		
		/*
		Java doc: This method multiplies three numbers.
		*/
		public int exampleMethod(int x,int y, int z){
			//multiply x,y
	
			x=x*y;
			ClassExamle test= new ClassExamle(42);
			int name = test.getName();
			//multiply result with z
			z=x*z;
	
			return name+z;
  		}
		

	}
}
