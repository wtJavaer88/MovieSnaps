package com.wnc.snaps.proceed;

import java.io.File;

import com.wnc.snaps.tool.PopupDialog;

public class MoviesHelper
{
    ProManager p;
    String[] movieTypes = new String[]
    { "avi", "mkv", "mp4", "wmv", "rm", "rmvb", "mpg" };

    public MoviesHelper(ProManager pm)
    {

        p = pm;
        getAllMovies();
    }

    private void getAllMovies()
    {
        p.srcFile = new File(p.path);
        if(p.srcFile.exists())
        {
            if(p.srcFile.isDirectory())
            {
                getAllMoviesByPath(p.path);
                System.out.println(p.movies.size() + ":movies_______________");
            }
            else
            {
                p.movies.add(p.path);
            }
        }
        else
        {
            new PopupDialog("文件错误", "不存在该视频文件！");
        }
    }

    private void getAllMoviesByPath(String strPath)
    {
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        for (File file : files)
        {
            if(file.isDirectory())
            {
                getAllMoviesByPath(file.getAbsolutePath());
            }
            else if(isMovie(file))
            {
                p.movies.add(file.getAbsolutePath());
            }
        }
    }

    private boolean isMovie(File file)
    {
        String strFile = file.getName();
        String filetail = strFile.substring(strFile.lastIndexOf(".") + 1);
        for (int i = 0; i < movieTypes.length; i++)
        {
            if(movieTypes[i].equalsIgnoreCase(filetail))
            {
                return true;
            }
        }
        return false;
    }
}
