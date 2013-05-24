package xk3y.dongle.android.exception;

public class XkeyException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int code;

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
	
	public XkeyException(int pCode) {
		super();
		this.code = pCode;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	

}
