// This file is part of the RECODER library and protected by the LGPL.

package recoder;

import recoder.io.*;
import recoder.service.*;

public class CrossReferenceServiceConfiguration
    extends DefaultServiceConfiguration {

    /**
       The cross reference source info of this service configuration.
       This is a copy of the sourceInfo attribute, to avoid type casts.
    */
    private CrossReferenceSourceInfo crossReferencer;
    
    public CrossReferenceServiceConfiguration() throws InitializationException 
    {
    	
    }

    protected void makeServices() { 
	super.makeServices();
	crossReferencer = (CrossReferenceSourceInfo)super.getSourceInfo();
    }

    protected void initServices() throws InitializationException { 
	super.initServices();
	// nothing to do, cross reference is already initialized
    }

    /**
      Returns the cross reference source info service.
    */
    public final CrossReferenceSourceInfo getCrossReferenceSourceInfo() {
	return crossReferencer;
    }  
    
    /**
       The cross reference source info is a subclass of the source info,
       so this class simply overrides the source info factory method.
     */
    protected SourceInfo makeSourceInfo() {
	return new DefaultCrossReferenceSourceInfo(this);
    }  
}

