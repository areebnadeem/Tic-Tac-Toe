public class Client {

	public static void main(String[] args) {

		Display board = new Display();
		Controller controller = new Controller(board);
		controller.begin();

		try {
			controller.game();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
