package simpleExp;
import recoder.ModelElement;
import recoder.service.DefaultErrorHandler;

/**
 * @author kis
 *
 * Custom Error Handler for Sourcerer.
 */
public class PlainAnalysisErrorHandler extends DefaultErrorHandler {

	/**
	 * Constructor for PlainAnalysisErrorHandler.
	 */
	public PlainAnalysisErrorHandler() {
		super();
	}

	/**
	 * Constructor for PlainAnalysisErrorHandler.
	 * @param errorThreshold
	 */
	public PlainAnalysisErrorHandler(int errorThreshold) {
		super(errorThreshold);
	}

	/**
	 * @see recoder.service.DefaultErrorHandler#isReferingUnavailableCode(ModelElement)
	 */
	protected boolean isReferingUnavailableCode(ModelElement me) {
		return true;
	}

}
