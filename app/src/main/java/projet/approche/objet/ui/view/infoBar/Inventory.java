package projet.approche.objet.ui.view.infoBar;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import projet.approche.objet.application.App;
import projet.approche.objet.ui.view.imageResource.ResourceImageResource;

public class Inventory extends VBox {
	private final App app;
	private final Text gold = new Text();
	private final Text food = new Text();
	private final Text wood = new Text();
	private final Text stone = new Text();
	private final Text coal = new Text();
	private final Text iron = new Text();
	private final Text steel = new Text();
	private final Text cement = new Text();
	private final Text lumber = new Text();
	private final Text tools = new Text();

	public Inventory(App app) {
		this.app = app;
		this.setSpacing(5);
		this.setPadding(new Insets(5));

		HBox goldGroup = infoGroup(ResourceImageResource.GOLD.getImage(), this.gold);
		HBox foodGroup = infoGroup(ResourceImageResource.FOOD.getImage(), this.food);
		HBox woodGroup = infoGroup(ResourceImageResource.WOOD.getImage(), this.wood);
		HBox stoneGroup = infoGroup(ResourceImageResource.STONE.getImage(), this.stone);
		HBox coalGroup = infoGroup(ResourceImageResource.COAL.getImage(), this.coal);
		HBox ironGroup = infoGroup(ResourceImageResource.IRON.getImage(), this.iron);
		HBox steelGroup = infoGroup(ResourceImageResource.STEEL.getImage(), this.steel);
		HBox cementGroup = infoGroup(ResourceImageResource.CEMENT.getImage(), this.cement);
		HBox lumberGroup = infoGroup(ResourceImageResource.LUMBER.getImage(), this.lumber);
		HBox toolsGroup = infoGroup(ResourceImageResource.TOOLS.getImage(), this.tools);
		getChildren().addAll(goldGroup, foodGroup, woodGroup, stoneGroup, coalGroup, ironGroup, steelGroup, cementGroup,
				lumberGroup, toolsGroup);
	}

	private HBox infoGroup(Image kind, Text number) {
		HBox group = new HBox();
		ImageView img = new ImageView(kind);
		group.setSpacing(4);
		number.setCache(true);
		number.setFill(Color.BLACK);
		number.getStyleClass().add("number");
		group.getChildren().addAll(img, number);
		return group;
	}

	public void update() {
		gold.setText(Integer.toString(this.app.getResourceQuantity("GOLD")));
		food.setText(Integer.toString(this.app.getResourceQuantity("FOOD")));
		wood.setText(Integer.toString(this.app.getResourceQuantity("WOOD")));
		stone.setText(Integer.toString(this.app.getResourceQuantity("STONE")));
		coal.setText(Integer.toString(this.app.getResourceQuantity("COAL")));
		iron.setText(Integer.toString(this.app.getResourceQuantity("IRON")));
		steel.setText(Integer.toString(this.app.getResourceQuantity("STEEL")));
		cement.setText(Integer.toString(this.app.getResourceQuantity("CEMENT")));
		lumber.setText(Integer.toString(this.app.getResourceQuantity("LUMBER")));
		tools.setText(Integer.toString(this.app.getResourceQuantity("TOOLS")));
	}
}
