
using System;

namespace Cars
{
	
	// an enum
	public enum Volume
	{
	   Low,
	   Medium,
	   High
	}
	
	// an abstract class
	abstract class ShapesClass
	{
	    abstract public int Area();
	}
	
	// an interface
	interface I1
		{
			void MyFunction();
		}
	
	// Declare a delegate.  
	delegate string strMod(string str);
	
	class A{}
	class B{}
	
}