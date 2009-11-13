/*
<EXPECTED_METRICS>
DS_TCC:[0.3333333333333333]
</EXPECTED_METRICS>
 */ 
using System;

namespace metricTests
{
class Example {

	private int i;
	
	// a directly connected method
	public Integer getI(){
		return i;
	}

	// a directly connected method
	public void setI(int i){
		this.i = i;
	}

	public void method1(){

	}

}
}
