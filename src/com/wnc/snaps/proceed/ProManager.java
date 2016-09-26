package com.wnc.snaps.proceed;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.wnc.snaps.bean.NumValBean;
import com.wnc.snaps.tool.PopupDialog;
import com.wnc.snaps.tool.RunCmd;

public class ProManager {

	public List<String> movies = new ArrayList<String>();
	public File srcFile = new File("");

	public String path = "";

	public ProManager(String string, int t) {
		path = string.trim().replace("\"", "");
		NumValBean.inturTime = t;
	}

	public void start() {
		getMovies();// 如果是目录,必须先获得所有视频
		System.out.println(movies + "--------");
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
	                .newFixedThreadPool(4);
		for (String mv : movies) {
			// 一下四个函数分别调用四个Helper

			executor.execute(new Task(mv, path));
//			new Task(mv, path).run();
		}
		executor.shutdown();
	}

	private void getMovies() {
		getAllMovies();
	}

	private void getAllMovies() {
		srcFile = new File(path);
		if (srcFile.exists()) {
			if (srcFile.isDirectory()) {
				getAllMoviesByPath(path);
				System.out.println(movies.size() + ":movies_______________");
			} else {
				movies.add(path);
			}
		} else {
			new PopupDialog("文件错误", "不存在该视频文件！");
		}
	}

	private void getAllMoviesByPath(String strPath) {
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				getAllMoviesByPath(file.getAbsolutePath());
			} else if (isMovie(file)) {
				movies.add(file.getAbsolutePath());
			}
		}
	}

	String[] movieTypes = new String[] { "avi", "mkv", "mp4", "wmv", "rm", "rmvb", "mpg" };

	private boolean isMovie(File file) {
		String strFile = file.getName();
		String filetail = strFile.substring(strFile.lastIndexOf(".") + 1);
		for (int i = 0; i < movieTypes.length; i++) {
			if (movieTypes[i].equalsIgnoreCase(filetail)) {
				return true;
			}
		}
		return false;
	}

	public String say(String str) {
		return " \"" + str + "\" ";
	}
}
