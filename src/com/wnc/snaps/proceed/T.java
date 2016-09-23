package com.wnc.snaps.proceed;

import org.junit.Test;

public class T
{
    int pCount = 0;
    int rows = 0;
    int rowPics = 4;

    @Test
    public void m()
    {
        pCount = 484;
        rows = 20;
        rowPics = 4;
        // System.out.println("结果:" + getSuitableRows());
        for (pCount = 4840; pCount > 100; pCount--)
        {
            System.out.println("结果:" + getSuitableRows());
        }

    }

    public int getSuitableRows()
    {
        System.out.println(pCount);
        int left = pCount % (rows * rowPics);
        float rate = 0.75f;
        while (left < rate * rows * rowPics || rows <= 10)
        {
            System.out.println(left + "  " + rows + " " + rate);
            rows--;
            if(rows == 0)
            {
                rate -= 0.01;
                rows = 20;
            }
            else
            {
                left = pCount % (rows * rowPics);
            }
        }
        return rows;
    }
}
