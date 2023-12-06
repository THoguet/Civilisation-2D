package projet.approche.objet.ui.view.infoBar;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import projet.approche.objet.application.App;
import projet.approche.objet.domain.valueObject.game.exceptions.GameAlreadyStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.GameEnded;
import projet.approche.objet.domain.valueObject.game.exceptions.GameNotStarted;
import projet.approche.objet.ui.view.imageResource.ButtonImageResource;

public class Button extends VBox {

	private final App app;
	private HBox button;
	private static final ImageView play = new ImageView(ButtonImageResource.PLAY.getImage());
	private static final ImageView pause = new ImageView(ButtonImageResource.PAUSE.getImage());

	public Button(App app) {
		this.app = app;
		this.button = new HBox();
		button.setSpacing(4);
		button.setCache(true);
		button.getChildren().addAll(play);
		button.setOnMouseClicked(e -> {
			switch (app.getGameState()) {
				case "NOTSTARTED":
					try {
						app.startGame();
					} catch (GameAlreadyStarted | GameEnded e1) {
						this.update();
						e1.printStackTrace();
					}
					break;
				case "PAUSED":
				case "RUNNING":
					try {
						app.pauseGame();
					} catch (GameNotStarted | GameEnded e1) {
						this.update();
						e1.printStackTrace();
					}
					break;
				case "ENDED":
					// TODO: restart game
					break;
				default:
					break;
			}
			this.update();
		});
		getChildren().addAll(button);
	}

	public void update() {
		switch (app.getGameState()) {
			case "NOTSTARTED":
			case "PAUSED":
				button.getChildren().clear();
				button.getChildren().addAll(play);
				break;
			case "RUNNING":
				button.getChildren().clear();
				button.getChildren().addAll(pause);
				break;
			case "ENDED":
				// TODO: restart game
				break;
			default:
				break;
		}
	}

}
