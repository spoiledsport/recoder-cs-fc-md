/*
<EXPECTED_METRICS>
DS_WOC:[1.0]
</EXPECTED_METRICS>
 */
using System;

namespace metricTestsWOC
{
	
	
    class Example {
   public static int number=11;   
    private string metricName= "WOC";

  public void setMetricName(String metricName){
      this.metricName= metricName;
    }
  public string getMetricName(){
      return metricName;
    }
   public static int calculate(){
    return number = number + 41;

    }
   public void print(){
    Console.WriteLine("CodeVizard");
    }

}
}
