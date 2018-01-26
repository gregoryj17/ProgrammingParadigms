public class Tube{
	
	//Tube position
	int x,y;
	final int w=55,h=400;
	
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
	
}
