using System;

namespace Cars
{
	class MainClass
	{
		public static void Main(string[] args)
		{
			Person horst = new Person("Horst");
			Console.WriteLine("Hello, " + horst.getName());
			String myname = horst.name;
		}
	}
}