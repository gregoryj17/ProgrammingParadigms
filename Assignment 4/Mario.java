import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Mario extends Sprite {

    double vert_vel=0;
    int frame;
    int lastGrounded;
    int pic=0;
    boolean moving=false;
    static BufferedImage[] images = new BufferedImage[5];
    int prevX,prevY;

    public Mario(Model model){
    	x=200;
    	y=0;
    	w=60;
    	h=95;
		m=model;
	}

    void update()
    {
		vert_vel += 1.2;
		prevY=y;
		y += vert_vel;
		if(y > 500)
		{
			vert_vel = 0.0;
			y = 500; // snap back to the ground
			lastGrounded=frame;
		}
		prevX=x;
		x=m.scrollPos+200;


		for(Sprite s : m.sprites){
			if(collidesWith(s)){
				getOutOfTube(s);
			}
		}

        frame++;
        if(moving)pic=(pic+1)%5;
    }

    void getOutOfTube(Sprite s){
    	if(prevX+w<s.x&&x+w>=s.x){
    		x=s.x-w-1;
    		m.scrollPos=x-200;
		}
		else if(prevX>s.x+s.w&&x<=s.x+s.w){
    		x=s.x+s.w+1;
    		m.scrollPos=x-200;
		}

		if(prevY+h<s.y&&y+h>=s.y){
    		y=s.y-h-1;
    		vert_vel=0;
    		lastGrounded=frame;
		}
		else if(prevY>s.y+s.h&&y<=s.y+s.h){
    		y=s.y+s.h+1;
    		vert_vel=0;
		}
	}

    void draw(Graphics g){
		g.drawImage(getImage(),x-m.scrollPos,y,null);
    }

    void jump(){
        if(frame-lastGrounded<=5) {
            vert_vel -= 5;
        }
    }

    void moving(boolean left, boolean right){
        moving = ((left^right)||vert_vel!=0);
    }

    BufferedImage getImage(){
        try{
            if(images[pic]==null){
                images[pic] = ImageIO.read(new File("mario"+(pic+1)+".png"));
            }
        }catch(Exception e){
            e.printStackTrace(System.err);
            System.exit(1);
        }

        return images[pic];
    }

}
