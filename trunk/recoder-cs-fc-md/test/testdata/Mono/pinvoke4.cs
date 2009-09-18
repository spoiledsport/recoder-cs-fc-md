using System;
using System.Runtime.InteropServices;

public class Test {

    [StructLayout (LayoutKind.Sequential)]
    public struct SimpleStruct {
        public bool a;
        public bool b;
        public bool c;
        public string d;
    }

    [DllImport ("libtest.so", EntryPoint="mono_test_return_vtype")]
    public static extern SimpleStruct mono_test_return_vtype ();


    public static int Main () {
        SimpleStruct ss = mono_test_return_vtype ();

        if (!ss.a && ss.b && !ss.c && ss.d == "TEST")
            return 0;
        
        return 1;
    }
}
