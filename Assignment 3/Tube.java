import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Tube{
	
	//Tube position
	int x,y;
	final int w=55,h=400;
	static BufferedImage image;
	
	//Constructs a tube with a position
	public Tube(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public Tube(Json json){
		this.x=(int)json.getLong("x");
		this.y=(int)json.getLong("y");
	}
	
	boolean wasClicked(int clickX, int clickY){
		return (clickX>=x&&clickX<=x+w&&clickY>=y&&clickY<=y+h);
	}
	
	Json marshal(){
		Json ob = Json.newObject();
		ob.add("x",x);
		ob.add("y",y);
		return ob;
	}

	BufferedImage getImage(){
		try{
			if(image==null){
				image=ImageIO.read(new File("tube.png"));
			}
		}catch(Exception e){
			e.printStackTrace(System.err);
			System.exit(1);
		}
		return image;
	}
	
}
