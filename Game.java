import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * This class handles the Server of the game. It sends and recieves requests to
 * and from the clients.
 * 
 * @author areebnadeem7
 *
 */
public class Game {
	private ServerSocket serverSocket;

	private String[] cells = new String[9];

	private Client currentClient;

	/**
	 * Initializes its instance variable, gameSocket and assigns serverSocket to it.
	 * 
	 * @param gameSocket
	 */
	public Game(ServerSocket gameSocket) {
		this.serverSocket = gameSocket;

	}

	/**
	 * Accepts first two socket connections and creates two playerss for the
	 * multiplayer game.
	 */
	public void begin() {

		try {
			var pool = Executors.newFixedThreadPool(200);

			pool.execute(new Client(serverSocket.accept(), "X"));

			pool.execute(new Client(serverSocket.accept(), "O"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This is a synchronized fucntion that allows only one player at a time to make
	 * their move on the tic tac toe board. It also checks if player's move is valid
	 * before placing it on the board.
	 * 
	 * @param client  for player who has made the most recent move
	 * @param cellNum for location of cell where move has been made
	 * @throws Exception
	 */
	public synchronized void makeMark(Client client, int cellNum) throws Exception {

		if (client.oppClient.clientName == null || client.clientName == null)
			throw new Exception("No Name");

		else if (client != currentClient || client.oppClient == null || cells[cellNum] != null)
			throw new Exception("Invalid");

		cells[cellNum] = currentClient.move;

		currentClient = currentClient.oppClient;
	}

	/**
	 * This Client class sets up communication between client and the server.
	 * 
	 * @author areebnadeem7
	 *
	 */
	class Client implements Runnable {

		Socket socket;
		Scanner input;
		PrintWriter output;
		int clientCount;

		String clientName;
		String move;
		Client oppClient;

		/**
		 * This constructor initializes instance variables and also sets up the socket
		 * that needs to be connected to the server
		 * 
		 * @param socket for connection to server
		 * @param move   for the mark the player can make on the board ('X' or 'O')
		 * @throws IOException
		 */
		public Client(Socket socket, String move) throws IOException {
			this.socket = socket;
			this.move = move;

			input = new Scanner(socket.getInputStream());
			output = new PrintWriter(socket.getOutputStream(), true);
		}

		/**
		 * This is called when a thread of any Client is executed. It calls on another
		 * method initiate() that handles communication between socket and server. It
		 * also checks whether players are still present in the game or not.
		 */
		public void run() {

			try {
				initiate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (oppClient != null && oppClient.output != null) {
					oppClient.output.println("PlayerLeft");
				}
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void initiate() throws IOException {

			output.println("Starting" + move);

			if (move == "X") {
				currentClient = this;
			} else if (move != "X") {
				oppClient = currentClient;
				oppClient.oppClient = this;
			}

			while (input.hasNextLine()) {
				String req = input.nextLine();
				if (req.startsWith("Name")) {
					this.clientName = req.substring(4);
				}
				if (req == ("Leave")) {
					return;
				} else if (req.startsWith("Mark")) {
					play(Integer.parseInt(req.substring(5)));
				}
			}
		}

		/**
		 * Checks whether game has been won by any player or not
		 * 
		 * @return true if game has been won. If no one has won, it returns false.
		 */
		public boolean CheckForWin() {
			// check for diagonal victory
			if ((cells[0] == cells[4] && cells[4] == cells[8]) && cells[4] != null
					|| ((cells[2] == cells[4] && cells[4] == cells[6]) && cells[4] != null)) {
				return true;
			}

			// check for horizontal victory
			else if ((cells[0] == cells[1] && cells[1] == cells[2]) && cells[0] != null
					|| ((cells[3] == cells[4] && cells[4] == cells[5]) && cells[3] != null
							|| ((cells[6] == cells[7] && cells[7] == cells[8]) && cells[6] != null))) {
				return true;
			}
			// check for vertical victory
			else if ((cells[0] == cells[3] && cells[3] == cells[6]) && cells[0] != null
					|| ((cells[1] == cells[4] && cells[4] == cells[7]) && cells[1] != null
							|| ((cells[2] == cells[5] && cells[5] == cells[8]) && cells[2] != null))) {
				return true;
			}

			return false;
		}

		/**
		 * Checks whether the tic tac toe board is fully occupied with marks or not.
		 * 
		 * @return true if there is no empty cell on the board, otherwise returns false.
		 */
		public boolean CheckForDraw() {

			for (int i = 0; i < 9; i++) {

				if (cells[i] == null) {
					return false;
				}

			}

			return true;
		}

		private void play(int cellNum) {
			try {
				makeMark(this, cellNum);
				output.println("Move");
				oppClient.output.println("Opp_Move" + cellNum);

				if (CheckForDraw()) {
					output.println("Tie");
					oppClient.output.println("Tie");
				}

				else if (CheckForWin()) {
					output.println("Won");
					oppClient.output.println("Lost");

				}

			} catch (Exception e) {
				output.println(e.getMessage());
			}
		}
	}
}
