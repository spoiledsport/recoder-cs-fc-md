package tests;
import recodercs.CrossReferenceServiceConfiguration;
import recodercs.abstraction.ClassType;
import recodercs.abstraction.Field;
import recodercs.csharp.declaration.ClassDeclaration;
import recodercs.csharp.declaration.MemberDeclaration;
import recodercs.csharp.declaration.MethodDeclaration;
import recodercs.kit.TwoPassTransformation;
import recodercs.kit.problem.ProblemReport;
import recodercs.list.MemberDeclarationArrayList;
import recodercs.list.MemberDeclarationMutableList;

public class SimpleTransformation extends TwoPassTransformation {

	private ClassDeclaration cd;
	private MemberDeclarationMutableList mdl = new MemberDeclarationArrayList();

	public SimpleTransformation(CrossReferenceServiceConfiguration sc, ClassDeclaration cd) {
		super(sc);
		this.cd = cd;
	}

	public ProblemReport analyze() {
	
		try {
		Field[] fields = ((ClassType)cd).getAllFields().toFieldArray();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			if (! f.isPublic()) {
				String getMethod = "public "+f.getType().getFullName()+" get_"+f.getName()+"() { return "+f.getName()+"; }";
				String setMethod = "public void set_"+f.getName()+"("+f.getType().getFullName()+" val) { this."+f.getName()+" = val; }";
				System.out.println("Adding: "+getMethod);
				System.out.println("Adding: "+setMethod);
				
				MethodDeclaration md = getProgramFactory().parseMethodDeclaration(getMethod);
				mdl.add(md);		
				md = getProgramFactory().parseMethodDeclaration(setMethod);
				mdl.add(md);		
			}	
		}
		
		return setProblemReport(NO_PROBLEM);
		} catch (Exception e) {
			e.printStackTrace();
			return setProblemReport(IDENTITY);
		}
	}

	public void transform() {
		super.transform();
		MemberDeclaration[] mda = mdl.toMemberDeclarationArray();
		for (int i=0; i<mda.length; i++) 
    		attach(mda[i],cd,0);
		
	}

}
