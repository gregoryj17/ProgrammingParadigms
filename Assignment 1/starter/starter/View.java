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
	BufferedImage turtle_image;
	Model model;

	View(Controller c, Model m)
	{
		//Creating a clickable button
		b1 = new JButton("Never push me");
		b1.addActionListener(c);
		this.add(b1);
		//Setting member variables
		c.setView(this);
		this.model=m;
		try{
			this.turtle_image = ImageIO.read(new File("turtle.png"));
		}catch(Exception e){
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
	
	public void paintComponent(Graphics g){
		g.setColor(new Color(128,255,255));
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		g.drawImage(this.turtle_image,model.turtle_x,model.turtle_y,null);
	}
	
	void removeButton(){
		this.remove(b1);
		this.repaint();
	}
	
}
