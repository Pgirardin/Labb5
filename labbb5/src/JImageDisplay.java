import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.JComponent;


 class JImageDisplay extends JComponent{
     private BufferedImage buf;

     public JImageDisplay(int widht, int height){
        buf = new BufferedImage(widht, height, BufferedImage.TYPE_INT_RGB);
        super.setPreferredSize(new Dimension(widht,height));
     }

     //Рисует изображение в компоненте
     @Override
     protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         g.drawImage (buf, 0, 0, buf.getWidth(), buf.getHeight(), null);
     }

     // Красит все пиксели в черный
     public void clearImage(){
         int widht = getWidth();
         int height = getHeight();
         int[] rgbArray = new int[widht*height];
         buf.setRGB(0,0,widht,height,rgbArray,0,1);

     }

     // Красит пиксель в нужный цвет 
     public void drawPixel(int x, int y, int color){
         buf.setRGB(x,y, color);
     }

    public BufferedImage getImage() {
        return buf;
     }
     
 }