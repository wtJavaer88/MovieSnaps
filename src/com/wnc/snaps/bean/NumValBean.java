package com.wnc.snaps.bean;

public class NumValBean
{
    public static int inturTime = 15;// 截图间隔时间
    public static int beginTime = 5;// 第几秒开始截图
    
    public final static int width = 320;// 图片宽
    public final static int height = 240;// 图片高
    public final static int startPicIndex = 100;// 从多少开始编号

    public final static int spaceX = 5;// 图片横向间隔
    public final static int spaceY = 5;// 图片纵向间隔

    public final static int offSetX = 10;// 第一张图片的原点X位置
    public final static int offSetY = 50;// 第一张图片的原点Y位置,至少比(fontSize+3)的三倍大

    public final static int FONT_SIZE = 12;// 字体大小
    public final static int ROW_PICS = 4;// 每页最多的列数
    public final static int MAX_ROWS = 20;// 每页最多的行数

}
