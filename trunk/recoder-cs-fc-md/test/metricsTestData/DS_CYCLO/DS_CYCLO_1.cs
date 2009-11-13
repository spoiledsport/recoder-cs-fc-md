/*
<EXPECTED_METRICS>
DS_CYCLO:[[3]]
</EXPECTED_METRICS>
 */
using System;

namespace metricTests
{    
  class ExampleCyclo{

   public String lastName;
   public String FirstName;

   public int exampleMethod(int x,int y, int z){
     //multiply x,y
       x=x*y;
      for(int i=0; i<10; i++){
       if(x== 12){
        x=x+y;
        }
       }
     //multiply result with z
     z=x*z;
     return z;
  }
}
}
