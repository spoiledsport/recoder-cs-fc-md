/*
<EXPECTED_METRICS>
DS_CYCLO:[[2,1],[2,2,2,2,2,2],[8,3,4,2,3,2]]
</EXPECTED_METRICS>
*/


/**
 * Test case for Cycomatic Complexity
 * 
 * (calculated as Decision Points + 1
 * 
 * C/P
 * - no cyclomatic complexity 
 * - low cyclomatic complecity
 * - high cyclomatic complexity
 * 
 */

/**
 * no cyclo
 */
using System;

namespace metricTests
{  
public class DSCYCLOTest {

	public static void main(String[] args){
		
		int[] fibarray = new int[] { 0, 1, 2, 3, 5, 8, 13 };
        foreach (int i in fibarray)
        {
            System.Console.WriteLine(i);
        }

	}
	
	public void compute(int input){
		
	}
	
}

/**
 * low cyclomatic complexity
 */
class DSCYCLOTest_2 {

	public static void main(String[] args){
		if(true){
			//internal(true);
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
 * high cyclomatic complexity
 */
class DSCYCLOTest_3 {

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
			for(int i = 0 ;i <5; i++){
				for(int j = 0 ;i <5; i++){
					for(int k = 0 ;i <5; i++){
						Console.WriteLine(i);
					}
				}
			}
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
