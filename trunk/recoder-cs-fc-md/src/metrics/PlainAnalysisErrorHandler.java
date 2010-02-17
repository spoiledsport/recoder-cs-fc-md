package metrics;
import recodercs.ModelElement;
import recodercs.service.DefaultErrorHandler;

/**
 *
 * Custom Error Handler for recoder-cs-fc-md
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
	 * @see recodercs.service.DefaultErrorHandler#isReferingUnavailableCode(ModelElement)
	 */
	protected boolean isReferingUnavailableCode(ModelElement me) {
		return true;
	}

}
