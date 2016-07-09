package vn.com.vietatech.phatbuugui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import vn.com.vietatech.phatbuugui.R;

public class DialogConfirm implements DialogInterface.OnClickListener, Runnable {
	public DialogConfirm(Context c, String q) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setTitle(c.getString(R.string.title_warning));
		builder.setMessage(q).setPositiveButton(c.getString(R.string.yes), this)
				.setNegativeButton(c.getString(R.string.no), this);
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