package xk3y.dongle.android;

import xk3y.dongle.android.utils.PromptDialog;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class xk3yExitDialog extends PromptDialog {
	Context mcontext;
	public xk3yExitDialog(Context context, int title, int message) {
		super(context, title, message);
		mcontext = context;
	}

	@Override
	public boolean onOkClicked() {
		((Activity)mcontext).finish();
		return false;
	}

}
