//
// Tests the various string implicit conversions
//

class XX {

    enum X {
        A = 1
    }
    
    static int Main ()
    {
        int one = 1;
        int two = 2;
        
        if (("a" + "b") != "ab")
            return 1;

        if (("one" + one) != "one1")
            return 2;

        if ((one + "one") != "1one")
            return 3;

        if ((one + "two" + two) != "1two2")
            return 4;

        if ((X.A + "a") != "Aa")
            return 5;

        if (((int)X.A) + "a" != "1a")
            return 6;
        
        System.Console.WriteLine ("test ok");
        return 0;
    }
}
    
