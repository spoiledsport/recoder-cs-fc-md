
using System;

namespace Cars
{
	
	
	public class Child<T>
	{
		public String name;
		
		
		public Person(String name)
		{
			this.name = name;
		}
		
		public String getName() {
			for (int i = 0; i<10;i++);
			if (true) return this.name;
		}
		
		public String toString() {
			return this.name;
		}
	}
	
}