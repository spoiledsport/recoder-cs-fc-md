// This file is part of the RECODER library and protected by the LGPL.

package recodercs;

import recodercs.ProgramFactory;
import recodercs.ServiceConfiguration;
import recodercs.io.DefaultSourceFileRepository;
import recodercs.io.ProjectSettings;
import recodercs.io.SourceFileRepository;
import recodercs.service.ChangeHistory;
import recodercs.service.ConstantEvaluator;
import recodercs.service.DefaultConstantEvaluator;
import recodercs.service.DefaultImplicitElementInfo;
import recodercs.service.DefaultNameInfo;
import recodercs.service.DefaultSourceInfo;
import recodercs.service.ImplicitElementInfo;
import recodercs.service.NameInfo;
import recodercs.service.SourceInfo;
import recodercs.csharp.CSharpProgramFactory;
import recodercs.io.*;
import recodercs.service.*;

public class DefaultServiceConfiguration extends ServiceConfiguration {

	public DefaultServiceConfiguration() throws InitializationException {
		super();
	}

	protected ProjectSettings makeProjectSettings() {
		return new ProjectSettings(this);
	}

	protected ChangeHistory makeChangeHistory() {
		return new ChangeHistory(this);
	}

	protected ProgramFactory makeProgramFactory() {
		return CSharpProgramFactory.getInstance();
	}

	protected SourceFileRepository makeSourceFileRepository() {
		return new DefaultSourceFileRepository(this);
	}

//	protected ClassFileRepository makeClassFileRepository() {
//		return new DefaultClassFileRepository(this);
//	}

	protected SourceInfo makeSourceInfo() {
		return new DefaultSourceInfo(this);
	}

//	protected ByteCodeInfo makeByteCodeInfo() {
//		return new DefaultByteCodeInfo(this);
//	}

	protected ImplicitElementInfo makeImplicitElementInfo() {
		return new DefaultImplicitElementInfo(this);
	}

	protected NameInfo makeNameInfo() {
		return new DefaultNameInfo(this);
	}

	protected ConstantEvaluator makeConstantEvaluator() {
		return new DefaultConstantEvaluator(this);
	}
}
