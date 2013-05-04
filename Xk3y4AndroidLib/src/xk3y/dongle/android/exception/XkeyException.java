package xk3y.dongle.android.exception;

public class XkeyException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XkeyException() {
		super();
		
	}

	public XkeyException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public XkeyException(String detailMessage) {
		super(detailMessage);
	}

	public XkeyException(Throwable throwable) {
		super(throwable);
	}
	
	

}
