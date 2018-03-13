import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Goomba extends Sprite {

	static BufferedImage image;
	static BufferedImage burnedimage;

	boolean right;
	int turnSteps;
	int curSteps;
	final int stepSize=1;
	int prevX;
	int frame=0;
	int burnedframe=-1;

	public Goomba(int x, int y){
		this.x=x;
		this.y=y;
		right=true;
		turnSteps=300;
		curSteps=0;
		w=99;
		h=118;
	}

	public Goomba(Json json){
		this.x=(int)json.getLong("x");
		this.y=(int)json.getLong("y");
		right=json.getBool("right");
		turnSteps=(int)json.getLong("turnSteps");
		curSteps=(int)json.getLong("curSteps");
		w=99;
		h=118;
	}


	void update() {
		prevX=x;
		x+=stepSize*(right?1:-1);
		curSteps++;
		if(curSteps>=turnSteps){
			curSteps=0;
			right=!right;
		}
		for(Sprite s : m.sprites){
			if(collidesWith(s)){
				if(s instanceof Tube||s instanceof Mario)getOutOfObject(s);
				else if(s instanceof Fireball){
					if(burnedframe==-1)burnedframe=frame;
					s.toRemove=true;
				}
			}
		}
		if(burnedframe!=-1&&frame>=burnedframe+10){
			toRemove=true;
		}
		frame++;
	}

	void getOutOfObject(Sprite s){
		if(prevX+w<s.x&&x+w>=s.x){
			x=s.x-w-1;
		}
		else if(prevX>s.x+s.w&&x<=s.x+s.w){
			x=s.x+s.w+1;
		}
		curSteps=0;
		right=!right;
	}

	void draw(Graphics g){
		g.drawImage(getImage(),x-m.scrollPos,y,null);
	}

	BufferedImage getImage(){
		try{
			if(image==null){
				image=ImageIO.read(new File("goomba.png"));
			}
			else if(burnedframe!=-1&&burnedimage==null){
				burnedimage=ImageIO.read(new File("goomba_fire.png"));
			}
		}catch(Exception e){
			e.printStackTrace(System.err);
			System.exit(1);
		}
		if(burnedframe!=-1)return burnedimage;
		return image;
	}

	Json marshal(){
		Json ob = Json.newObject();
		ob.add("x",x);
		ob.add("y",y);
		ob.add("right",right);
		ob.add("turnSteps",turnSteps);
		ob.add("curSteps",curSteps);
		return ob;
	}

}
