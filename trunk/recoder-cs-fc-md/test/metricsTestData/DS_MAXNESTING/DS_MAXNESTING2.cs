/*
<EXPECTED_METRICS>
DS_MAXNESTING:[[0,0],[1,1,1,1,1,1],[4,2,3,1,2,1]]
</EXPECTED_METRICS>
*/
using System;

namespace metricTests
{
	
	
//**
// * Maximum Nesting Level
// * 
// * C/P Method:
// * 
// * - no nesting
// * - low nesting
// * - high nesting
// * 
// */
//
///**
// * no nesting
// */
public class DSMAXNESTINGTest {

	public static void main(String[] args){
		
		int i = 0;
		
		i++;
	}
	
	public int compute(int input){
		return input+input;
	}
	
}

/**
 * low nesting
 */
class DSMAXNESTINGTest_2 {

	public static void main(String[] args){
		if(true){
			Console.WriteLine(true);
		}
		else
		{
			Console.WriteLine(false);
		}
	}
	
	public int compute(int input){
		for(int i = 0 ;i <5; i++){
			Console.WriteLine(i);
		}
		return 6;
	}
	
	private void compute2(int input){
		while(input<5){
			Console.WriteLine("haha");
			input++;
		}
		
	}
	
	private void compute3(int input){
		switch(input){
		case 0: Console.WriteLine("foo");break;
		case 1: Console.WriteLine("bar");break;
		default: Console.WriteLine("!");break;
		}
		
	}
	
	private int compute4(int x, int y){
		
		return (x < y ? x : y);
		
	}
	
	private void compute5(int input){
		do{
			Console.WriteLine("foo");
		}
		while(input <10);
		
	}
}

/**
 * high nesting
 */
class DSMAXNESTINGTest_3 {

	public static void main(String[] args){
		if(true){
			for(int i = 0 ;i <5; i++){
				for(int j = 0 ;i <5; i++){
					for(int k = 0 ;i <5; i++){
						Console.WriteLine(i);
					}
				}
			}
		}
		else
		{
			Console.WriteLine(false);
		}
	}
	
	public int compute(int input){
		for(int i = 0 ;i <5; i++){
			Console.WriteLine(i);
			while(input<5){
				Console.WriteLine("haha");
				input++;
			}
		}
		return 6;
	}
	
	private void compute2(int input){
		int x = 0;
		int y = 0;
		while(input<5){
			Console.WriteLine("haha");
			input++;
			switch(input){
				case 0: Console.WriteLine("foo");break;
				case 1: input = (x < y ? x : y);break;
				default: Console.WriteLine("!");break;
			}
		}
		
	}
	
	private void compute3(int input){
		switch(input){
		case 0: Console.WriteLine("foo");break;
		case 1: Console.WriteLine("bar");break;
		default: Console.WriteLine("!");break;
		}
		
	}
	
	private int compute4(int x, int y){
		
		return (x < y ? x : (x<y?x:y));
		
	}
	
	private void compute5(int input){
		do{
			Console.WriteLine("foo");
		}
		while(input <10);
		
	}
}

}

