using System;

public class Blah {

    enum Bar {
        a = MyEnum.Foo,
        b = A.c,
        c = MyEnum.Bar,
        d = myconstant
    }
    
    public enum MyEnum : byte {
        Foo = 254,
        Bar = B.y
    }

    enum A {
        a, b, c
    }
    
    enum B {
        x, y, z
    }
    
    enum AA : byte { a, b }
    enum BB : ulong { x, y }

    const int myconstant = 30;
    
    public static int Main ()
    {
        byte b = (byte) MyEnum.Foo;
        
        Console.WriteLine ("Foo has a value of " + b);

        if (b != 254)
            return 1;
        
        int i = (int) A.a;
        int j = (int) B.x;
        int k = (int) A.c;
        int l = (int) AA.b + 1;

        if (i != j)
            return 1;

        if (k != l)
            return 1;

        A var = A.b;

        i = (int) Bar.a;

        if (i != 254)
            return 1;

        i = (int) Bar.b;

        if (i != 2)
            return 1;

        j = (int) Bar.c;

        if (j != 1)
            return 1;

        j = (int) Bar.d;

        if (j != 30)
            return 1;

        Console.WriteLine ("Enum emission test okay");
        return 0;
    }
}
