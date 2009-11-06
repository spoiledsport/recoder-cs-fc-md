package metrics.util;

import java.util.ArrayList;
import java.util.List;
import recoder.abstraction.ClassType;
import recoder.abstraction.Field;
import recoder.abstraction.Method;
import recoder.convenience.TreeWalker;
import recoder.csharp.CSharpSourceElement;
import recoder.csharp.ProgramElement;
import recoder.csharp.Reference;
import recoder.csharp.SourceElement;
import recoder.csharp.StatementBlock;
import recoder.csharp.declaration.ClassDeclaration;
import recoder.csharp.declaration.ConstructorDeclaration;
import recoder.csharp.declaration.EnumDeclaration;
import recoder.csharp.declaration.FieldSpecification;
import recoder.csharp.declaration.MethodDeclaration;
import recoder.csharp.declaration.TypeDeclaration;
import recoder.csharp.expression.operator.Conditional;
import recoder.csharp.expression.operator.New;
import recoder.csharp.reference.ConstructorReference;
import recoder.csharp.reference.FieldReference;
import recoder.csharp.reference.MethodReference;
import recoder.csharp.reference.SuperConstructorReference;
import recoder.csharp.reference.ThisConstructorReference;
import recoder.csharp.reference.ThisReference;
import recoder.csharp.reference.TypeReference;
import recoder.csharp.reference.VariableReference;
import recoder.csharp.statement.Catch;
import recoder.csharp.statement.Do;
import recoder.csharp.statement.For;
import recoder.csharp.statement.If;
import recoder.csharp.statement.Return;
import recoder.csharp.statement.Switch;
import recoder.csharp.statement.Try;
import recoder.csharp.statement.While;
import recoder.service.CrossReferenceSourceInfo;

/**
 * Util methods for DS Framework calculators.
 */
public final class Util
{ 
	
	/**
	 * Return all Methods of a Class, which are abstract and public or public and not static.
	 * Used by: NAS, PNAS
	 * @param List<Method>
	 * @return List<Method>
	 * 
	 */
	protected static List<Method> calculateAllMethods(List<Method> typ )
	{
		List<Method> restMethods = typ;
		// throw the Object Methods out from the Methodslist
		ArrayList<Method> res= new ArrayList<Method>();
		// add all methods	
		for(Method method: restMethods)
		{		
			if((method.isAbstract()&&(method.isPublic()))||((method.isPublic())&& !(method.isStatic()))){
				
				res.add(method);	
			}
			
		}					
		return res;
	}
	
	/**
	 * Check, if the Methods are overriden in the parentclasses
	 * and return not overridden methods.
	 * Used by: BOvR,NAS,PNAS
	 * @param List<Method> ParentClass
	 * @param List<Method> ChildClass
	 * @return List<Method>
	 */
	protected static List<Method> calculateOverridenMethods(List<Method> ParentClass,List<Method> ChildClass){
		
		List<Method> copyChildMethods=new ArrayList<Method>();
		copyChildMethods.addAll(ChildClass);
		
		for(Method methodlist: ChildClass){	
			for(Method paMethod:ParentClass){			
				//Delete the methods,if the names and the Returntypes are same as well ChildClass as ParentClass
				if((methodlist.getName().equals(paMethod.getName())) &&
					(methodlist.getReturnType()==paMethod.getReturnType()) &&
					(methodlist.getSignature().size()==paMethod.getSignature().size())){	
					if(methodlist.getSignature().size()==0){			
						copyChildMethods.remove(methodlist);						
					}else if(checkParameter(methodlist,paMethod)){
						copyChildMethods.remove(methodlist);
						
					}	
				}
			}	
			
		}
		
		return copyChildMethods;
	}


