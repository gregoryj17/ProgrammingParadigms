import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

class Model
{
	ArrayList<Tube> tubes;
	Mario mario;

	Model()
	{
		tubes = new ArrayList<Tube>();
		mario = new Mario();
	}

	public void mousePressed(int x, int y, int scrollPos){
		boolean tubeClicked = false;
		for(Tube t : tubes){
			if(t.wasClicked(x+scrollPos, y)){
				tubes.remove(t);
				tubeClicked = true;
				break;
			}
		}
		if(!tubeClicked){
			Tube t = new Tube(x+scrollPos, y);
			tubes.add(t);
			TubeComparator tc = new TubeComparator();
			tubes.sort(tc);
		}
	}

	void saveState(){
		Json ob = Json.newObject();
		Json tmpList = Json.newList();
		ob.add("Tubes", tmpList);
		for(int i = 0; i < tubes.size(); i++){
			tmpList.add(tubes.get(i).marshal());
		}
		ob.save("model.json");
		System.out.println("Current state saved.");
	}
	
	void loadState(){
		tubes = new ArrayList<Tube>();
		Json ob = Json.load("model.json");
		Json list = ob.get("Tubes");
		for(int i=0; i<list.size(); i++){
			tubes.add(new Tube(list.get(i)));
		}
		System.out.println("Previous state loaded.");
	}

	public void update()
	{
	    marioDropDistance();
		mario.update();
	}

	boolean legalMarioMove(int scrollAmount,int scrollPos){
        Iterator<Tube> it = tubes.iterator();
        while(it.hasNext()){
            Tube t = it.next();
            if(mario.collidesWith(t.x,t.y,t.w,t.h,scrollAmount,scrollPos)){
                return false;
            }
        }
	    return true;
    }

    void marioDropDistance(){
	    int dropDistance = Integer.MAX_VALUE;
        Iterator<Tube> it = tubes.iterator();
        while(it.hasNext()){
            Tube t = it.next();
            if(mario.collidesX(t.x,t.w)){
                if((t.y-(mario.y+mario.height))<dropDistance){
                    dropDistance = (t.y-(mario.y+mario.height));
                }
            }
        }
        mario.setDropDistance(dropDistance);
    }

}


class TubeComparator implements Comparator<Tube>
{
	public int compare(Tube a, Tube b)
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
