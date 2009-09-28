using System;

namespace Cars
{

public class Harry : Person
{
    public int age;

	
	public Harry()
    {
        Console.WriteLine("Child Constructor.");

    }

	public String toString() {
		return this.name + " : " + this.age;
	}
	
	public String myAddedService() {
		return "more Service";
	}
	
	// nesting test
	private int methodNesting(int k){
		int i=k;
		if(k==0){
			k=1;
		}else
			for(int cnt=0; cnt<100;cnt++){
		    	i= (int) (Math.random()*cnt+1);
				if(i==13){
	        		i=0;
	        	}
		}
		return i;
	  }
	

}
}