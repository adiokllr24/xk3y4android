package xk3y.dongle.android.utils;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.FullGameInfo;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class PaginateButton extends Button implements OnClickListener {

	public PaginateButton(Context context, String text) {
		super(context);
		this.setBackgroundResource(R.drawable.rounded_button);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(15, 15, 15, 15);
		this.setLayoutParams(lp);
		this.setPadding(25, 0, 25, 0);
		this.setText(text);
		this.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		ConfigUtils.getConfig().vivrate();
		for (FullGameInfo game : ConfigUtils.getConfig().getListeGames()) {
			/*
			LoadingUtils.clearBitmap(game.getBanner());
			LoadingUtils.clearBitmap(game.getCover());
			LoadingUtils.clearBitmap(game.getOriginalCover());
			*/
			game.setBanner(null);
			game.setCover(null);
			game.setOriginalCover(null);
		}
		System.gc();
		
		int numPage = Integer.valueOf(this.getText().toString());
		ConfigUtils.getConfig().setCurrentPage(numPage);
		/*
		int start = ConfigUtils.getConfig().getFirstGameToLoad();
		int end = ConfigUtils.getConfig().getLastGameToLoad();
		
		String loadingGame = "Loading game " + start + " to " + end;
		Toast.makeText(v.getContext(), loadingGame, Toast.LENGTH_SHORT).show();
		*/
		ConfigUtils.getConfig().getCurrentActivity().initActivity();
	}

}
