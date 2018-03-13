import javax.swing.*;
import java.awt.*;

public class Game extends JFrame
{
	Model model;
	Controller controller;
	View view;
	
	public Game()
	{
		//Initializing member variables
		model = new Model();
		controller = new Controller(model);
		view = new View(controller, model);
		
		//Setting JFrame settings
		this.setTitle("Mario Game");
		this.setSize(1000, 1000);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		view.addMouseListener(controller);
		this.addKeyListener(controller);
		this.setFocusable(true);

		model.loadState("start.json");

	}

	public void run()
	{
		while(true)
		{
			controller.update();
			model.update();
			view.repaint(); // Indirectly calls View.paintComponent
			Toolkit.getDefaultToolkit().sync(); // Updates screen

			// Go to sleep for 50 miliseconds
			try
			{
				Thread.sleep(40);
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			//System.out.println("hi");
		}
	}

	public static void main(String[] args)
	{
		Game g = new Game();
		g.run();
	}
	

	
}
