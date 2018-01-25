import javax.swing.JFrame;

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
		this.setTitle("Turtle attack!");
		this.setSize(500, 500);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		view.addMouseListener(controller);
		this.addKeyListener(controller);
		this.setFocusable(true);
	}

	public void run()
	{
		while(true)
		{
			controller.update();
			model.update();
			view.repaint(); // Indirectly calls View.paintComponent

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
