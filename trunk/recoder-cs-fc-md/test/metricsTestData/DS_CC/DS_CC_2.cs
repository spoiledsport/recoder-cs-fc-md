/*
<EXPECTED_METRICS>
DS_CC:[[1,1,0,1,0],[0]]
</EXPECTED_METRICS>
 */
using System;

namespace metricTests
{
	   public class Simple {

       public String firstname;
       public String lastname;

		public Simple(String fn) {
			this.firstname = fn;
		}

        public String getFirstname() {
		return firstname;
	}
	public void setFirstname(string firstname) {
		this.firstname = firstname;
	}

        public String getLastname() {
		return lastname;
	}
	public void setLastname(string lastname) {
		this.lastname = lastname;
	}
}
      public class Simple1{
       
		Simple simple= new Simple("fn");

      public String printName(){
      return simple.getFirstname()+simple.getLastname();
      }
}

	

}
