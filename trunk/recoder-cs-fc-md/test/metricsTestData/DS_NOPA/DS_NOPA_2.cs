/*
<EXPECTED_METRICS>
DS_NOPA:[3,null,0]
</EXPECTED_METRICS>
 */
using System;

namespace metricTests
{
	
	
/**
 * Testclass for Number of Public Attributes (NOPA)
 * 
 * Category/Partition Method:
 * 
 * Class
 *   {abstract ; not abstract}
 * Number of public attributes
 *   {0 ; many}
 * Mix of Attributes (e.g. public, private, protected, static, final)
 *   {random mix}
 * Types
 *   {different types}
 * Order
 *   {before and after method declaration}
 * 
 * Configurations:
 * 
 * DSNOPATest:  {not abstract, many, mix, different types, before and after}
 * DSNOPATest2: {abstract    , many, mix, different types, before and after}
 * DSNOPATest3: {not abstract, 0   , mix, different types, before and after}
 */
public class DSNOPATest {
	
	private int i = 0;
	public String s; //this is one
	public static int i2 = 0;
	public readonly double d = 0.0;
	public static readonly float f = 0f;
	protected String s2;
	public int i3; //this is one
	protected readonly int i4 = 4;
	
	public DSNOPATest(int i){
		i++;
		s="i="+i;
	}
	
	public String s3; //this is one
}

abstract class DSNOPATest2{
	private int i = 0;
	public String s;
	public static int i2 = 0;
	public readonly double d = 0.0;
	public static readonly float f = 0f;
	protected String s2;
	public int i3;
	protected readonly int i4 = 4;
	
	public DSNOPATest2(int i){
		i++;
		s="i="+i;
	}
	
	public String s3;
}

class DSNOPATest3{
	private int i = 0;
	public static int i2 = 0;
	public readonly double d = 0.0;
	public static readonly float f = 0f;
	protected String s2;
	protected readonly int i4 = 4;
	
	public DSNOPATest3(int i){
		i++;
	}
	
	
}
}
