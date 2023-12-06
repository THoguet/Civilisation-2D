package projet.approche.objet.ui.view;

import javafx.geometry.Insets;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import projet.approche.objet.application.App;
import projet.approche.objet.ui.view.imageResource.BuildingImageResource;

public class PickerView extends HBox implements Updateable {
	private final ToggleGroup group = new ToggleGroup();

	public PickerView(App app) {
		this.setSpacing(15);
		this.setPadding(new Insets(15));

		for (String type : app.getBuildingsTypes()) {
			ToggleButton btn = new ToggleButton();
			btn.setToggleGroup(group);
			btn.setUserData(type);
			Image image = BuildingImageResource.get(type);
			ImageView imageView = new ImageView(image);
			btn.setGraphic(imageView);
			getChildren().add(btn);
		}
	}

	public String getSelected() {
		Toggle toggle = group.getSelectedToggle();
		if (toggle == null)
			return null;
		return (String) toggle.getUserData();
	}

	public void clearSelection() {
		group.selectToggle(null);
	}

	public void update() {
	}
}
