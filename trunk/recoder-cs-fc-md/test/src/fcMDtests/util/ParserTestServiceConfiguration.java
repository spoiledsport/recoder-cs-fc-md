package fcMDtests.util;
// This file is part of the RECODER library and protected by the LGPL.

import recoder.io.*;
import recoder.service.*;
import recoder.csharp.CSharpProgramFactory;
import recoder.*;

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
     * @see recoder.ServiceConfiguration#initServices()
     */
    protected void initServices() throws InitializationException {
        programFactory.initialize(this);
        projectSettings.initialize(this);
    }

    /**
     * @see recoder.ServiceConfiguration#makeServices()
     */
    protected void makeServices() {
        programFactory = makeProgramFactory();
        projectSettings = makeProjectSettings();
    }

    /**
     * @see recoder.ServiceConfiguration#makeChangeHistory()
     */
    protected ChangeHistory makeChangeHistory() {
        return null;
    }

    /**
     * @see recoder.ServiceConfiguration#makeConstantEvaluator()
     */
    protected ConstantEvaluator makeConstantEvaluator() {
        return null;
    }

    /**
     * @see recoder.ServiceConfiguration#makeImplicitElementInfo()
     */
    protected ImplicitElementInfo makeImplicitElementInfo() {
        return null;
    }

    /**
     * @see recoder.ServiceConfiguration#makeNameInfo()
     */
    protected NameInfo makeNameInfo() {
        return null;
    }

    /**
     * @see recoder.ServiceConfiguration#makeSourceFileRepository()
     */
    protected SourceFileRepository makeSourceFileRepository() {
        return null;
    }

    /**
     * @see recoder.ServiceConfiguration#makeSourceInfo()
     */
    protected SourceInfo makeSourceInfo() {
        return null;
    }

}
