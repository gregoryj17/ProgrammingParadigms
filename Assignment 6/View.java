import javax.swing.JPanel;
import java.awt.*;

class View extends JPanel
{
	Model model;

	View(Controller c, Model m)
	{
		//Setting member variables
		c.setView(this);
		this.model=m;
	}
	
	public void paintComponent(Graphics g){
		g.setColor(new Color(128,255,255));
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		g.setColor(Color.gray);
		g.drawLine(0, 596, 2000, 596);
		for(Sprite s : model.sprites) {
			s.draw(g);
		}
	}
	
	int findFirstTubeOnScreen()
	{
		int start = 0;
		int end = model.sprites.size();
		while(true)
		{
			int mid = (start + end) / 2;
			if(mid == start)
				return start;
			Sprite t = model.sprites.get(mid);
			if(t.x - model.scrollPos < -100)
				start = mid;
			else
				end = mid;
		}
	}
	
}
