import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

public class GuiTest {

	@SuppressWarnings("deprecation")
	public static void main(String[] args){
		
		JFrame frame = new JFrame();
		JPanel testPanel = new JPanel(new GridBagLayout());
		GridBagConstraints constrain = new GridBagConstraints();
		
		
		JButton testBut1 = new JButton("Button 1");
		constrain.gridx = 0;
		Action test = new Action();
		
		constrain.gridy = 0;
		testBut1.(testPanel.setBackground(Color.BLACK));
		testPanel.add(testBut1,constrain);

		JButton testBut2 = new JButton("Button 2");
		constrain.gridx = 2;
		constrain.gridy = 0;
		testPanel.add(testBut2,constrain);
		System.out.println("Hello");
		frame.add(testPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}	
}
