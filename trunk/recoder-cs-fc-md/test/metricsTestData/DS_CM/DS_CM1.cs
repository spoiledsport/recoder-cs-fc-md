/*
<EXPECTED_METRICS>
DS_CM:[[0,1,0,1,0],[0]]
</EXPECTED_METRICS>
 */
using System;

namespace metricTestsCC
{
	   public class Simple {

       public String firstname;
       public String lastname;
		int i;

       public Simple(int i) {
		this.i = i;
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
       
      Simple simple= new Simple(5);

      public String printName(){
      return simple.getFirstname()+simple.getLastname();
      }
}

	

}
