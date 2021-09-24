import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class develops the GUI display of the Tic Tac Toe game.
 * 
 * @author areebnadeem7
 *
 */
public class Display {

	JFrame frame;
	JMenuBar menubar;
	JMenu control_menu;
	JMenu help_menu;
	JMenuItem exit_item;
	JMenuItem instructions_item;
	JPanel south_panel;
	JPanel north_panel;
	JPanel board;
	JTextField name_field;
	JButton submit_btn;
	JLabel message;
	JButton cell[];
	JButton cellClicked;

	/**
	 * The Display() constructor initializes all the instance variables and calls on
	 * the go() fucntion that will launch the GUI of the game.
	 */
	public Display() {
		frame = new JFrame("Tic Tac Toe");
		menubar = new JMenuBar();

		control_menu = new JMenu("Control");
		exit_item = new JMenuItem("Exit");
		help_menu = new JMenu("Help");
		instructions_item = new JMenuItem("Instructions");

		north_panel = new JPanel();
		message = new JLabel("Enter your player name..");

		south_panel = new JPanel();
		submit_btn = new JButton("Submit");
		name_field = new JTextField(10);

		board = new JPanel();
		cell = new JButton[9];

		go();
	}

	/**
	 * This method launches the GUI of the game for the player.
	 */
	public void go() {
		control_menu.add(exit_item);
		menubar.add(control_menu);

		exit_item.addActionListener(e -> {
			frame.dispose();
		});

		help_menu.add(instructions_item);
		menubar.add(help_menu);

		instructions_item.addActionListener(e -> {
			JOptionPane.showMessageDialog(frame,
					"Some information about the game:\n" + "Criteria for a valid move:\n"
							+ "-The move is not occupied by any mark.\n" + "-The move is made in the player's turn.\n"
							+ "-The move is made within the 3x3 board.\n"
							+ "The game would continue and switch among the opposite player until it reaches either one"
							+ "of the following conditions:\n" + "-Player 1 wins.\n" + "Player 2 wins.\n" + "-Draw.");
		});

		frame.setJMenuBar(menubar);

		north_panel.add(message);
		north_panel.setLayout(new FlowLayout(FlowLayout.LEFT));

		frame.add(north_panel, BorderLayout.NORTH);

		south_panel.add(name_field);
		south_panel.add(submit_btn);

		frame.add(south_panel, BorderLayout.SOUTH);

		board.setBackground(Color.white);
		board.setLayout(new GridLayout(3, 3));

		for (int i = 0; i < 9; i++) {
			cell[i] = new JButton("");
			cell[i].setFont(new Font("Serif", Font.BOLD, 25));
			cell[i].setFocusPainted(false);
			cell[i].setEnabled(false);
			board.add(cell[i]);
		}

		frame.add(board);

		frame.setSize(400, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}