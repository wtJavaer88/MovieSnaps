package com.wnc.snaps.proceed;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PreHelper
{
    ProManager p;
    List<String> movies = new ArrayList<String>();

    String dir = "";
    String movie = "";
    String movieTimeStr = "";

    public PreHelper(ProManager pm, String str)
    {
        p = pm;
        p.path = str;
        seperatePath(p.path);
        initTmpPath();

    }

    private void initTmpPath()
    {

        if(p.srcFile.isDirectory())
        {
            p.tmpPath = p.tmpPath + getMovieRoot() + "\\";
        }
        else
        {
            System.out.println(p.prework);
            p.tmpPath = p.tmpPath + getMovieName() + "\\";
        }

        File file = new File(p.tmpPath);
        if(!file.exists())
        {
            file.mkdirs();
        }
    }

    private void seperatePath(String path)
    {
        File file = new File(path);
        if(file.exists())
        {
            dir = file.getParent() + "\\";
            System.out.println("目录： " + dir);
            movie = file.getName();
            System.out.println(movie + " movie is __________");
        }
        else
        {
            throw new RuntimeException(path + " 文件不存在");
        }
    }

    String getMovieRoot()
    {
        String root = p.path;
        // 将path中的srcFile目录去掉,后缀也去掉
        root = p.path.replace(p.srcFile.getAbsolutePath() + "\\", "");
        return p.srcFile.getName() + "\\"
                + root.substring(0, root.lastIndexOf("."));
    }

    public String getMovieName()
    {
        int i = movie.lastIndexOf(".");
        return movie.substring(0, i);
    }
}
