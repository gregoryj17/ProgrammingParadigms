import java.util.ArrayList;
import java.util.Comparator;
class Model
{
	ArrayList<Tube> tubes;

	Model()
	{
		tubes = new ArrayList<Tube>();
		/*Tube t = new Tube(400, 300);
		tubes.add(t);*/
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
