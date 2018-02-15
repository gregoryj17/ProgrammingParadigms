import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class Controller implements ActionListener, MouseListener, KeyListener
{
	View view;
	Model model;
	boolean keyLeft;
	boolean keyRight;
	boolean keyUp;
	boolean keyDown;
	boolean keySpace;
	
	Controller(Model m)
	{
		model = m;
	}

	void setView(View v){
		view = v;
	}
	
	public void actionPerformed(ActionEvent e){
		
	}
	
	public void mousePressed(MouseEvent e)
	{
		model.mousePressed(e.getX(), e.getY(), view.scrollPos);
	}

	public void mouseReleased(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) {	}
	
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: keyRight = true; break;
			case KeyEvent.VK_LEFT: keyLeft = true; break;
			case KeyEvent.VK_UP: keyUp = true; break;
			case KeyEvent.VK_DOWN: keyDown = true; break;
			case KeyEvent.VK_S: model.saveState(); break;
			case KeyEvent.VK_L: model.loadState(); break;
            case KeyEvent.VK_SPACE: keySpace = true; break;
		}
	}

	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: keyRight = false; break;
			case KeyEvent.VK_LEFT: keyLeft = false; break;
			case KeyEvent.VK_UP: keyUp = false; break;
			case KeyEvent.VK_DOWN: keyDown = false; break;
            case KeyEvent.VK_SPACE: keySpace = false; break;
		}
	}

	public void keyTyped(KeyEvent e)
	{
	}

	void update()
	{
		int scrollSpeed = 4;
		if(keyLeft){
			view.scroll(-1*scrollSpeed);
		}
		if(keyRight){
			view.scroll(scrollSpeed);
		}
		if(keyUp||keySpace){
		    model.mario.jump();
        }
        model.mario.moving(keyLeft,keyRight);
	}
	
}
