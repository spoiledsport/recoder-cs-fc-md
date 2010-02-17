// This file is part of the RECODER library and protected by the LGPL.

package recodercs;

import recodercs.ProgramFactory;
import recodercs.io.ProjectSettings;
import recodercs.io.SourceFileRepository;
import recodercs.service.ChangeHistory;
import recodercs.service.ConstantEvaluator;
import recodercs.service.ImplicitElementInfo;
import recodercs.service.NameInfo;
import recodercs.service.SourceInfo;
import recodercs.io.*;
import recodercs.service.*;

/**
   A configuration of services that can work together.
   <P>
   To exchange a service S1 by a service S2,
   simply override the corresponding <CODE>makeS</CODE> method.
   <P>
   To provide a complete new Service S, add the following code to a subclass
   of the DefaultServiceConfiguration:
   <PRE>
   private S s;
   protected void makeServices() { super.makeServices(); s = makeS(); }
   protected void initServices() { super.initServices(); s.initialize(this); }
   protected S makeS() { return new S1(...); }
   public final S getS() { return s; }
   </PRE>
*/
public abstract class ServiceConfiguration {

	protected ProjectSettings projectSettings;
	protected ProgramFactory programFactory;
	protected ChangeHistory changeHistory;
	protected SourceFileRepository sourceFileRepository;
//	protected ClassFileRepository classFileRepository;
	protected SourceInfo sourceInfo;
	//    protected ByteCodeInfo byteCodeInfo;
	protected ImplicitElementInfo implicitElementInfo;
	protected NameInfo nameInfo;
	protected ConstantEvaluator constantEvaluator;

	public ServiceConfiguration() throws InitializationException {
		makeServices();
		initServices();
	}

	/**
	   Called during service initialization: constructs services.
	 */
	protected void makeServices() {
		changeHistory = makeChangeHistory();
		projectSettings = makeProjectSettings();
		programFactory = makeProgramFactory();
		sourceFileRepository = makeSourceFileRepository();
		implicitElementInfo = makeImplicitElementInfo();
		nameInfo = makeNameInfo();
//		classFileRepository = makeClassFileRepository();
		sourceInfo = makeSourceInfo();
//		byteCodeInfo = makeByteCodeInfo();
		constantEvaluator = makeConstantEvaluator();
	}

	/**
	   Called during service initialization: constructs services.
	 */
	protected void initServices() throws InitializationException {
		changeHistory.initialize(this);
		projectSettings.initialize(this);
		programFactory.initialize(this);
		sourceFileRepository.initialize(this);
		implicitElementInfo.initialize(this);
		nameInfo.initialize(this);

//		classFileRepository.initialize(this);
		sourceInfo.initialize(this);
//		 byteCodeInfo.initialize(this);
		constantEvaluator.initialize(this);
	}

	public final ProjectSettings getProjectSettings() {
		return projectSettings;
	}

	public final ProgramFactory getProgramFactory() {
		return programFactory;
	}

	public final ChangeHistory getChangeHistory() {
		return changeHistory;
	}

	public final SourceFileRepository getSourceFileRepository() {
		return sourceFileRepository;
	}

//	public final ClassFileRepository getClassFileRepository() {
//		return classFileRepository;
//	}

	public final SourceInfo getSourceInfo() {
		return sourceInfo;
	}

//	public final ByteCodeInfo getByteCodeInfo() {
//		return byteCodeInfo;
//	}

	public final ImplicitElementInfo getImplicitElementInfo() {
		return implicitElementInfo;
	}

	public final NameInfo getNameInfo() {
		return nameInfo;
	}

	public final ConstantEvaluator getConstantEvaluator() {
		return constantEvaluator;
	}

	protected abstract ProjectSettings makeProjectSettings();
	protected abstract ChangeHistory makeChangeHistory();
	protected abstract ProgramFactory makeProgramFactory();
	protected abstract SourceFileRepository makeSourceFileRepository();
//	protected abstract ClassFileRepository makeClassFileRepository();
	protected abstract SourceInfo makeSourceInfo();
//	protected abstract ByteCodeInfo makeByteCodeInfo();
	protected abstract ImplicitElementInfo makeImplicitElementInfo();
	protected abstract NameInfo makeNameInfo();
	protected abstract ConstantEvaluator makeConstantEvaluator();

}
