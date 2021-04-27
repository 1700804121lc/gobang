package Backgammon;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FiveChess extends JFrame implements MouseListener, Runnable {
   
    private static final long serialVersionUID = 1L; 
    int width = Toolkit.getDefaultToolkit().getScreenSize().width; 
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    int x, y;
    int[][] allChess = new int[15][15];
    boolean isBlack = true;
    boolean canPlay = true;
    String message = "黑方先行";
    // 保存棋谱
    int[] chessX = new int[255];
    int[] chessY = new int[255];
    int countX, countY;
    int maxTime = 0;
    int inputTime = 0;
    // 游戏时间设置的信息
    String blackMessage = "无限制";
    String whiteMessage = "无限制";
    int blackTime = 29;
    int whiteTime = 0;
    // 游戏倒计时
    Thread timer = new Thread(this); //是把当前这个实现了Runnable接口的类设置成一个线程

    public FiveChess() {
        this.setTitle("五子棋小游戏  ");
        this.setSize(500, 500);
        this.setLocation((width - 500) / 2, (height - 500) / 2);
        this.setResizable(false);  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);  
        this.repaint();
        this.addMouseListener(this);//addMouseListener方法，实现对 Mouse 的监听，所以就需要实现 MouseListener 类
        timer.start();
        timer.suspend();  //线程暂停
    }

    public void paint(Graphics g) {
        // 画棋盘                 
        BufferedImage bi = new BufferedImage(500, 500,BufferedImage.TYPE_INT_RGB); //图像缓存区
        Graphics g2 = bi.createGraphics();  //画笔类

        g2.setColor(new Color(91,91,91)); 
        g2.fillRect(0, 0, 500, 500); 
        g2.setColor(new Color(128,0,255));
        g2.fill3DRect(43, 60, 375, 375, true);

        for (int i = 0; i <= 15; i++) {
            g2.setColor(Color.WHITE);
            g2.drawLine(43, i * 25 + 60, 375 + 43, 60 + i * 25);
            g2.drawLine(i * 25 + 43, 60, 43 + i * 25, 375 + 60);
        }

        g2.setFont(new Font("黑体", Font.BOLD, 20));
        g2.drawString("现在由：" + message, 50, 50);
        g2.drawRect(30, 440, 180, 40);
        g2.drawRect(250, 440, 180, 40);
        g2.setFont(new Font("宋体", 0, 12));
        g2.drawString("黑方时间：" + blackMessage, 40, 465);
        g2.drawString("白方时间：" + whiteMessage, 260, 465);
        // 重新开始按钮
        g2.drawRect(428, 66, 54, 20);
        g2.drawString("重新开始", 432, 80);

        // 游戏设置按钮
        g2.drawRect(428, 106, 54, 20);
        g2.drawString("游戏设置", 432, 120);

        // 游戏说明按钮
        g2.drawRect(428, 146, 54, 20);
        g2.drawString("游戏简介", 432, 160);

        // 退出游戏按钮
        g2.drawRect(428, 186, 54, 20);
        g2.drawString("退出游戏", 432, 200);


        // 悔棋
        g2.drawRect(428, 246, 54, 20);
        g2.drawString("悔棋", 442, 260);

        // 认输
        g2.drawRect(428, 286, 54, 20);
        g2.drawString("认输", 442, 300);

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                // 黑子
                if (allChess[i][j] == 1) {
                    int tempX = i * 25 + 55;
                    int tempY = j * 25 + 72;
                    g2.setColor(Color.BLACK);
                    g2.fillOval(tempX - 8, tempY - 8, 16, 16);
                }
                // 白子
                if (allChess[i][j] == 2) {
                    int tempX = i * 25 + 55;
                    int tempY = j * 25 + 72;
                    g2.setColor(Color.WHITE);
                    g2.fillOval(tempX - 8, tempY - 8, 16, 16); 
                    g2.drawOval(tempX - 8, tempY - 8, 16, 16); 
                }
            }
        }
       
        g.drawImage(bi, 0, 0, this);  //在指定位置并且按指定形状和大小绘制指定的Image。

    }

    public void mouseClicked(MouseEvent arg0) { 
    }

    public void mouseEntered(MouseEvent arg0) {

    }

    public void mouseExited(MouseEvent arg0) { 

    }

    public void mousePressed(MouseEvent e) {  
        boolean checkWin = false;   
        
      
        if (canPlay) {
            x = e.getX();  //返回鼠标点的X
            y = e.getY();
            if (x >= 43 && x <= 418 && y >= 60 && y <= 435) {  
                    x = (x - 43) / 25;
                    y = (y - 60) / 25;
                   
                if (allChess[x][y] == 0) {

                    chessX[countX++] = x;
                    chessY[countY++] = y;

                    if (isBlack) {
                        allChess[x][y] = 1;
                        isBlack = false;
                        message = "白方下子";
                        blackTime = inputTime;
                    } else {
                        allChess[x][y] = 2;
                        isBlack = true;
                        message = "黑方下子";
                        whiteTime = inputTime;
                    }
                    this.repaint();
                    checkWin = isWin();
                    if (checkWin) {
                        if (allChess[x][y] == 1) {
                            JOptionPane.showMessageDialog(this, "游戏结束，黑方胜利");  
                            restart();        
                        }
                        else {
                            JOptionPane.showMessageDialog(this, "游戏结束，白方胜利");  
                            restart();
                        }
                    }
                }
            }
        }

        // 重新开始游戏按钮
        if (e.getX() >= 428 && e.getX() <= 482 && e.getY() >= 66&& e.getY() <= 86) {
            int result = JOptionPane.showConfirmDialog(this, "是否重新开始游戏？","请选择",JOptionPane.YES_NO_CANCEL_OPTION); 
            if (result == 0) {  
                restart();   
            }
        }
        // 游戏倒计时设置
        if (e.getX() >= 428 && e.getX() <= 482 && e.getY() >= 106&& e.getY() <= 126) {
            String input = JOptionPane.showInputDialog("请输入游戏的最大时间(单位:秒),如果输入0,表示没有时间限制:");
                                   
            maxTime = Integer.parseInt(input);   //Integer(整数)
            inputTime = Integer.parseInt(input);
            if (maxTime < 0) {
            	          
                JOptionPane.showMessageDialog(this, "输入的游戏时间有误，请重新设置！");
            } else if (maxTime == 0) {
                int result = JOptionPane.showConfirmDialog(this, "游戏时间设置成功，是否开始游戏？");
                if (result == 0) {
                    restart();
                }
            } else if (maxTime > 0) {
                int result = JOptionPane.showConfirmDialog(this,"游戏时间设置成功，是否重新开始游戏？");
                if (result == 0) {
                    for (int i = 0; i < 15; i++)
                        for (int j = 0; j < 15; j++)
                            allChess[i][j] = 0;
                    for (int i = 0; i < 15; i++) {
                        chessX[i] = -1;
                        chessY[i] = -1;
                    }
                    countX = 0;
                    countY = 0;
                    message = "黑方先行";
                    isBlack = true;
                    
                   
                    blackMessage = maxTime / 3600 + ":"+ (maxTime / 60 - maxTime / 3600 * 60) + ":"+ (maxTime - maxTime / 60 * 60);
                    whiteMessage = maxTime / 3600 + ":"+ (maxTime / 60 - maxTime / 3600 * 60) + ":"+ (maxTime - maxTime / 60 * 60);

                    blackTime = maxTime;
                    whiteTime = maxTime;
                  
                    timer.resume();  //线程恢复
                    this.canPlay = true;
                    this.repaint();
                }
            }
        }
        // 游戏简介
        if (e.getX() >= 428 && e.getX() <= 482 && e.getY() >= 146
                && e.getY() <= 166) {
            JOptionPane.showMessageDialog(this, "五子棋是全国智力运动会竞技项目之一，是一种两人对弈的纯策略型棋类游戏。通常双方分别使用黑白两色的棋子，轮流下在棋盘竖线与横线的交叉点上，先形成五子连线者获胜。");
        }
        // 退出游戏
        if (e.getX() >= 428 && e.getX() <= 482 && e.getY() >= 186
                && e.getY() <= 206) {
            int result = JOptionPane.showConfirmDialog(this, "是否退出游戏？");
            if (result == 0) {
                System.exit(0);
            }
        }
        // 悔棋
        if (e.getX() >= 428 && e.getX() <= 482 && e.getY() >= 246&& e.getY() <= 266) {
            int result = JOptionPane.showConfirmDialog(this,(isBlack == true ? "白方悔棋，黑方是否同意？" : "黑方悔棋，白方是否同意？"));
            if (result == 0) {
            	
                allChess[chessX[--countX]][chessY[--countY]] = 0;
                isBlack = (isBlack == true) ? false : true;
                this.repaint();
            }
        }
        // 认输
        if (e.getX() >= 428 && e.getX() <= 482 && e.getY() >= 286
                && e.getY() <= 306) {
            int result = JOptionPane.showConfirmDialog(this, "是否认输？");
            if (result == 0) {
                JOptionPane.showMessageDialog(this, "游戏结束，"+ (isBlack == true ? "黑方认输，白方获胜!" : "白方认输，黑方获胜!"));
                for (int i = 0; i < 15; i++)
                    for (int j = 0; j < 15; j++)
                        allChess[i][j] = 0;
               
                countX = 0;
                countY = 0;
                message = "黑方先行";
                isBlack = true;
                if(maxTime!=0) {
                blackMessage = maxTime / 3600 + ":"+ (maxTime / 60 - maxTime / 3600 * 60) + ":"+ (maxTime - maxTime / 60 * 60);
                whiteMessage = maxTime / 3600 + ":"+ (maxTime / 60 - maxTime / 3600 * 60) + ":" + (maxTime - maxTime / 60 * 60);
                }else {
                	blackMessage = "无限制";
                    whiteMessage = "无限制";
                }
                blackTime = maxTime;
                whiteTime = maxTime;
               
                timer.resume();  //resume，使线程恢复
                this.canPlay = true;
                this.repaint();
            }
        }
    }

    public void mouseReleased(MouseEvent arg0) {   //鼠标释放

    }

    private boolean isWin() {
        boolean flag = false;
        int count = 1;
        // 判断横向是否有5个棋子相连
        int color = allChess[x][y];
        // 判断横向
        count = this.checkCount(1, 0, color);
        if (count >= 5) {
            flag = true;
        } else {
            // 判断纵向
            count = this.checkCount(0, 1, color);
            if (count >= 5) {
                flag = true;
            } else {
                // 判断右上、左下
                count = this.checkCount(1, -1, color);
                if (count >= 5) {
                    flag = true;
                } else {
                    // 判断右下、左上
                    count = this.checkCount(1, 1, color);
                    if (count >= 5) {
                        flag = true;
                    }
                }
            }
        }

        return flag;
    }

    // 判断棋子连接的数量
    private int checkCount(int xChange, int yChange, int color) {     //一个方向
        int count = 1;
        int tempX = xChange;
        int tempY = yChange;
        while (x + xChange >= 0 && x + xChange <= 14 && y + yChange >= 0&& y + yChange <= 14&& color == allChess[x + xChange][y + yChange]) {
            count++;
            if (xChange != 0)
                xChange++;
            if (yChange != 0) {
                if (yChange > 0)
                    yChange++;
                else {
                    yChange--;
                }
            }
        }
        xChange = tempX;
        yChange = tempY;
                                        //       相反方向
        while (x - xChange >= 0 && x - xChange <= 14 && y - yChange >= 0&& y - yChange <= 14&& color == allChess[x - xChange][y - yChange]) {
            count++;
            if (xChange != 0)
                xChange++;
            if (yChange != 0) {
                if (yChange > 0)
                    yChange++;
                else {
                    yChange--;
                }
            }
        }
        return count;
    }

    public void run() {   //判断是否超时
        if (maxTime > 0) {
            while (true) {
                if (isBlack) {
                    blackTime--;
                    if (blackTime == 0) {
                        JOptionPane.showMessageDialog(this, "黑方超时,游戏结束!");
                        restart();
                    }
                } else {
                    whiteTime--;
                    if (whiteTime == 0) {
                        JOptionPane.showMessageDialog(this, "白方超时,游戏结束!");
                        restart();
                    }
                }                             //黑方下棋，然后白方时间回复到初始值的白方时间
                blackMessage = blackTime / 3600 + ":"+ (blackTime / 60 - blackTime / 3600 * 60) + ":"+ (blackTime - blackTime / 60 * 60);
                whiteMessage = whiteTime / 3600 + ":"+ (whiteTime / 60 - whiteTime / 3600 * 60) + ":"+ (whiteTime - whiteTime / 60 * 60);
                this.repaint();
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();   //抛出异常，程序不会崩溃
                }
            }
        }
    }

    public void restart() {    //游戏重新开始
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++)
                allChess[i][j] = 0;
        for (int i = 0; i < 15; i++) {
            chessX[i] = 0;
            chessY[i] = 0;
        }
        countX = 0;    //某一步的棋子所在X轴的第countX个格子数
        countY = 0;
        message = "黑方先行";
        blackMessage = "无限制";
        whiteMessage = "无限制";
        blackTime = maxTime;
        whiteTime = maxTime;
        isBlack = true;
        canPlay = true;
        this.repaint();
    }

    public static void main(String[] args) {
        new FiveChess();
    }

}