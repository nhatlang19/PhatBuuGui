package vn.com.vietatech.lib;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.content.Context;


import vn.com.vietatech.dto.User;
import vn.com.vietatech.phatbuugui.R;

public class UserUtil {
	private static String FILENAME = "NearUser";

	@SuppressLint("NewApi")
	public static void write(User user, Context context)
			throws IOException {
		File dir = new File(context.getFilesDir().getPath() + "/"
				+ context.getResources().getString(R.string.app_folder));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, FILENAME);
		Properties props = new Properties();
		props.setProperty("userId", String.valueOf(user.getId()));

		FileWriter writer = new FileWriter(file);
		props.store(writer, "config");
		writer.close();
	}

	@SuppressLint("NewApi")
	public static User read(Context context) throws IOException {

		File file = new File(context.getFilesDir().getPath() + "/"
				+ context.getResources().getString(R.string.app_folder) + "/"
				+ FILENAME);
		if (file.exists() && file.isFile()) {
			FileReader reader = new FileReader(file);
			Properties props = new Properties();
			props.load(reader);

			User user = new User();
			user.setId(Integer.parseInt(props.getProperty("userId")));
			reader.close();
			return user;
		}
		return null;
	}
}
