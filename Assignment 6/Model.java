import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

class Model
{
	ArrayList<Sprite> sprites;
	Mario mario;
	int scrollPos;
	int jumpCount=0,fireballCount=0,deadGoombas=0;
	final int run=0, run_and_jump=1, run_and_shoot=2;

	Model()
	{
		sprites = new ArrayList<Sprite>();
		mario = new Mario(this);
		mario.setModel(this);
		sprites.add(mario);
	}

	Model(Model oldModel){
		sprites=new ArrayList<Sprite>();
		scrollPos = oldModel.scrollPos;
		jumpCount=oldModel.jumpCount;
		fireballCount=oldModel.fireballCount;
		deadGoombas=oldModel.deadGoombas;
		for(int i=0;i<oldModel.sprites.size();i++){
			Sprite s = oldModel.sprites.get(i);
			if(s instanceof Fireball){
				s = new Fireball((Fireball)s);
			}
			else if(s instanceof Goomba){
				s = new Goomba((Goomba)s);
			}
			else if(s instanceof Mario){
				s = new Mario((Mario)s);
				mario=(Mario)s;
			}
			else if(s instanceof Tube){
				s = new Tube((Tube)s);
			}
			s.setModel(this);
			sprites.add(s);
		}
	}

	double evaluateAction(int action, int depth) {

		int d=35,k=6;

		// Evaluate the state
		if(depth >= d)
			return scrollPos + 20 * deadGoombas - fireballCount - jumpCount;

		// Simulate the action
		Model copy = new Model(this); // uses the copy constructor
		copy.doAction(action);
		copy.update(); // advance simulated time

		// Recurse
		if(depth % k != 0)
			return copy.evaluateAction(run, depth + 1);
		else {
			double best = copy.evaluateAction(run, depth + 1);
			best = Math.max(best,
					copy.evaluateAction(run_and_jump, depth + 1));
			best = Math.max(best,
					copy.evaluateAction(run_and_shoot, depth + 1));
			return best;
		}
	}

	void doAction(int action){
		int scrollSpeed = 6;
		if(action==run_and_shoot) {
			scroll(scrollSpeed);
			shootFlame();
		}
		else if(action==run_and_jump) {
			scroll(scrollSpeed);
			mario.jump();
		}
		else {
			scroll(scrollSpeed);
		}
	}

	public void mousePressed(int x, int y, int type){
		if(type==0) {
			boolean tubeClicked = false;
			for (Sprite t : sprites) {
				if (!(t instanceof Tube)) continue;
				if (((Tube) t).wasClicked(x + scrollPos, y)) {
					sprites.remove(t);
					tubeClicked = true;
					break;
				}
			}
			if (!tubeClicked) {
				Tube t = new Tube(x + scrollPos, y);
				t.setModel(this);
				sprites.add(t);
			}
		}
		else if(type==1){
			spawnGoomba(x+scrollPos);
		}
		else if(type==2){
			Fireball f=new Fireball(x+scrollPos,y);
			f.setModel(this);
			sprites.add(f);
		}
	}

	public void spawnGoomba(int x){
		Goomba g = new Goomba(x,478);
		g.setModel(this);
		sprites.add(g);
	}

	public void shootFlame(){
		Fireball f = new Fireball(mario.x,mario.y);
		f.setModel(this);
		sprites.add(f);
		fireballCount++;
	}

	void scroll(int scrollAmount){
		scrollPos += scrollAmount;
	}

	void saveState(){
		Json ob = Json.newObject();
		Json tmpList = Json.newList();
		ob.add("Tubes", tmpList);
		for(int i = 0; i < sprites.size(); i++){
			if((sprites.get(i) instanceof Tube))tmpList.add(((Tube)sprites.get(i)).marshal());
		}
		Json gmbaList = Json.newList();
		ob.add("Goombas",gmbaList);
		for(Sprite s : sprites){
			if(s instanceof Goomba)gmbaList.add(((Goomba)(s)).marshal());
		}
		ob.save("model.json");
		System.out.println("Current state saved.");
	}

	void saveState(boolean silent){
		Json ob = Json.newObject();
		Json tmpList = Json.newList();
		ob.add("Tubes", tmpList);
		for(int i = 0; i < sprites.size(); i++){
			if((sprites.get(i) instanceof Tube))tmpList.add(((Tube)sprites.get(i)).marshal());
		}
		Json gmbaList = Json.newList();
		ob.add("Goombas",gmbaList);
		for(Sprite s : sprites){
			if(s instanceof Goomba)gmbaList.add(((Goomba)(s)).marshal());
		}
		ob.save("model.json");
		if(!silent)System.out.println("Current state saved.");
	}
	
	void loadState(){
		sprites = new ArrayList<Sprite>();
		sprites.add(mario);
		Json ob = Json.load("model.json");
		Json list = ob.get("Tubes");
		for(int i=0; i<list.size(); i++){
			sprites.add(new Tube(list.get(i)));
		}
		list = ob.get("Goombas");
		for(int i=0; i<list.size(); i++){
			sprites.add(new Goomba(list.get(i)));
		}
		for(int i=0;i<sprites.size();i++){
			sprites.get(i).setModel(this);
		}
		System.out.println("Previous state loaded.");
	}

	void loadState(boolean silent){
		sprites = new ArrayList<Sprite>();
		sprites.add(mario);
		Json ob = Json.load("model.json");
		Json list = ob.get("Tubes");
		for(int i=0; i<list.size(); i++){
			sprites.add(new Tube(list.get(i)));
		}
		list = ob.get("Goombas");
		for(int i=0; i<list.size(); i++){
			sprites.add(new Goomba(list.get(i)));
		}
		for(int i=0;i<sprites.size();i++){
			sprites.get(i).setModel(this);
		}
		if(!silent)System.out.println("Previous state loaded.");
	}

	void loadState(String filename){
		sprites = new ArrayList<Sprite>();
		sprites.add(mario);
		Json ob = Json.load(filename);
		Json list = ob.get("Tubes");
		for(int i=0; i<list.size(); i++){
			sprites.add(new Tube(list.get(i)));
		}
		list = ob.get("Goombas");
		for(int i=0; i<list.size(); i++){
			sprites.add(new Goomba(list.get(i)));
		}
		for(int i=0;i<sprites.size();i++){
			sprites.get(i).setModel(this);
		}
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
