import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

class Model
{
	ArrayList<Sprite> sprites;
	Mario mario;
	int scrollPos;

	Model()
	{
		sprites = new ArrayList<Sprite>();
		mario = new Mario(this);
		sprites.add(mario);
	}

	public void mousePressed(int x, int y, int scrollPos){
		boolean tubeClicked = false;
		for(Sprite t : sprites){
			if(!(t instanceof Tube))continue;
			if(((Tube)t).wasClicked(x+scrollPos, y)){
				sprites.remove(t);
				tubeClicked = true;
				break;
			}
		}
		if(!tubeClicked){
			Tube t = new Tube(x+scrollPos, y);
			sprites.add(t);
			TubeComparator tc = new TubeComparator();
			sprites.sort(tc);
		}
	}

	public void spawnGoomba(int x, int y){
		Goomba g = new Goomba(x,y);
		sprites.add(g);
	}

	public void shootFlame(){
		Fireball f = new Fireball(mario.x,mario.y);
		sprites.add(f);
	}

	void scroll(int scrollAmount){
		scrollPos += scrollAmount;
	}

	void saveState(){
		Json ob = Json.newObject();
		Json tmpList = Json.newList();
		ob.add("Tubes", tmpList);
		for(int i = 0; i < sprites.size(); i++){
			if(!(sprites.get(i) instanceof Mario))tmpList.add(((Tube)sprites.get(i)).marshal());
		}
		ob.save("model.json");
		System.out.println("Current state saved.");
	}
	
	void loadState(){
		sprites = new ArrayList<Sprite>();
		Json ob = Json.load("model.json");
		Json list = ob.get("Tubes");
		for(int i=0; i<list.size(); i++){
			sprites.add(new Tube(list.get(i)));
		}
		System.out.println("Previous state loaded.");
	}

	public void update()
	{
		for(Sprite s : sprites){
			s.update();
		}
		for(int i=0;i<sprites.size();i++){
			if(sprites.get(i).toRemove){
				sprites.remove(i);
				i--;
			}
		}
	}

}


class TubeComparator implements Comparator<Sprite>
{
	public int compare(Sprite a, Sprite b)
	{
		if(a.x < b.x)
			return -1;
		else if(a.x > b.x)
			return 1;
		else
			return 0;
	}

	public boolean equals(Object obj)
	{
		return false;
	}
}
