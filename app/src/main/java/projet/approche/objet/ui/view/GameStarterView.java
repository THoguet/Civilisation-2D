package projet.approche.objet.ui.view;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import projet.approche.objet.application.App;
import projet.approche.objet.domain.valueObject.game.GameStarter;

public class GameStarterView extends BorderPane {

	public GameStarterView(Stage stage) {
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");

		MenuItem easyGame = new MenuItem("Load EASY game");
		MenuItem normalGame = new MenuItem("Load NORMAL game");
		MenuItem hardGame = new MenuItem("Load HARD game");

		fileMenu.getItems().addAll(
				easyGame, normalGame, hardGame);
		menuBar.getMenus().addAll(fileMenu);
		this.setTop(menuBar);

		easyGame.setOnAction(e -> {
			startGame(stage, GameStarter.EASY, 10);
		});

		normalGame.setOnAction(e -> {
			startGame(stage, GameStarter.NORMAL, 10);
		});

		hardGame.setOnAction(e -> {
			startGame(stage, GameStarter.HARD, 10);
		});
	}

	private static void startGame(Stage stage, GameStarter gs, int gridSize) {
		App app = new App(gs, 10);
		GameView gameView = new GameView(stage, app);
		Scene gameScene = new Scene(gameView);
		stage.setTitle(gs.toString());
		stage.setScene(gameScene);
		stage.sizeToScene();
		stage.show();
	}

}
