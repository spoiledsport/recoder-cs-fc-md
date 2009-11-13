
using System;

namespace metricTests
{
	public class Example{
     
        private int i5;
        public readonly int i=7;
        public  string pin="CodeVizard";
   	
        public Example(){ }
	
		public int getI5() {
			return i5 +1;
		}

		public void setI5(int i5) {
			this.i5 = i5;
		}
	}
	
	public class ATFDsimple
	{
		
		public ATFDsimple()
		{
		}

    	public static void Main(string[] args){
	
			Example ex= new Example();
	    	int cnt;
	    	
			int fi=ex.i; // this is not one, because readOnly.
			
			ex.setI5(42);
	  		cnt=ex.getI5();
	   		string name= ex.pin;
			Console.WriteLine("got I5 from example: " + cnt);
			Console.WriteLine("get pin from example: " + name);
    	}
		
		
	}
}
