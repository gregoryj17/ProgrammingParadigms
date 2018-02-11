import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import javax.swing.JButton;

class View extends JPanel
{
	JButton b1;
	BufferedImage tube_image;
	BufferedImage[] mario_images;
	Model model;
	int scrollPos;

	View(Controller c, Model m)
	{
		//Setting member variables
		c.setView(this);
		this.model=m;
		try{
			this.tube_image = ImageIO.read(new File("tube.png"));
            mario_images = new BufferedImage[5];
            mario_images[0] = loadImage("mario1.png");
            mario_images[1] = loadImage("mario2.png");
            mario_images[2] = loadImage("mario3.png");
            mario_images[3] = loadImage("mario4.png");
            mario_images[4] = loadImage("mario5.png");
		}catch(Exception e){
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
	
	void scroll(int scrollAmount){
	    if(model.legalMarioMove(scrollAmount,scrollPos)){
	        scrollPos += scrollAmount;
        }
	}
	
	public void paintComponent(Graphics g){
		g.setColor(new Color(128,255,255));
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		g.setColor(Color.gray);
		g.drawLine(0, 596, 2000, 596);
		for(int i = findFirstTubeOnScreen(); i < model.tubes.size(); i++)
		{
			Tube t = model.tubes.get(i);
			g.drawImage(tube_image, t.x - scrollPos, t.y, null);
			if(t.x - scrollPos > this.getWidth()){
				break;
			}
		}
		g.drawImage(mario_images[model.mario.pic], model.mario.x, model.mario.y, null);
	}
	
	int findFirstTubeOnScreen()
	{
		int start = 0;
		int end = model.tubes.size();
		while(true)
		{
			int mid = (start + end) / 2;
			if(mid == start)
				return start;
			Tube t = model.tubes.get(mid);
			if(t.x - scrollPos < -100)
				start = mid;
			else
				end = mid;
		}
	}

	BufferedImage loadImage(String filename){
	    BufferedImage b;
	    try{
	        b=ImageIO.read(new File(filename));
        }catch(Exception e){
	        e.printStackTrace();
	        throw new RuntimeException(e);
        }
        return b;
    }
	
}
