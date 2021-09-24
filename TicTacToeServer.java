import java.io.IOException;
import java.net.ServerSocket;

/**
 * This class launches the Server of the Tic Tac Toe game
 * 
 * @author areebnadeem7
 *
 */
public class TicTacToeServer {

	/**
	 * This method creates an object of the Game class and sets up the Server
	 * 
	 * @param args Unused
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Tic Tac Toe Server is Running...");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("Server Stopped.");
			}
		}));

		try (var listener = new ServerSocket(58901)) {

			while (true) {
				Game game = new Game(listener);
				game.begin();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
