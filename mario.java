import java.awt.*;
  import java.awt.event.*;
  import javax.swing.*;
  import java.util.*;
  import java.awt.image.*;
  import javax.imageio.*;
  import java.io.*;

  public class mario extends JPanel {
   boolean go=false;
    float sp=3;

    ArrayList<Figure> figs = new ArrayList<Figure>();
    ArrayList<Animation> anim = new ArrayList<Animation>();
    JButton bu1 = new JButton("Start");

    public mario() {
    	setOpaque(true);
      setLayout(null);
      add(bu1);
       bu1.setBounds(600, 600, 50, 50);  
      Man p1 = new Man("mar1.png", 50, 500,15);
      Brick b1 = new Brick("br.png", 450, 200);
      Brick b2 = new Brick("br.png", 300, 200);
      Brick b3 = new Brick("br.png", 150, 200);
      figs.add(p1); figs.add(b1);
       figs.add(b2); figs.add(b3); 
      anim.add(new Walk(p1,false));
      anim.add(new BrickMove(b1,3));
      anim.add(new BrickMove(b2,3));
      anim.add(new BrickMove(b3,3));

      bu1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          go = true; 
          requestFocus();
        }
      });
        new javax.swing.Timer(80, new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            sp+=0.05;
            if(go){
             float xmove=0;
              int count=0;
              for(Figure f:figs){
                if(f.getPos()<50.0 && f.getType()==0){
                  if(f.askWin()==false){
                    go=false;
                    break;
                  }
                  while(count<3){
                    xmove=500+(int)(Math.random()*(200 + 1));
                    count=0;
                    for(Figure ff:figs){
                      if(ff.getType()==0 && xmove>(ff.getPos()+100))count++;
                    }
                  }
                  f.changeInto("br.png");
                  f.win(false);
                  f.moveTo(xmove,200); 
               }
              }
            

                for(Animation a: anim) { 
                 a.set();
                 a.changeSpeed(sp); 
              }
            }
            repaint();
          }
        }).start();


    addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent evt) {
          if(evt.getKeyCode() == 39) { p1.moveBy(1,0); }
          if(evt.getKeyCode() == 37) { p1.moveBy(-1,0); }
          if(evt.getKeyCode() == KeyEvent.VK_SPACE) { 
            p1.moveBy(0,-200); 
            p1.changeInto("mar3.png");
            for(Figure f:figs){
              if(f.getType()==0 && p1.getPos()-f.getPos()>-70 && p1.getPos()-f.getPos()<100){
                f.win(true);
                f.changeInto("coin.png");
              }
              }
            } 
          repaint(); 
        }
      });
    addKeyListener(new KeyAdapter() {
        public void keyReleased(KeyEvent evt) {
          p1.moveTo(p1.getPos(),500);
          p1.changeInto("mar1.png");
          repaint(); 
        }
      });
    }
    public void paintComponent(Graphics g) {
    	setBackground(Color.WHITE);
      g.drawLine(0,500,700,500);
      for(Figure f: figs) { f.draw(g); }
    }
    public static void main(String[] args) {
      JFrame app = new JFrame();
      app.add(new mario());
      app.setSize(700, 700);
      app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      app.setVisible(true);
    }
    interface Figure {
      public void draw(Graphics g);
      public void moveTo(float x, float y);
      public float getPos();
      public int getType();
      public void setSpeed(float x);
      public void win(boolean b);
      public boolean askWin();
      public void changeInto(String fname);
    }
    interface Animation {
      public void set();
      public void changeSpeed(float v);
    }
    static abstract class SimpleFigure implements Figure {
      float xpos, ypos,width,height;
      BufferedImage img;
      boolean win;
      public SimpleFigure(String fname, float x, float y) {
         xpos = x; ypos = y;
         try {
          img = ImageIO.read(new File(fname));
        } catch(Exception ex) { }
          width = img.getWidth(); height = img.getHeight();
      }
      public void win(boolean b){}
      public boolean askWin(){return win;}
      public void moveTo(float x, float y) { xpos = x; ypos = y; }
      public float getPos(){return xpos;}
      public void draw(Graphics g) { }
    }
    static class Brick extends SimpleFigure{
      boolean win;
      public Brick(String fname, float x, float y) {      
        super(fname,x,y);win=false;
      }
      public int getType(){return 0;}
      public void changeInto(String fname){
        try {
            img = ImageIO.read(new File(fname));
          } catch(Exception ex) { }
      }
      public void win(boolean b){
        win=b;
      }
      public boolean askWin(){return win;}
      public void setSpeed(float x){}
      public void draw(Graphics g) {
       int x = (int)(xpos-width/2), y = (int)(ypos-height/2);
          g.drawImage(img, x, y,100,100, null);
      }
    }
 static class Man extends SimpleFigure{
      float speed;
      public Man(String fname, float x, float y,float s) {
       super(fname,x,y); speed=s;
      }
      public int getType(){return 1;}
      public void moveBy(int x, float y) {
        xpos += x*speed; ypos += y;
      }
      public void setSpeed(float x){speed+=0.001*x;}
      public void changeInto(String fname){
      	try {
          	img = ImageIO.read(new File(fname));
        	} catch(Exception ex) { }
      }
      public void draw(Graphics g) {
       int x = (int)(xpos-width/2), y = (int)(ypos-height/2);
        g.drawImage(img, x, y, 80,80,null);
      }
    }
   

   static class BrickMove implements Animation {
      Figure fig;
      float speed;
      public BrickMove(Figure f,float v) {
       fig = f; speed = v;
      }
      public void changeSpeed(float v){speed=v;}
      public void set() {
        fig.moveTo(fig.getPos()-speed, 200);
      }
    }
    static class Walk implements Animation {
      Figure fig;
      boolean c;
      public Walk(Figure f,boolean b) {
       fig = f; c = b;
      }
      public void changeSpeed(float v){
        fig.setSpeed(v);
      }
      public void set() {
         if(c==false){
            fig.changeInto("mar1.png");
         	  c=true;
		      }
		     else {
			     fig.changeInto("mar2.png");
			     c=false;
		      }
      }
    }
   
   
  }
