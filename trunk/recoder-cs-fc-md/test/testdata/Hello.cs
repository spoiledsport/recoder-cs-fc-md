
public class SayHello 
{
    int _int;

    public static void Main(string[] args) 
    {
        System.Console.WriteLine("Hello World!"+_int);
    }
    
    public SayHello this[int i] {
    	get {
    		return this;
    	}
    }
    
}
