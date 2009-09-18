
using System;

delegate SayHello sayhello(string whattosay);



public class SayHello : System.Hello
{
    int _int;
    
    string str;
    
    object o;
    
    sayhello y;
    
    public event sayhello Hi;
    
    public SayHello x(String s) {
        return this;
    }

    public static void Main(string[] args) 
    {
        System.Console.WriteLine("Hello World!"+_int);
        (new SayHello()).run();
    }
    
    public SayHello q(String x) {
    	return this;
    }
    
    public void run() {
        int z;
        y = new sayhello(x);
        y += new sayhello(q);
        y("Abrakadabra");
        Hi("Hello World");
        z = 1 + 2;
        
    }
    
}
