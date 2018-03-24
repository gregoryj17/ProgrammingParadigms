import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Sprite {

    int x,y,w,h;
    Model m;
    boolean toRemove = false;

    abstract void update();

    abstract void draw(Graphics g);

    abstract BufferedImage getImage();

    Sprite(){

	}

    Sprite(Sprite s){
    	x=s.x;
    	y=s.y;
    	w=s.w;
    	h=s.h;
    	toRemove=s.toRemove;
	}

    void setModel(Model mod){
    	m=mod;
	}

    boolean collidesWith(int xx, int ww, int yy, int hh){
    	if(x+w<xx)return false;
    	if(y+h<yy)return false;
    	if(xx+ww<x)return false;
    	if(yy+hh<y)return false;
    	return true;
	}

	int xOverlap(int xx, int ww){
    	if(x+w<xx||xx+ww<x)return Integer.MAX_VALUE;
    	return (x+w-xx);
	}

	int yOverlap(int yy, int hh){
    	if(y+h<yy||yy+hh<y)return Integer.MAX_VALUE;
    	return (y+h-yy);
	}

	boolean collidesWith(Sprite s){
    	if(s==this)return false;
    	if(x+w<s.x)return false;
    	if(y+h<s.y)return false;
    	if(s.x+s.w<x)return false;
    	if(s.y+s.h<y)return false;
    	return true;
	}

	int xOverlap(Sprite s){
    	if(x+w<s.x||s.x+s.w<x)return Integer.MAX_VALUE;
    	return (x+w-s.x);
	}

	int yOverlap(Sprite s){
		if(y+h<s.y||s.y+s.h<y)return Integer.MAX_VALUE;
		return (y+h-s.y);
	}

	Sprite collidesWith(ArrayList<Sprite> s){
    	for(Sprite spr : s){
    		if(spr==this)continue;
    		if(collidesWith(spr)){
    			return spr;
			}
		}
		return null;
	}

}
