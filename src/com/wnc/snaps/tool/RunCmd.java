package com.wnc.snaps.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

public class RunCmd
{
    /**
     * 运行各种各样的exe或cmd命令
     * 
     * @param command
     *            执行命令
     */

    static String ret = "";

    public static String runCommand(String command)
    {
        ret = "";
        System.out.println("即将执行命令： " + command);
        final CountDownLatch threadSignal = new CountDownLatch(2);
        try
        {

            final Process pro = Runtime.getRuntime().exec(command);
            pro.getOutputStream().close();
            Runnable errThread = new Runnable()
            {

                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    try
                    {
                        InputStream is = pro.getErrorStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String line = "";
                        StringBuilder sb = new StringBuilder();
                        while ((line = br.readLine()) != null)
                        {
                            sb.append(line);
                        }
                        // System.out
                        // .println("error:" + new String(sb.toString()));
                        ret = sb.toString();
                        is.close();
                        isr.close();
                        br.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    threadSignal.countDown();
                }

            };
            new Thread(errThread).start();

            Runnable inputThread = new Runnable()
            {

                @Override
                public void run()
                {
                    try
                    {
                        InputStream is = pro.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String line = "";
                        StringBuilder sb = new StringBuilder();
                        while ((line = br.readLine()) != null)
                        {
                            sb.append(line);
                        }
                        // System.out.println("input:"+sb.toString());
                        is.close();
                        isr.close();
                        br.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    threadSignal.countDown();
                }

            };
            new Thread(inputThread).start();
            try
            {
                threadSignal.await();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return ret;
    }
}