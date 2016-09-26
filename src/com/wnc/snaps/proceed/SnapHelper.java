package com.wnc.snaps.proceed;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.wnc.basic.BasicFileUtil;
import com.wnc.snaps.MainFrame;
import com.wnc.snaps.bean.NumValBean;
import com.wnc.snaps.ex.ErrCode;
import com.wnc.snaps.ex.SnapException;
import com.wnc.snaps.tool.RunCmd;

public class SnapHelper
{
    String snapExe = " \"" + MainFrame.FFMPEG_EXE + "\" ";

    Task task;

    int width;
    int height;
    int inturTime;
    //int totalTime = 0;

    public SnapHelper(Task  task)
    {
        this.task = task;
        this.width = NumValBean.width;
        this.height = NumValBean.height;
        this.inturTime = NumValBean.inturTime;
        initParas();
        executor = (ThreadPoolExecutor) Executors
                .newFixedThreadPool(THREAD_COUNTS);
    }

    final int THREAD_COUNTS = 4;
    boolean allOver = false;
    ThreadPoolExecutor executor;

    public void snap() throws SnapException
    {
        int totalTime = 3600 * task.hour + 60 * task.minute + task.second;
      if(totalTime <= NumValBean.beginTime)
      {
    	  throw new SnapException(ErrCode.Time_Too_Short);
      }
        System.out.println("totalTime:" + totalTime);
        int curTime = NumValBean.beginTime;
        while (curTime < totalTime)
        {

            executor.execute(new SnapTask(curTime));
            curTime += inturTime;
            task.pCount++;
            // 强制最多只有80张
            if(task.pCount == 80)
            {
                break;
            }
        }
        executor.shutdown();
        while (executor.getActiveCount() > 0)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("截图结束!###############图片数: " + task.pCount);
    }

    private void initParas()
    {
        font = " -font C:\\Windows\\Fonts\\msyh.ttf -fill blue";
        fontSize = NumValBean.FONT_SIZE;
        rowPics = NumValBean.ROW_PICS;
        rows = NumValBean.MAX_ROWS;
        spaceX = NumValBean.spaceX;
        spaceY = NumValBean.spaceY;
        offSetX = NumValBean.offSetX;
        offSetY = NumValBean.offSetY;
        startPicIndex = NumValBean.startPicIndex;

        this.w = NumValBean.width;
        this.h = NumValBean.height;
        textX = w - 5 * fontSize;
        textY = h - fontSize;
        this.tmpPath = task.tmpPath;
    }

    String picTextExe = "\"" + MainFrame.PIC_CONVERT_EXE + "\" ";
    String font;
    int fontSize;
    int rowPics;
    int rows;
    int spaceX;
    int spaceY;
    int offSetX;
    int offSetY;
    int startPicIndex;

    int textX;
    int textY;

    int w;
    int h;
    String tmpPath;

    class SnapTask implements Runnable
    {
        public int time;

        public SnapTask(int time)
        {
            this.time = time;
        }

        @Override
        public void run()
        {

            int picIndex = (time - NumValBean.beginTime) / NumValBean.inturTime
                    + NumValBean.startPicIndex;
            String pic = tmpPath + picIndex + ".jpg";
			RunCmd.runCommand(snapExe + " -ss " + time + " -loglevel panic -i "
                    + say(task.mvpath) + "  -t 0.0001 -s " + width + "x" + height
                    + " " + say(pic));
            if(!BasicFileUtil.isExistFile(pic)){
            	Logger.getLogger("Task").error(ErrCode.Snap_Err+" "+pic);;
            }
            RunCmd.runCommand(picTextExe
                    + say(tmpPath + (picIndex) + ".jpg")
                    + font
                    + " -pointsize "
                    + fontSize
                    + " -draw "
                    + say("text " + textX + "," + textY + " " + "\'"
                            + getTimeStrs(time) + "\'")
                    + say(pic));
        }
    }

    public String getTimeStrs(int curTime)
    {
        String timeStr = "";

        if(curTime / 3600 > 9)
        {
            timeStr += curTime / 3600 + ":";
        }
        else
        {
            timeStr += "0" + curTime / 3600 + ":";
        }

        curTime = curTime % 3600;

        if(curTime / 60 > 9)
        {
            timeStr += curTime / 60 + ":";
        }
        else
        {
            timeStr += "0" + curTime / 60 + ":";
        }

        curTime = curTime % 60;

        if(curTime > 9)
        {
            timeStr += curTime;
        }
        else
        {
            timeStr += "0" + curTime;
        }
        return timeStr;
    }

    public String say(String str)
    {
        return " \"" + str + "\" ";
    }
}
