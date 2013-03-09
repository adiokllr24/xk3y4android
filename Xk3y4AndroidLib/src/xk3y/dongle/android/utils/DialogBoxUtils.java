package xk3y.dongle.android.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.widget.TextView;

public class DialogBoxUtils {

	/**
	 * Construit une boite de dialog et affiche un message
	 * 
	 * @param activity
	 * @param listeMessages
	 */
	public DialogBoxUtils(Context activity, String msg) {
		createAndShowDialogBox(activity, " ", msg);
	}

	/**
	 * Construit une boite de dialog et affiche un message
	 * 
	 * @param activity
	 * @param listeMessages
	 */
	public DialogBoxUtils(Context activity, int title, String msg) {
		createAndShowDialogBox(activity, title, msg);
	}
	
	/**
	 * Construit une boite de dialog et affiche un message
	 * 
	 * @param activity
	 * @param listeMessages
	 */
	public DialogBoxUtils(Context activity, int msg) {
		createAndShowDialogBox(activity, " ", msg);
	}
	
	/**
	 * Construit une boite de dialog avec confirmation et affiche un message
	 * 
	 * @param activity
	 * @param listeMessages
	 *//*
	public DialogBoxUtils(Context activity, String msg, OnClickListener listener) {
		createAndShowConfirmDialogBox(activity, "", msg, listener);
	}*/
	/**
	 * Construit une boite de dialog et affiche un message
	 * 
	 * @param activity
	 * @param listeMessages
	 */
	private void createAndShowDialogBox(Context activity, String title,
			String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		builder.setMessage(msg).setTitle(title)
				.setIcon(android.R.drawable.stat_sys_warning)
				.setCancelable(false)
				.setPositiveButton(android.R.string.ok, null);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Construit une boite de dialog et affiche un message
	 * 
	 * @param activity
	 * @param listeMessages
	 */
	private void createAndShowDialogBox(Context activity, String title,
			int msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		builder.setMessage(msg).setTitle(title)
				.setIcon(android.R.drawable.stat_sys_warning)
				.setCancelable(false)
				.setPositiveButton(android.R.string.ok, null);
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/**
	 * Construit une boite de dialog et affiche un message
	 * 
	 * @param activity
	 * @param listeMessages
	 */
	private void createAndShowDialogBox(Context activity, int title,
			String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		
	    
		builder.setMessage(msg).setTitle(title)
				.setCancelable(false)
				.setPositiveButton(android.R.string.ok, null);
		AlertDialog alert = builder.create();
		alert.show();
		
		TextView textView = (TextView) alert.findViewById(android.R.id.message);
	    textView.setTextSize(14);
	}
	
	/**
	 * Construit une boite de dialog et affiche un message
	 * 
	 * @param activity
	 * @param listeMessages
	 */
	private void createAndShowConfirmDialogBox(Context activity, String title,
			String msg, OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		builder.setMessage(msg).setTitle(title)
				.setIcon(android.R.drawable.stat_sys_warning)
				.setCancelable(true)
				.setPositiveButton(android.R.string.ok, listener)
				.setNegativeButton(android.R.string.no, listener);
		AlertDialog alert = builder.create();
		alert.show();
	}

}
