/*
<EXPECTED_METRICS>
DS_NOAM:[2]
</EXPECTED_METRICS>
 */
using System;

namespace metricTests
{
	
	
	public class DS_NOAM
	{
		
		int theNumber;
		
		public DS_NOAM()
		{
		}
		
		public int getTheNumber(){
			return this.theNumber;
		}
	
		public void setResult(int theNumber) {
			this.theNumber = theNumber;
		}
    

	}
}
