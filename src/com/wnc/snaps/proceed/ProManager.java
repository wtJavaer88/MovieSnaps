package com.wnc.snaps.proceed;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wnc.snaps.bean.NumValBean;
import com.wnc.snaps.tool.RunCmd;

public class ProManager
{

    PreHelper prework;

    public List<String> movies = new ArrayList<String>();
    public String tmpPath;
    public File srcFile = new File("");

    public String path = "";

    public ProManager(String string, int t)
    {
        path = string.trim().replace("\"", "");
        NumValBean.inturTime = t;
    }

    public void start()
    {
        getMovies();// 如果是目录,必须先获得所有视频
        System.out.println(movies + "--------");
        long l = new Date().getTime();
        for (String movie : movies)
        {
            tmpPath = "d:\\temp\\snaps\\";
            // 一下四个函数分别调用四个Helper

            prepare(movie);// 每个视频截取前的准备工作
            initNumVal();
            // 获得时分秒
            if(new MediaHelper(this).initTime())
            {
                createPics();// 截图
                dealPics();// 打水印,拼接

                // viewPic();// 查看
                System.out.println("耗时:" + ((new Date().getTime() - l) / 1000));
                recycle();// 删除临时文件
            }

        }
    }

    private void initNumVal()
    {
        NumValBean.hour = 0;
        NumValBean.minute = 0;
        NumValBean.second = 0;
        NumValBean.pCount = 0;
    }

    private void getMovies()
    {
        new MoviesHelper(this);
    }

    private void prepare(String str)
    {
        prework = new PreHelper(this, str); // 此对象以后还用得着
    }

    private void viewPic()
    {
        RunCmd.runCommand("explorer.exe"
                + say(tmpPath + prework.getMovieName() + "_p1.jpg"));
    }

    private void createPics()
    {
        new SnapHelper(this).snap();
    }

    private void dealPics()
    {
        new PicHelper(this).comcatPics();
    }

    public void recycle()
    {
        new File(tmpPath + "fullEmpty.jpg").delete();
        new File(tmpPath + "partEmpty.jpg").delete();
        new File(tmpPath + "time.txt").delete();
        for (int i = 0; i < NumValBean.pCount; i++)
        {
            new File(tmpPath + (i + NumValBean.startPicIndex) + ".jpg")
                    .delete();
        }
    }

    public String say(String str)
    {
        return " \"" + str + "\" ";
    }
}
