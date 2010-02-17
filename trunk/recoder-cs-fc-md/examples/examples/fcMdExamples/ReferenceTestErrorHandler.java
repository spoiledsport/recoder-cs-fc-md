package examples.fcMdExamples;
import recodercs.ModelElement;
import recodercs.service.DefaultErrorHandler;

/**
 * @author kis
 *
 * Custom Error Handler for Sourcerer.
 */
public class ReferenceTestErrorHandler extends DefaultErrorHandler {

	/**
	 * Constructor for PlainAnalysisErrorHandler.
	 */
	public ReferenceTestErrorHandler() {
		super();
	}

	/**
	 * Constructor for PlainAnalysisErrorHandler.
	 * @param errorThreshold
	 */
	public ReferenceTestErrorHandler(int errorThreshold) {
		super(errorThreshold);
	}

	/**
	 * @see recodercs.service.DefaultErrorHandler#isReferingUnavailableCode(ModelElement)
	 */
	protected boolean isReferingUnavailableCode(ModelElement me) {
		return true;
	}

}
