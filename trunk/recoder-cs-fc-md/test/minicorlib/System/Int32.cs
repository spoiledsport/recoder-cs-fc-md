//
// System.Int32.cs
//
// Author:
//   Miguel de Icaza (miguel@ximian.com)
//
// (C) Ximian, Inc.  http://www.ximian.com
//

using System.Globalization;
using System.Threading;

namespace System 
{
    
    [Serializable]
    public class Int32 
    {
        public string ToString() 
        {
        }

        public int length 
        {
            get 
            {
                return 1;
            }
        }
    }
}
