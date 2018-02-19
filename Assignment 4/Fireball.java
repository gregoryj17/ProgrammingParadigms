import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Fireball extends Sprite {

	static BufferedImage image;

	public Fireball(int xx, int yy){
		x=xx;
		y=yy;
		w=47;
		h=47;
	}

	public void update(){

	}

	public void draw(Graphics g){

	}

	public BufferedImage getImage(){
		try{
			if(image==null){
				image= ImageIO.read(new File("fireball.png"));
			}
		}catch(Exception e){
			e.printStackTrace(System.err);
			System.exit(1);
		}
		return image;
	}

}
