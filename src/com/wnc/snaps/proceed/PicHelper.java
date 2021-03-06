package com.wnc.snaps.proceed;

import com.wnc.basic.BasicFileUtil;
import com.wnc.snaps.MainFrame;
import com.wnc.snaps.bean.NumValBean;
import com.wnc.snaps.ex.ErrCode;
import com.wnc.snaps.ex.SnapException;
import com.wnc.snaps.tool.RunCmd;

public class PicHelper
{
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
    int pCount;

    Task task;
    public PicHelper(Task task)
    {
    	this.task = task;
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
        this.tmpPath = task.tmpPath;

        this.pCount = task.pCount;
        textX = w - 5 * fontSize;
        textY = h - fontSize;
    }

    int threadCounts = 5;
    boolean allOver = false;

    public void comcatPics() throws SnapException
    {
        // 每页最多4x20张图片,然后慢慢拼
        rows = getSuitableRows();
        if(rows == 0){
        	throw new SnapException(ErrCode.PAGE_ROWS_ZERO);
        }
        int pages = (int) Math.ceil((float) pCount / (rows * rowPics));
        System.out.println(pages + "________________________________________"
                + rows);
        getEmptyjpg();
        if(pages == 0){
        	throw new SnapException(ErrCode.PIC_PAGES_ZERO);
        }
        for (int i = 0; i < pages; i++)
        {
            String pasteStr = "";
            for (int j = i * rows * rowPics; j < pCount
                    && j < (i + 1) * rows * rowPics; j++)
            {
                int newj = j - i * rows * rowPics;
                int posX = (newj % rowPics) * (w + spaceX) + offSetX;
                int posY = (newj / rowPics) * (h + spaceY) + offSetY;
                pasteStr += " " + say(tmpPath + (j + startPicIndex) + ".jpg")
                        + " -geometry +" + posX + "+" + posY + " -composite ";
            }
            // 判断最后一页是全是缺, 如果有rows行就是全,否则就是缺
            int left = pCount % (rows * rowPics);

            String destPic = tmpPath + task.getMovieName() + "_p"
			        + (i + 1) + ".jpg";
			if(i == pages - 1 && left > 0 && left <= (rows - 1) * rowPics)
            {
                RunCmd.runCommand(picTextExe
                        + say(tmpPath + "partEmpty.jpg")
                        + pasteStr
                        + say(destPic));
            }
            else
            {
                RunCmd.runCommand(picTextExe
                        + say(tmpPath + "fullEmpty.jpg")
                        + pasteStr
                        + say(destPic));
            }
			if(!BasicFileUtil.isExistFile(destPic)){
            	throw new SnapException(ErrCode.PIC_PAGE_CREATE_ERR);
            }
        }
    }

    public int getSuitableRows()
    {
        if(pCount % (rows * rowPics) == 0)
        {
            return 20 * (pCount / (rows * rowPics));
        }
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

    public void getEmptyjpg()
    {
        int tw = (w + spaceX) * rowPics + offSetX + spaceX;
        int th = (h + spaceY) * rows + offSetY;
        getEmptyjpgByType(tw, th, "fullEmpty.jpg");
        System.out.println(rows + " dddddddd " + rows * rowPics);
        // 计算可能残缺的最后一页的尺寸
        if(pCount % (rows * rowPics) <= rows * rowPics - rowPics)
        {
            int leftRows = (int) Math.ceil((float) (pCount % (rows * rowPics))
                    / rowPics);
            th = (h + spaceY) * leftRows + offSetY;
            getEmptyjpgByType(tw, th, "partEmpty.jpg");
        }

    }

    public void getEmptyjpgByType(int tw, int th, String jpgType)
    {
        System.out.println("生成空白图片：" + picTextExe + " -size " + tw + "x" + th
                + " -strip -colors 8 -depth 8 xc:white "
                + say(tmpPath + jpgType));
        RunCmd.runCommand(picTextExe + " -size " + tw + "x" + th
                + " -strip -colors 8 -depth 8 xc:white "
                + say(tmpPath + jpgType));
        // 写影片基本信息
        RunCmd.runCommand(picTextExe + say(tmpPath + jpgType) + font
                + " -pointsize " + fontSize + " -draw " + "\"" + "text "
                + offSetX + "," + (fontSize + 3) + " " + "'" + "影片目录： "
                + task.dir + " \'" + "\" " + say(tmpPath + jpgType));
        RunCmd.runCommand(picTextExe + say(tmpPath + jpgType) + font
                + " -pointsize " + fontSize + " -draw " + "\"" + "text "
                + offSetX + "," + (fontSize + 3) * 2 + " " + "\'" + "影片名称： "
                + task.movieName + "\'" + "\" " + say(tmpPath + jpgType));
        RunCmd.runCommand(picTextExe + say(tmpPath + jpgType) + font
                + " -pointsize " + fontSize + " -draw " + "\"" + "text "
                + offSetX + "," + (fontSize + 3) * 3 + " " + "\'" + "影片时长： "
                + task.movieTimeStr + "\'" + "\" "
                + say(tmpPath + jpgType));
    }

    public String say(String str)
    {
        return " \"" + str + "\" ";
    }
}