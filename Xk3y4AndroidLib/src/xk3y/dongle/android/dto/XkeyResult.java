package xk3y.dongle.android.dto;

public class XkeyResult {
	
	private boolean showError;
	
	private int messageCode;
	
	private StringBuffer debugMessage;
	
	

	public XkeyResult() {
		debugMessage = new StringBuffer();
	}

	public boolean isShowError() {
		return showError;
	}

	public void setShowError(boolean showError) {
		this.showError = showError;
	}

	public int getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(int messageCode) {
		this.messageCode = messageCode;
	}

	public StringBuffer getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(StringBuffer debugMessage) {
		this.debugMessage = debugMessage;
	}

	
	
	

	

}
