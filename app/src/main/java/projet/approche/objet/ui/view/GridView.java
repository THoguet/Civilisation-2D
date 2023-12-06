package projet.approche.objet.ui.view;

import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.BorderPane;
import projet.approche.objet.application.App;
import projet.approche.objet.domain.valueObject.building.exceptions.NotEnoughNeedsException;
import projet.approche.objet.domain.valueObject.game.exceptions.GameEnded;
import projet.approche.objet.domain.valueObject.game.exceptions.GameNotStarted;
import projet.approche.objet.domain.valueObject.grid.exceptions.NoBuildingHereException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotFreeException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotInGridException;
import projet.approche.objet.ui.view.imageResource.BuildingImageResource;

public class GridView extends BorderPane {
	private final PickerView pickerView;
	private final int gridSize;
	private final App app;
	private final List<Updateable> updateables;

	public GridView(App app, PickerView pickerView, List<Updateable> updateables) {
		this.pickerView = pickerView;
		this.app = app;
		this.gridSize = app.getGridSize();
		this.updateables = updateables;
		setPrefSize(gridSize * BuildingImageResource.size,
				gridSize * BuildingImageResource.size);
		for (int i = 0; i < this.gridSize; i++) {
			for (int j = 0; j < this.gridSize; j++) {
				createTile(i, j);
			}
		}
	}

	private void createTile(int i, int j) {
		int layoutX = i * BuildingImageResource.size;
		int layoutY = j * BuildingImageResource.size;
		String kind;
		try {
			kind = app.getBuildingType(i, j);
		} catch (NoBuildingHereException e) {
			kind = "Empty";
		} catch (NotInGridException e) {
			throw new RuntimeException(e); // should not happen
		}
		Tile tile = new Tile(BuildingImageResource.get(kind), layoutX, layoutY);
		getChildren().add(tile);
		tile.setOnMouseClicked(e -> {
			update(tile, i, j);
			updateables.forEach(updateable -> updateable.update());
		});
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(-0.2);
		tile.setOnMouseEntered(e -> {
			tile.setEffect(colorAdjust);
		});
		tile.setOnMouseExited(e -> {
			tile.setEffect(null);
		});
	}

	private void update(Tile tile, int i, int j) {
		String kind;
		try {
			kind = app.getBuildingType(i, j);
		} catch (NoBuildingHereException e) {
			kind = null;
		} catch (NotInGridException e) {
			throw new RuntimeException(e); // should not happen
		}
		if (pickerView.getSelected() != null && pickerView.getSelected() != kind) {
			getChildren().remove(tile);
			try {
				app.buildBuilding(pickerView.getSelected(), i, j);
			} catch (GameNotStarted e) {
				// Open a window saying that the game is not started
				this.popUp("Game not started", "Game not started...",
						"You must start a new game before building.");
			} catch (GameEnded e) {
				// Open a window saying that the game is ended
				this.popUp("Game ended", "Game ended...",
						"You must start a new game before building.");
			} catch (NotInGridException e) {
				throw new RuntimeException(e); // should not happen
			} catch (NotFreeException e) {
				// Open a window saying that the tile is not free
				this.popUp("Tile not free", "Tile not free...",
						"You must build on a free tile.");
			} catch (NotEnoughNeedsException e) {
				// Open a window saying that the tile is not free
				this.popUp("Not enough needs", "Not enough needs...",
						"You must have enough needs to build this building.");
			}
			createTile(i, j);
		}
	}

	public int getSize() {
		return this.getSize();
	}

	private void popUp(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait().ifPresent(rs -> {
		});
	}
}