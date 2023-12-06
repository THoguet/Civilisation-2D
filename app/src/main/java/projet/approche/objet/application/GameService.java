package projet.approche.objet.application;

import projet.approche.objet.domain.valueObject.game.exceptions.GameAlreadyStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.GameEnded;
import projet.approche.objet.domain.valueObject.game.exceptions.GameNotStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.GamePaused;

public interface GameService {

	public void pauseGame() throws GameNotStarted, GameEnded;

	public void startGame() throws GameEnded, GameAlreadyStarted;

	public void endGame() throws GameNotStarted, GameEnded;

	public void update() throws GameNotStarted, GameEnded, GamePaused;

	public boolean isGameStarted();

	public boolean isGameEnded();

	public String getGameState();

	// public int getGameTime();

	public int getGridSize();
}
