import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import javax.swing.JButton;
import java.awt.Color;

class View extends JPanel
{
	JButton b1;
	BufferedImage tube_image;
	Model model;
	int scrollPos;

	View(Controller c, Model m)
	{
		//Setting member variables
		c.setView(this);
		this.model=m;
		try{
			this.tube_image = ImageIO.read(new File("tube.png"));
		}catch(Exception e){
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
	
	void scroll(int scrollAmount){
		scrollPos += scrollAmount;
	}
	
	public void paintComponent(Graphics g){
		g.setColor(new Color(128,255,255));
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		for(int i = findFirstTubeOnScreen(); i < model.tubes.size(); i++)
		{
			Tube t = model.tubes.get(i);
			g.drawImage(tube_image, t.x - scrollPos, t.y, null);
			if(t.x - scrollPos > this.getWidth()){
				break;
			}
		}
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
	
}
