/*
<EXPECTED_METRICS>
DS_NOAM:[6]
</EXPECTED_METRICS>
 */
using System;

namespace metricTests
{
	
	
	public class DS_NOAM
	{
		
		int theNumber;
		
		// property style getter setter
		public int roleId { get { return roleId; } set { roleId = value; } }
		public int FileSize { get { return _size; } set { _size = value; } }
		
		public DS_NOAM()
		{
		}
		
		public int getTheNumber(){
			return this.theNumber;
		}
	
		public void setTheNumber(int theNumber) {
			this.theNumber = theNumber;
		}
    

	}
}
