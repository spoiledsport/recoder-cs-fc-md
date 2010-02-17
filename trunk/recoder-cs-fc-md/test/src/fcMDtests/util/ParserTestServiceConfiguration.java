package fcMDtests.util;
// This file is part of the RECODER library and protected by the LGPL.

import recodercs.io.ProjectSettings;
import recodercs.io.SourceFileRepository;
import recodercs.service.ChangeHistory;
import recodercs.service.ConstantEvaluator;
import recodercs.service.ImplicitElementInfo;
import recodercs.service.NameInfo;
import recodercs.service.SourceInfo;
import recodercs.*;
import recodercs.csharp.CSharpProgramFactory;
import recodercs.io.*;
import recodercs.service.*;
import recodercs.ProgramFactory;
import recodercs.ServiceConfiguration;

public class ParserTestServiceConfiguration extends ServiceConfiguration {

    public ParserTestServiceConfiguration() throws InitializationException {
        super();
    }

    protected ProjectSettings makeProjectSettings() {
        return new ProjectSettings(this);
    }

    protected ProgramFactory makeProgramFactory() {
        return CSharpProgramFactory.getInstance();
    }

    /**
     * @see recodercs.ServiceConfiguration#initServices()
     */
    protected void initServices() throws InitializationException {
        programFactory.initialize(this);
        projectSettings.initialize(this);
    }

    /**
     * @see recodercs.ServiceConfiguration#makeServices()
     */
    protected void makeServices() {
        programFactory = makeProgramFactory();
        projectSettings = makeProjectSettings();
    }

    /**
     * @see recodercs.ServiceConfiguration#makeChangeHistory()
     */
    protected ChangeHistory makeChangeHistory() {
        return null;
    }

    /**
     * @see recodercs.ServiceConfiguration#makeConstantEvaluator()
     */
    protected ConstantEvaluator makeConstantEvaluator() {
        return null;
    }

    /**
     * @see recodercs.ServiceConfiguration#makeImplicitElementInfo()
     */
    protected ImplicitElementInfo makeImplicitElementInfo() {
        return null;
    }

    /**
     * @see recodercs.ServiceConfiguration#makeNameInfo()
     */
    protected NameInfo makeNameInfo() {
        return null;
    }

    /**
     * @see recodercs.ServiceConfiguration#makeSourceFileRepository()
     */
    protected SourceFileRepository makeSourceFileRepository() {
        return null;
    }

    /**
     * @see recodercs.ServiceConfiguration#makeSourceInfo()
     */
    protected SourceInfo makeSourceInfo() {
        return null;
    }

}
