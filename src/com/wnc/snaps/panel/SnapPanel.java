package com.wnc.snaps.panel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.wnc.snaps.MainFrame;
import com.wnc.snaps.bean.NumValBean;
import com.wnc.snaps.i.INewFrame;
import com.wnc.snaps.proceed.ProManager;
import com.wnc.snaps.tool.PopupDialog;

public class SnapPanel extends JPanel implements INewFrame
{
    MainFrame m;
    JButton chooseBt;// 选择
    JTextField jtfTime;
    JButton startBt;// 开始
    public JTextField jtfMovie;

    public SnapPanel(MainFrame mf)
    {
        m = mf;
        init();
        setBounds();
        addComp();
        listen();
    }

    @Override
    public void init()
    {
        this.setLayout(null);
        chooseBt = new JButton("选择电影");// 选择
        jtfTime = new JTextField(30);
        startBt = new JButton("Start!");// 开始
        jtfMovie = new JTextField(100);
        jtfTime.setText("" + NumValBean.inturTime);
        jtfMovie.setText("D:\\TDDOWNLOAD\\【江城足球网】6月29日 欧洲杯半决赛 德国vs意大利\\北川\\");
        jtfTime.setToolTipText("设置截图间隔");
        jtfTime.setHorizontalAlignment(JTextField.CENTER);
        jtfMovie.setToolTipText("请选择一部电影或整个目录，或者手动输入！");
    }

    @Override
    public void setBounds()
    {
        chooseBt.setBounds(50, 50, 100, 30);
        jtfTime.setBounds(170, 50, 60, 30);
        startBt.setBounds(50, 150, 100, 30);
        jtfMovie.setBounds(50, 100, 200, 30);
    }

    @Override
    public void addComp()
    {
        this.add(chooseBt);
        this.add(startBt);
        this.add(jtfMovie);
        this.add(jtfTime);
    }

    @Override
    public void listen()
    {
        chooseBt.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                JFileChooser fChooser = new JFileChooser("E:\\一个\\jpg");
                fChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Video Files", "mp4", "avi", "rmvb", "rm", "wmv", "mkv");
                fChooser.setFileFilter(filter);
                fChooser.setMultiSelectionEnabled(false);

                fChooser.showOpenDialog(null);
                File tmpFile = fChooser.getSelectedFile();

                jtfMovie.setText(tmpFile.getAbsolutePath());
            }

        });
        startBt.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                int inturTime = 0;
                if(jtfTime.getText().matches("\\d+"))
                {
                    inturTime = Integer.parseInt(jtfTime.getText());
                }
                else
                {
                    new PopupDialog("时间错误", "请输入一个整数！");
                    throw new RuntimeException("请输入一个整数来表示时间间隔");
                }
                new ProManager(jtfMovie.getText(), inturTime).start();
            }
        });
    }

    @Override
    public void refresh()
    {
    }

}