	/**
	 * 
	 * Check the parameter size and the typs of each parameter.
	 * Return true,if they are same as well in ChildClass as in Parentclass
	 * @param Method methodlist
	 * @param Method paMethod
	 * @return boolean
	 */
	private static boolean checkParameter(Method methodlist, Method paMethod)
	{
		int cnt=0;
		for(int i=0;i<methodlist.getSignature().size();i++){
			// TODO: is this really what we want??
			// was: if(fsp.getType()==m.getSignature().get(0)){ 
			if(methodlist.getSignature().getType(i)==paMethod.getSignature().getType(i)){	
			cnt++;
			}
		}
		if(cnt==methodlist.getSignature().size()){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check the distinct methods in the specificed method
	 * and return these method references.
	 * Used by: CDISP, CINT
	 * @param Method method
	 * @return List<Reference>
	 */
	protected static List<Reference> isDistinctMethod(Method method)
	{
		List<Reference> disMethods= new ArrayList<Reference>();

		TreeWalker walker = new TreeWalker((ProgramElement)method);
		// dont count the methods itself
		walker.next();
		while(walker.next()){
			try{
				ProgramElement pe = walker.getProgramElement();
				if(pe instanceof ConstructorReference &&
						!(pe instanceof SuperConstructorReference) &&
						!(pe instanceof ThisConstructorReference)){
					
					disMethods.add((((New) pe).getTypeReference()));
				}
				else if(( pe instanceof MethodReference)){
					disMethods.add((MethodReference) pe);
				}
			}catch(NullPointerException e){
				System.out.println("Warning - Util(6) - NullPointer - continue");
				continue;
			}
		}
		return disMethods;
	}
	
	/**
	 * Sorted the methods and constructurs in order.
	 * Used by: CDISP,CINT,CM,CYCLO,LAA,LOCC,LOCM,MAXNESTING,NOAV 
	 * @param List<Method>
	 * @return List<Method>
	 * 
	 */
	public static List<Method> bubSort(List<Method> methods){
		Method [] array= new Method[methods.size()];
		for(int i=0;i<methods.size();i++){
			array[i]=methods.get(i);
		}
		boolean swapped;
		int a=1;
		do{
			swapped=false;
			for(int i=0;i<array.length-1;i++){
				if(((array[i] instanceof MethodDeclaration || array[i] instanceof ConstructorDeclaration))&&
						(array[i+1] instanceof MethodDeclaration || array[i+1] instanceof ConstructorDeclaration)){
					
					SourceElement.Position start= ((CSharpSourceElement) array[i]).getStartPosition();
					SourceElement.Position start1= ((CSharpSourceElement) array[i+1]).getStartPosition();
				 
					if(start.getLine()>start1.getLine()){
						swap(array,i,i+1);
						swapped=true;
					}
				}
			}
		a++;
		}while(swapped);
		
			List<Method> sortedList= new ArrayList<Method>();
			
			for(int i=0;i<methods.size();i++){
				sortedList.add((Method) array[i]);
			}
			return sortedList;
	}
	
	/**
	 * it is only a util method for the method bubSort above
	 */
	private static void swap (Method [] array, int idx1, int idx2)
	{
	Method tmp = array[idx1];
	array[idx1] = array[idx2];
	array[idx2] = tmp;
	}
	
	/**
	 * All constructurs and methods in the specificed List without default constructur
	 * @param methods
	 * @return List<Method>
	 */
	protected static List<Method> getMethodAndConstructurDeclaration(List<Method> methods){
		List<Method> buffer= new ArrayList<Method>();
		for(Method method:methods){
			if((method instanceof MethodDeclaration || method instanceof ConstructorDeclaration))
			{
				buffer.add(method);
			}
		}
		return buffer;
	}
	
	
	/**
	 * Check, if the specified method reference is a setter/getter
	 * @param methodReference
	 * @param cu
	 * @param si
	 * @return boolean
	 */
	protected static boolean isGetterSetterCall(MethodReference mr, ClassType cu, CrossReferenceSourceInfo si) {
		ClassType originClassType = (si.getMethod(mr)).getContainingClassType();
		
		if(originClassType.equals(cu)) {
			return false;
		}
		
		ArrayList<FieldSpecification> afs = (ArrayList<FieldSpecification>) originClassType.getFields();
		
		for(Field fs : afs) {
			if(mr.getName().toLowerCase().matches("get"+fs.getName().toLowerCase())) {
				return true;
			} else if(mr.getName().toLowerCase().matches("set"+fs.getName().toLowerCase())) {
				return true;
			} 
		}
		
		return false;
	}
	
	
	/**
	 * Check, if the accesed field is a getter/setter and return this field.
	 * @param method reference
	 * @param classType
	 * @param sourceinfo
	 * @return Field
	 */
	protected static Field getAccessedField(MethodReference mr,ClassType cu, CrossReferenceSourceInfo si) {
		
		ClassType originClassType = (si.getMethod(mr)).getContainingClassType();
		ArrayList<FieldSpecification> afs = (ArrayList<FieldSpecification>) originClassType.getFields();
		
		for(Field fs : afs) {
			if(mr.getName().toLowerCase().matches("get"+fs.getName().toLowerCase())) {
				return (Field) fs;
			} else if(mr.getName().toLowerCase().matches("set"+fs.getName().toLowerCase())) {
				return (Field) fs;
			} 
		}
		
		return null;
	}

	/**
	 * Checking, if the specificed method is a getter/setter
	 * @param method
	 * @return boolean
	 */
	protected static boolean isGetterSetter(Method method) 
	{
		boolean res=false;
		
		if(method.getName().length()>=3){
			if(isGetter(method)|| isSetter(method)){
				res= true;
			}
		}
		return res;
	}

	/**
	 * It is a util method for the method isGetterSetter, check if a method is setter
	 * @param m
	 * @return boolean
	 */
	private static boolean isSetter(Method m)
	{
		String sub = m.getName().substring(0, 3);
		if((sub.equals("set"))&&setterUtility1(m)){
			
			if(setterUtility1(m)&&setterUtility2(m)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Util method for the method isSetter, check if a method is setter
	 * @param m
	 * @return boolean
	 */
	private static boolean setterUtility1(Method m)
	{
		List<FieldSpecification> classAttributes = (List<FieldSpecification>) m.getContainingClassType().getFields();
		
		if(m.getReturnType()==null && m.getSignature().size()==1){
			
			TreeWalker walker = new TreeWalker((ProgramElement)m);
			walker.next();
			
			while(walker.next()){
				try{
					ProgramElement pe = walker.getProgramElement();
					
					if(pe instanceof FieldReference){
						
						for(FieldSpecification fsp: classAttributes){
							
							if(fsp.getName().equals(((FieldReference)pe).getName())){	
								// TODO: is this really what we want??
								// was: if(fsp.getType()==m.getSignature().get(0)){ 	
								if(fsp.getType()==m.getSignature().getType(0)){ 	
									return true;
								}
							}
						}
					}
				}catch(NullPointerException e){
					System.out.println("Warning - Util(7) - NullPointer - continue");
					continue;
				}
			}
		}
		return false;
	}
	
	/**
	 * Util method for the method isSetter, check if a method is setter
	 * @param m
	 * @return boolean
	 */
	private static boolean setterUtility2(Method m)	{
		
		TreeWalker walker = new TreeWalker((ProgramElement)m);
		walker.next();
		
		while(walker.next()){
			try{
				ProgramElement pe = walker.getProgramElement();
			
				if(pe instanceof StatementBlock){
					walker.next();
					walker.next();
					pe = walker.getProgramElement();
				
					 if(pe instanceof FieldReference) {
						 
						walker.next();
						pe = walker.getProgramElement();
				
						if(pe instanceof ThisReference){
							walker.next();
							walker.next();
							pe = walker.getProgramElement();
						
							if(pe instanceof VariableReference){
								walker.next();
								walker.next();
								pe=walker.getProgramElement();
								
								if(pe==null){
									return true;
								}
							}
							
						}else
							walker.next();
							pe = walker.getProgramElement();
						if(pe instanceof VariableReference){
							return true;
						}
					 } 
				}
			}catch(NullPointerException e){
				System.out.println("Warning - Util(8) - NullPointer - continue");
				continue;
			}
		}	
		return false;
	}
	
	/**
	 * It is a util method for the method isGetterSetter, check if a method is a getter
	 * @param m
	 * @return boolean
	 */
	private static boolean isGetter(Method m){
		
		String sub = m.getName().substring(0, 3);
	
		if((sub.equals("get"))&& m.getSignature().isEmpty()&&getterUtility1(m)){
			return true;
		}
		return false;
	}
	
	/**
	 * Util method for the method isGetter, check if a method is getter
	 * @param m
	 * @return boolean
	 */
	private static boolean getterUtility1(Method m)
	{
		TreeWalker walker = new TreeWalker((ProgramElement)m);
		walker.next();
		while(walker.next()) 
		{
			try{
				ProgramElement pe = walker.getProgramElement();
				if(pe instanceof StatementBlock)
				{
					walker.next();
					pe = walker.getProgramElement();
					if(pe instanceof Return)
					{
	 				if(getterUtility2(m))
						{
							walker.next();
							pe = walker.getProgramElement();
							if(pe instanceof FieldReference){return true;}
						}
				    }			
				}
			}catch(NullPointerException e){
				System.out.println("Warning - Util(4) - NullPointer - continue");
				continue;
			}
		}
		return false;
	}
	
	/**
	 * Util method for the method isGetter, check if a method is getter
	 * @param m
	 * @return boolean
	 */
	private static boolean getterUtility2(Method m){
		List<FieldSpecification> classAttributes = (List<FieldSpecification>) m.getContainingClassType().getFields();
		TreeWalker walker = new TreeWalker((ProgramElement)m);
		walker.next();
		
		while(walker.next()){
			try{
				ProgramElement pe = walker.getProgramElement();
				if(pe instanceof FieldReference){
					for(FieldSpecification fsp: classAttributes){
						if(fsp.getName().equals(((FieldReference)pe).getName())){
						
							if(fsp.getType()==m.getReturnType()){ 	
								return true;
							}
							
						}
					}
				}
			}catch(NullPointerException e){
				System.out.println("Warning - Util(5) - NullPointer - continue");
				continue;
			}
		}
			
		return false;
	}

	/**
	 * Count, the Cyclo Metric Complexity in the specified method
	 * @param method
	 * @return int
	 */
	protected static int calculateCyclometicComplexity(Method method) {
		
		/* count is one, because we are calculating
		 * CMC= number of loop in a method+ 1
		 */
		int count =1;
		TreeWalker walker = new TreeWalker((ProgramElement)method);
		while(walker.next()) {
			try{
				ProgramElement pe = walker.getProgramElement();
				if(pe instanceof For) {
					count++;
				} else if(pe instanceof While) {
					count++;
				} else if(pe instanceof Do) {
					count++;
				} else if(pe instanceof If) {
					count++;
				} else if(pe instanceof Switch) {
					count++;
				}  else if(pe instanceof Conditional) {
					count++;
				} else if(pe instanceof Try) {
					count++;
				} else if(pe instanceof Catch) {
					count++;
				}
			}catch(NullPointerException e){
				System.out.println("Warning - Util(3) - NullPointer - continue");
				continue;
			}
		}
		
		return count;
	}
	
	/**
     * Calculate all the accessed methods in the specified method
     * @param method
     * @return List
     */

	protected static List<MethodReference> allAccessesMethods(Method method){
		List<MethodReference> accessesMethods= new ArrayList<MethodReference>();
		TreeWalker walker = new TreeWalker((ProgramElement)method);
		while(walker.next()) {
			try{
				ProgramElement pe = walker.getProgramElement();
				if(pe instanceof MethodReference){
					accessesMethods.add((MethodReference) pe);
				}
			}catch(NullPointerException e){
				System.out.println("Warning - Util(2) - NullPointer - continue");
				continue;
			}
		}
		return accessesMethods;
	}
	

	/**
	 * Calculate all the accessed fields in the specified method
	 * @param method
	 * @return List<FieldReference>
	 */
	protected static List<FieldReference> allAccessesAttributes(Method method){
		List<FieldReference> accessesMethods= new ArrayList<FieldReference>();
		TreeWalker walker = new TreeWalker((ProgramElement)method);
		while(walker.next()) {
			try{
				ProgramElement pe = walker.getProgramElement();
				if(pe instanceof FieldReference ){
					accessesMethods.add((FieldReference) pe);
				}
			}catch(NullPointerException e){
				System.out.println("Warning - Util(1) - NullPointer - continue");
				continue;
			}
			
		}
			return accessesMethods;
	}
	

	/**
	 * Check, if the accessed method referens is a getter/setter
	 * @param metref
	 * @return boolean
	 */
	protected static boolean isMethodReferesGetterSetter(MethodReference metref)
	{
		if(metref.getName().length()<3){
			return false;
		}
		String sub = metref.getName().substring(0, 3);
		if((sub.equals("get"))||(sub.equals("set")))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Get the parent class declaration from a Method Reference
	 * @param mthdref
	 * @return TypeDeclaration
	 */
	protected static TypeDeclaration getParentClassDeclaration(MethodReference mthdref) {
		ProgramElement p = (ProgramElement) mthdref;
	
		do {
		    p = p.getASTParent();
			
		    if (p instanceof ClassType || p instanceof EnumDeclaration) {
		    	return (TypeDeclaration) p;
		    }
		} while (p != null);
		return null;
	}
	
	/**
	 * Get the parent class declaration from a New Reference 
	 * @param mthdref
	 * @return TypeDeclaration
	 */
	protected static TypeDeclaration getParentClassDeclaration(New mthdref) {
		
		ProgramElement p = (ProgramElement) mthdref;
		do {
		    p = p.getASTParent();
		    if (p instanceof ClassDeclaration || p instanceof EnumDeclaration) {
		    	return (TypeDeclaration) p;
		    }
		} while (p != null);
		return null;
	}
	
	/**
	 * Get the parent class declaration from a Constructur Reference
	 * @param mthdref
	 * @return TypeDeclaration
	 */ 
	protected static TypeDeclaration getParentClassDeclaration(ConstructorReference mthdref) {
		ProgramElement p = (ProgramElement) mthdref;
	
		do {
		    p = p.getASTParent();
			
		    if (p instanceof ClassType || p instanceof EnumDeclaration) {
		    	return (TypeDeclaration) p;
		    }
		} while (p != null);
		return null;
	}
	
	/**
	 * Check the fields in foreignFields which are unrelated with the measured class or in the same hierarchy as the measured class
	 * @param foreignFields
	 * @param list
	 * @return ArrayList<Field>
	 */
	protected static ArrayList<Field> getUnrelatedFields(ArrayList<Field> foreignFields,List<? extends Field> list){
		ArrayList<Field> puffer= new ArrayList<Field>();
		for(Field foreign:foreignFields ){
			if(!list.contains(foreign)){
				puffer.add(foreign);
			}
		}
		return puffer;
	}	
	
	/**
	 * Return all the method and constructurs referens, which are related with the measured class or in the same hierarchy as the measured class 	 
	 * @param ArrayList<Reference>
	 * @param List<Method>
	 * @return ArrayList<Reference>
	 */
	protected static ArrayList<Reference> getRelatedMethods(ArrayList<Reference> foreignmethods,List<? extends Method> list){
		ArrayList<Reference> puffer= new ArrayList<Reference>();
		
		for(Reference foreign:foreignmethods )
		{

			for(Method parentMethod: list){

				if(foreign instanceof MethodReference){
					
					if(parentMethod.getName().equals(((MethodReference) foreign).getName())){
						puffer.add(foreign);
					}
				}else if(parentMethod.getName().equals(((TypeReference) foreign).getName())){
					
					puffer.add(( (TypeReference)foreign));
				}
				
			}
		}
		return puffer;
	}
	
	
	/**
	 * Return all the methods and constructurs without default Constructur
	 * @param List<Method>
	 * @return List<Method>
	 */
	protected static List<Method> getMethodsWithoutDefaultConst(List<Method> allMethods){
		
		ArrayList<Method> puffer= new ArrayList<Method>();
		
		for(Method m: allMethods){
			if(m instanceof ConstructorDeclaration || m instanceof MethodDeclaration){
			puffer.add(m);
			}
		}
		return puffer;
	}
}
