import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Goomba extends Sprite {

	static BufferedImage image;

	boolean right;
	int turnSteps;
	int curSteps;
	final int stepSize=1;

	public Goomba(int x, int y){
		this.x=x;
		this.y=y;
		right=true;
		turnSteps=30;
		curSteps=0;
		w=99;
		h=118;
	}

	void update() {
		x+=stepSize*(right?1:-1);
		curSteps++;
		if(curSteps>=turnSteps){
			curSteps=0;
			right=!right;
		}
	}

	void draw(Graphics g){
		g.drawImage(getImage(),x-m.scrollPos,y,null);
	}

	BufferedImage getImage(){
		try{
			if(image==null){
				image=ImageIO.read(new File("goomba.png"));
			}
		}catch(Exception e){
			e.printStackTrace(System.err);
			System.exit(1);
		}
		return image;
	}

}
