import java.awt.Graphics;

import javax.swing.JComponent;

public class DrawHandler extends JComponent {
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g) {
		g.drawRect(50, 50, 20, 20);
		
		g.fillRect(100, 100, 50, 50);
	}
	
//	public void paintC
}
