import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Controller {

	private Display board;
	private Socket sock;
	private PrintWriter out;
	private BufferedReader in;

	public Controller(Display board) {
		this.board = board;
	}

	public void begin() {
		try {
			this.sock = new Socket("127.0.0.1", 58901);
			this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			this.out = new PrintWriter(sock.getOutputStream(), true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		board.submit_btn.addActionListener(e -> {
			String name = board.name_field.getText();
			Boolean nameCheck = name.trim().isEmpty();

			if (!nameCheck) {
				board.frame.setTitle("Tic Tac Toe-Player: " + name);
				board.message.setText("WELCOME " + name);
				board.submit_btn.setEnabled(false);
				board.name_field.setEditable(false);

				out.println("Name" + name);

				for (int i = 0; i < 9; i++) {
					board.cell[i].setEnabled(true);
				}

			}

		});

		// CHECK THIS OUT
		int i = 0;
		while (i < 9) {

			final int clicked = i;

			board.cell[i].addActionListener(e -> {
				out.println("Mark " + clicked);
				board.cellClicked = board.cell[clicked];

			});
			i++;
		}

	}

	public void game() throws Exception {

		try {
			String server_response;

			char mark = 'x';
			char oppMark = 'x'; 

			server_response = in.readLine();
			while (server_response != null) {

				if (server_response.startsWith("Starting")) {

					mark = server_response.charAt(8);
					if (mark == 'X')
						oppMark = 'O';
					else
						oppMark = 'X';

				} else if (server_response.startsWith("Move")) {

					board.message.setText("Valid move, wait for your opponent.");

					if (mark == 'X') {
						board.cellClicked.setForeground(Color.RED);
					} else {
						board.cellClicked.setForeground(Color.GREEN);
					}

					board.cellClicked.setText("" + mark);

				} else if (server_response.startsWith("Opp_Move")) {

					if (oppMark == 'X') {
						board.cell[Integer.parseInt(server_response.substring(8))].setForeground(Color.RED);
					} else {
						board.cell[Integer.parseInt(server_response.substring(8))].setForeground(Color.GREEN);
					}
////
					board.cell[Integer.parseInt(server_response.substring(8))].setText("" + oppMark);
					board.message.setText("Your opponent has moved, now is your turn.");

				}

				else if (server_response.startsWith("Won")) {

					JOptionPane.showMessageDialog(board.frame, "Congratulations. You win.");
					break;

				} else if (server_response.startsWith("Lost")) {

					JOptionPane.showMessageDialog(board.frame, "You lose.");
					break;

				} else if (server_response.startsWith("Tie")) {

					JOptionPane.showMessageDialog(board.frame, "Draw.");
					break;

				} else if (server_response.startsWith("No")) {

					board.message.setText("Wait for opponenent to enter name.");
					Thread.sleep(500);
					String name = board.name_field.getText();
					board.message.setText("WELCOME " + name);

				} else if (server_response.startsWith("PlayerLeft")) {

					JOptionPane.showMessageDialog(board.frame, "Game Ends. One of the players left.");
					break;

				} else if (server_response.startsWith("Invalid")) {
					;
				}
				server_response = in.readLine(); 
			}
			out.println("Leave");

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

		}
		this.sock.close();
		board.frame.dispose();
	}
}
