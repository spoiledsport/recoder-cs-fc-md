/*
<EXPECTED_METRICS>
DS_MAXNESTING:[[0,2,3]]
</EXPECTED_METRICS>
 */
using System;

namespace metricTestsMAX
{
	
	
    class Example{

   public String lastName;
   public String FirstName;

   public Example(){
    Console.WriteLine("HELLO");
   }
   

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
    private int methodExample(int k){
        int i=k;
        if(k==0){
	  k=1;
	}else
	for(int cnt=0; cnt<100;cnt++){
	      i= (int) (42*cnt+1);
       if(i==13){
        i=0;
        }
	}
		return i;
    }

}
}