//
// System.Activator.cs
//
// Authors:
//   Nick Drochak II (ndrochak@gol.com)
//   Gonzalo Paniagua (gonzalo@ximian.com)
//
// (C) 2001 Nick Drochak II
// (c) 2002 Ximian, Inc. (http://www.ximian.com)
//

using System.Runtime.Remoting;
using System.Reflection;
using System.Globalization;
using System.Security.Policy;

namespace System 
{
    public sealed class Activator
    {
        private static BindingFlags _flags = BindingFlags.CreateInstance |
                             BindingFlags.Public |
                             BindingFlags.Instance;

        private Activator () {}

        [MonoTODO]
        public static ObjectHandle CreateComInstanceFrom (string assemblyName, string typeName)
        {
            throw new NotImplementedException(); 
        }

        public static ObjectHandle CreateInstanceFrom (string assemblyFile, string typeName)
        {
            return CreateInstanceFrom (assemblyFile, typeName, null);
        }

        public static ObjectHandle CreateInstanceFrom (string assemblyFile,
                                   string typeName,
                                   object [] activationAttributes)
        {
            return Activator.CreateInstanceFrom (assemblyFile,
                                 typeName,
                                 false,
                                 _flags,
                                 null,
                                 null,
                                 null,
                                 activationAttributes,
                                 null);
        }
        
        [MonoTODO]
        public static ObjectHandle CreateInstanceFrom (string assemblyFile,
                                   string typeName,
                                   bool ignoreCase,
                                   BindingFlags bindingAttr,
                                   Binder binder,
                                   object [] args,
                                   CultureInfo culture,
                                   object [] activationAttributes,
                                   Evidence securityInfo)
        {
            //TODO: when Assembly implements security, use it.
            //Assembly assembly = Assembly.LoadFrom (assemblyFile, securityInfo);
            Assembly assembly = Assembly.LoadFrom (assemblyFile);
            if (assembly == null)
                return null;

            Type type = assembly.GetType (typeName, true, ignoreCase);
            if (type == null)
                return null;

            object obj = CreateInstance (type, bindingAttr, binder, args, culture, activationAttributes);
            return (obj != null) ? new ObjectHandle (obj) : null;
        }
        
        public static ObjectHandle CreateInstance (string assemblyName, string typeName)
        {
            return Activator.CreateInstance (assemblyName, typeName, null);
        }
        
        public static ObjectHandle CreateInstance (string assemblyName,
                               string typeName,
                               object [] activationAttributes)
        {
            return Activator.CreateInstance (assemblyName,
                             typeName,
                             false,
                             _flags,
                             null,
                             null,
                             null,
                             activationAttributes,
                             null);
        }
        
        [MonoTODO]
        public static ObjectHandle CreateInstance (string assemblyName,
                               string typeName,
                               bool ignoreCase,
                               BindingFlags bindingAttr,
                               Binder binder,
                               object [] args,
                               CultureInfo culture,
                               object [] activationAttributes,
                               Evidence securityInfo)
        {
            //TODO: when Assembly implements security, use it.
            //Assembly assembly = Assembly.Load (assemblyFile, securityInfo);
            Assembly assembly = Assembly.Load (assemblyName);
            Type type = assembly.GetType (typeName, true, ignoreCase);
            object obj = CreateInstance (type, bindingAttr, binder, args, culture, activationAttributes);
            return (obj != null) ? new ObjectHandle (obj) : null;
        }
        
        public static object CreateInstance (Type type)
        {
            return CreateInstance (type, false);
        }
        
        public static object CreateInstance (Type type, object [] args)
        {
            return CreateInstance (type, args, new object [0]);
        }

        [MonoTODO]
        public static object CreateInstance (Type type, object [] args, object [] activationAttributes)
        {
            // activationAttributes?
            if (type == null)
                throw new ArgumentNullException ("type");

            int length = 0;
            if (args != null)
                length = args.Length;

            Type [] atypes = new Type [length];
            for (int i = 0; i < length; ++i) {
                atypes [i] = args [i].GetType ();
            }
            ConstructorInfo ctor = type.GetConstructor (atypes);
            if (ctor == null)
                return null;

            return ctor.Invoke (args);
        }

        public static object CreateInstance (Type type,
                             BindingFlags bindingAttr,
                             Binder binder,
                             object [] args,
                             CultureInfo culture)
        {
            return CreateInstance (type, bindingAttr, binder, args, culture, new object [0]);
        }

        [MonoTODO]
        public static object CreateInstance (Type type,
                             BindingFlags bindingAttr,
                             Binder binder,
                             object [] args,
                             CultureInfo culture,
                             object [] activationAttributes)
        {
            // activationAttributes?
            int length = 0;
            if (args != null)
                length = args.Length;

            Type[] atypes = new Type [length];
            for (int i = 0; i < length; ++i) {
                atypes [i] = args [i].GetType ();
            }
            //FIXME: when GetConstructor works with this parameter list, use it
            //ConstructorInfo ctor = type.GetConstructor (bindingAttr, binder, atypes, null);
            ConstructorInfo ctor = type.GetConstructor (atypes);
            if (ctor == null)
                return null;

            //FIXME: when Invoke allows this parameter list, use it
            //return ctor.Invoke (args, bindingAttr, binder, args, culture);
            return ctor.Invoke (args);
        }

        public static object CreateInstance (Type type, bool nonPublic)
        { 
            if (type == null)
                throw new ArgumentNullException ("type");
                
            ConstructorInfo ctor = type.GetConstructor (Type.EmptyTypes);
            if (ctor.IsPublic && nonPublic == true)
                return null;

            if (ctor == null)
                return null;

            return ctor.Invoke (null);
        }

        [MonoTODO]
        public static object GetObject (Type type, string url)
        {
            throw new NotImplementedException(); 
        }

        [MonoTODO]
        public static object GetObject (Type type, string url, object state)
        { 
            throw new NotImplementedException(); 
        }
    }
}

