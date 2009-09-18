package sourcerer;

import recoder.ModelElement;
import recoder.service.DefaultErrorHandler;

/**
 * @author kis
 *
 * Custom Error Handler for Sourcerer.
 */
class SourcererErrorHandler extends DefaultErrorHandler {

	/**
	 * Constructor for SourcererErrorHandler.
	 */
	public SourcererErrorHandler() {
		super();
	}

	/**
	 * Constructor for SourcererErrorHandler.
	 * @param errorThreshold
	 */
	public SourcererErrorHandler(int errorThreshold) {
		super(errorThreshold);
	}

	/**
	 * @see recoder.service.DefaultErrorHandler#isReferingUnavailableCode(ModelElement)
	 */
	protected boolean isReferingUnavailableCode(ModelElement me) {
		return true;
	}

}
