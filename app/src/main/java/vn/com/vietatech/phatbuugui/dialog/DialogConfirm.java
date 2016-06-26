package vn.com.vietatech.phatbuugui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogConfirm implements DialogInterface.OnClickListener, Runnable {
	public DialogConfirm(Context c, String q) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setMessage(q).setPositiveButton("Yes", this)
				.setNegativeButton("No", this);
		builder.create().show();
	}

	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
		if (which == DialogInterface.BUTTON_POSITIVE) {
			run();
		}
		
		if (which == DialogInterface.BUTTON_NEGATIVE) {
			no();
		}
	}
	
	public void no() {
		
	}

	@Override
	public void run() {
	}
}