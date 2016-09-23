package com.wnc.snaps.proceed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.wnc.snaps.MainFrame;
import com.wnc.snaps.bean.NumValBean;
import com.wnc.snaps.tool.RunCmd;

public class MediaHelper
{
    String mediaInfoExe = MainFrame.MEDIA_INFO_EXE;
    ProManager p;

    String time = "";
    int hour;
    int minute;
    int second;

    public MediaHelper(ProManager pm)
    {
        p = pm;
    }

    public boolean initTime()
    {
        if(getTimeFromTxt())
        {
            p.prework.movieTimeStr = time;
            NumValBean.hour = hour;
            NumValBean.minute = minute;
            NumValBean.second = second;
            System.out.println(time + "ddddddddddddddddddddd");
        }
        else
        {
            return false;
        }
        return true;
    }

    public boolean getTimeFromTxt()
    {
        String logTxt = p.tmpPath + "time.txt";
        System.out.println("cmd /c " + say(mediaInfoExe) + say(p.path) + " >"
                + say(logTxt));
        RunCmd.runCommand("cmd /c " + mediaInfoExe + say(p.path) + " >"
                + say(logTxt));
        File file = new File(logTxt);
        FileInputStream in;
        String timeStr = null;
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));

            while ((timeStr = br.readLine()) != null)
            {
                if(timeStr.contains("Duration"))
                {
                    System.out.println("找到Duration!__" + timeStr.length());
                    break;
                }
            }
            br.close();
        }
        catch (Exception e)
        {
            return false;
        }
        if(timeStr != null)
        {
            int i = timeStr.lastIndexOf(":");
            timeStr = timeStr.substring(i + 1, timeStr.length()).trim();
            int a = timeStr.lastIndexOf("h");
            int b = timeStr.lastIndexOf("mn");
            int c = timeStr.lastIndexOf("s");
            if(a != -1)
            {
                hour = Integer.parseInt(timeStr.substring(0, a).trim());
            }
            if(b != -1)
            {
                minute = Integer.parseInt(timeStr.substring(a + 1, b).trim());
            }
            if(c != -1)
            {
                second = Integer.parseInt(timeStr.substring(b + 2, c).trim());
            }

            if(hour > 0)
            {
                time = hour + "时 " + minute + "分 ";
            }
            else
            {
                time = minute + "分 " + second + "秒";
            }
        }
        else
        {
            // throw new RuntimeException("找不到该影片的时间");
            RunCmd.runCommand("cmd /c " + "echo  " + p.path
                    + ">>D:\\snaperr.txt");
            return false;
        }
        return true;
    }

    public String say(String str)
    {
        return " \"" + str + "\" ";
    }
}
