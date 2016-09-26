package com.wnc.snaps.proceed;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import com.wnc.basic.BasicStringUtil;
import com.wnc.snaps.MainFrame;
import com.wnc.snaps.bean.NumValBean;
import com.wnc.snaps.ex.SnapException;
import com.wnc.snaps.tool.RunCmd;
import com.wnc.string.PatternUtil;

public class Task implements Runnable {

	private static final Logger LOGGER = Logger.getLogger("Task");
	String mvpath;
	File srcFile;

	String tmpPath = "d:\\temp\\snaps\\";
	String dir;
	String movieName;

	int hour;
	int minute;
	int second;
	int pCount;
	String movieTimeStr;

	public Task(String mvpath,String srcPath) {
		this.mvpath = mvpath;
		srcFile = new File(srcPath);
	}

	@Override
	public void run() {
		long l = new Date().getTime();
		try {
			prepare(mvpath);// 每个视频截取前的准备工作
			if(getTimeFromTxt(mvpath)){
				createPics();// 截图
				dealPics();// 打水印,拼接
				LOGGER.info("耗时:" + ((new Date().getTime() - l) / 1000)+" "+mvpath);
			}
			else{
				LOGGER.error("invalid video , not found time:"+mvpath);
			}
		} catch (SnapException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}catch(Exception ex){
			
		}
	}
	private void dealPics() throws SnapException {
		new PicHelper(this).comcatPics();
	}
	private void createPics() throws SnapException {
		new SnapHelper(this).snap();
	}
	private void viewPic() {
		RunCmd.runCommand("explorer.exe" + say(tmpPath + getMovieName() + "_p1.jpg"));
	}

	public void recycle() {
		new File(tmpPath + "fullEmpty.jpg").delete();
		new File(tmpPath + "partEmpty.jpg").delete();
		new File(tmpPath + "time.txt").delete();
		for (int i = 0; i < pCount; i++) {
			new File(tmpPath + (i + NumValBean.startPicIndex) + ".jpg").delete();
		}
	}

	public boolean getTimeFromTxt(String path) {
		String command = say(MainFrame.FFMPEG_EXE) + " -i " + say(mvpath);
		String runCommand = RunCmd.runCommand(command);
		String time = PatternUtil.getFirstPatternGroup(runCommand, " Duration: (.*?),");
		if(BasicStringUtil.isNotNullString(time)){
		movieTimeStr = time;
		System.out.println(time);
		hour = Integer.parseInt(time.substring(0, 2));
		minute = Integer.parseInt(time.substring(3, 5));
		second = Integer.parseInt(time.substring(6, 8));
		}
		else{
			return false;
		}
		return true;
	}

	private void prepare(String str) {
		dir = new File(mvpath).getParent() + "\\";
		movieName = new File(mvpath).getName();

		if (srcFile.isDirectory()) {
			tmpPath = tmpPath + getMovieRoot() + "\\";
		} else {
			tmpPath = tmpPath + getMovieName() + "\\";
		}

		File file = new File(tmpPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	String getMovieRoot() {
		// 将path中的srcFile目录去掉,后缀也去掉
		String root = mvpath.replace(srcFile.getAbsolutePath() + "\\", "");
		return srcFile.getName() + "\\" + root.substring(0, root.lastIndexOf("."));
	}

	public String getMovieName() {
		int i = movieName.lastIndexOf(".");
		if(i==-1){
			return movieName;
		}
		return movieName.substring(0, i);
	}

	public static String say(String str) {
		return " \"" + str + "\" ";
	}
}
