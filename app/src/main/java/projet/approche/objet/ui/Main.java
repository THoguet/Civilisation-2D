package projet.approche.objet.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import projet.approche.objet.ui.view.GameStarterView;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		GameStarterView editorView = new GameStarterView(primaryStage);

		primaryStage.setTitle("Approche Objet");
		primaryStage.setScene(new Scene(editorView));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch();
	}

}
