/*
<EXPECTED_METRICS>
DS_NOPA:[2]
</EXPECTED_METRICS>
 */
using System;

namespace metricTests
{
	
	
   class Simple {

       public String firstname;
       public String lastname;
        private String nopa;
        protected int i;
        public readonly int cnt =1;
        public static int k = 12;
  
        public Simple() {
        }

        public String getFirstname() {
		String st="";
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

}
}
