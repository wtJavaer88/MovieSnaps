package com.wnc.snaps;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.wnc.snaps.panel.SnapPanel;

public class MainFrame extends JFrame
{
    public SnapPanel sp;
    public static String PIC_CONVERT_EXE;

    public static String FFMPEG_EXE;
    public static String MEDIA_INFO_EXE;
    static
    {
        Properties p = new Properties();
        try
        {
            // p.load(new FileInputStream("outexe.properties"));
            InputStream ips = MainFrame.class
                    .getResourceAsStream("/outexe.properties");
            BufferedReader ipss = new BufferedReader(new InputStreamReader(ips));
            p.load(ipss);
            PIC_CONVERT_EXE = p.getProperty("picConvertExe");
            FFMPEG_EXE = p.getProperty("ffmpegExe");
            MEDIA_INFO_EXE = p.getProperty("mediaInfoExe");

            // InputStream logIs = MainFrame.class
            // .getResourceAsStream("/log4j.properties");
            // String snapLogPath = "D:\\snap-log4j.properties";
            // FileOutputStream fos = new FileOutputStream(snapLogPath);
            // byte[] b = new byte[1024];
            // while ((logIs.read(b)) != -1)
            // {
            // fos.write(b);
            // }
            // logIs.close();
            // fos.close();
            // System.setProperty("log4j.configuration", "file:" + snapLogPath);

            // 读取jar包内配置文件
            InputStream logIs = MainFrame.class
                    .getResourceAsStream("/log4j.properties");
            Properties p2 = new Properties();
            try
            {
                p.load(logIs);
                PropertyConfigurator.configure(p2);
                System.out.println("读取jar包内配置文件");
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Logger.getLogger("Snap").error("snap err:");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("读取配置文件失败！", e);
        }
    }

    public MainFrame()
    {
        init();
    }

    public static void main(String[] args)
    {
        new MainFrame();
    }

    public void init()
    {
        sp = new SnapPanel(this);
        sp.setBounds(0, 0, 500, 200);
        this.add(sp);
        this.setFocusable(true);
        this.requestFocus();
        this.setLayout(null);
        this.setBounds(0, 0, 800, 600);

        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("电影预览生成器By~龙年生");
        this.setDropTarget(new DropTarget(this, DnDConstants.ACTION_REFERENCE,
                new MyDropImgListener(this), true));
    }

}