/*
 * Tests the ?: operator and the string concatenation
 */

using System;
class X {
    static int Main (string [] args)
    {
        string a = "hello";
        string b = "1";
        string c = a + b;
        string d = a + 1;
        string y;
        
        if (c != d)
            return 1;
        if (d != (a + b))
            return 2;
        if (d != x (a, b))
            return 3;
        if (d != x (a, 1))
            return 4;

        y = c == d ? "equal" : "not-equal";
        if (y != "equal")
            return 5;
        y = b == a ? "oops" : "nice";
        if (y != "nice")
            return 6;
        
        Console.WriteLine (c);
        return 0;
    }

    static string s (string a, int o)
    {
        return a + o;
    }
    static string x (string s, object o)
    {
        return s + o;
    }

}
