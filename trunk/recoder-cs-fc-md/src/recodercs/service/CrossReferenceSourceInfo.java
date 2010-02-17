// This file is part of the RECODER library and protected by the LGPL.

package recodercs.service;

import recodercs.abstraction.Constructor;
import recodercs.abstraction.Field;
import recodercs.abstraction.Method;
import recodercs.abstraction.Type;
import recodercs.abstraction.Variable;
import recodercs.service.SourceInfo;
import recodercs.*;
import recodercs.abstraction.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;
import recodercs.csharp.expression.*;
import recodercs.csharp.expression.operator.*;
import recodercs.csharp.reference.*;
import recodercs.list.*;


/** 
    Source information service supporting cross reference information.
    @author AL
 */
public interface CrossReferenceSourceInfo extends SourceInfo {

    /**
       Returns the list of references to a given method (or constructor).
       The references stem from all known compilation units.
       @param m a method.
       @return the possibly empty list of references to the given method.
     */
    MemberReferenceList getReferences(Method m);

    /**
       Returns the list of references to a given constructor. The references
       stem from all known compilation units.
       @param m a constructor.
       @return the possibly empty list of references to the given constructor.
     */
    ConstructorReferenceList getReferences(Constructor m);

    /**
       Returns the list of references to a given variable. The references
       stem from all known compilation units.
       @param v a variable.
       @return the possibly empty list of references to the given variable.
     */
    VariableReferenceList getReferences(Variable v);

    /**
       Returns the list of references to a given field. The references
       stem from all known compilation units.
       @param f a field.
       @return the possibly empty list of references to the given field.
     */
    FieldReferenceList getReferences(Field f);

    /**
       Returns the list of references to a given type. The references
       stem from all known compilation units.
       @param t a type.
       @return the possibly empty list of references to the given type.
     */
    TypeReferenceList getReferences(Type t);

    /**
       Returns the list of references to a given package. The references
       stem from all known compilation units.
       @param p a package.
       @return the possibly empty list of references to the given package.
     */
    NamespaceReferenceList getReferences(Namespace p);

}

