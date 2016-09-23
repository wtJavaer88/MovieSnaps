package com.wnc.snaps.proceed;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.wnc.snaps.MainFrame;
import com.wnc.snaps.bean.NumValBean;
import com.wnc.snaps.tool.RunCmd;

public class SnapHelper
{
    String snapExe = " \"" + MainFrame.FFMPEG_EXE + "\" ";

    ProManager p;

    int hour;
    int minute;
    int second;
    int width;
    int height;
    int inturTime;
    int totalTime = 0;

    public SnapHelper(ProManager pm)
    {
        this.p = pm;
        this.hour = NumValBean.hour;
        this.minute = NumValBean.minute;
        this.second = NumValBean.second;
        this.totalTime = 3600 * hour + 60 * minute + second;
        this.width = NumValBean.width;
        this.height = NumValBean.height;
        this.inturTime = NumValBean.inturTime;
        initParas();
    }

    final int THREAD_COUNTS = 4;
    boolean allOver = false;
    ThreadPoolExecutor executor;

    public void snap()
    {
        if(totalTime < 60)
        {
            return;
        }
        executor = (ThreadPoolExecutor) Executors
                .newFixedThreadPool(THREAD_COUNTS);
        int totalTime = 3600 * hour + 60 * minute + second;
        System.out.println("totalTime:" + totalTime);
        int curTime = NumValBean.beginTime;
        while (curTime < totalTime)
        {

            executor.execute(new SnapTask(curTime));
            curTime += inturTime;
            NumValBean.pCount++;
            // 强制最多只有80张
            if(NumValBean.pCount == 80)
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
        System.out.println("截图结束!###############图片数: " + NumValBean.pCount);
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
        this.tmpPath = p.tmpPath;
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
            RunCmd.runCommand(snapExe + " -ss " + time + " -loglevel panic -i "
                    + say(p.path) + "  -t 0.0001 -s " + width + "x" + height
                    + " " + say(p.tmpPath + picIndex + ".jpg"));

            RunCmd.runCommand(picTextExe
                    + say(tmpPath + (picIndex) + ".jpg")
                    + font
                    + " -pointsize "
                    + fontSize
                    + " -draw "
                    + say("text " + textX + "," + textY + " " + "\'"
                            + getTimeStrs(time) + "\'")
                    + say(tmpPath + picIndex + ".jpg"));
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
