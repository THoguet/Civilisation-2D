package projet.approche.objet.domain.valueObject.building;

import projet.approche.objet.domain.valueObject.needs.ConstructionNeeds;
import projet.approche.objet.domain.valueObject.needs.Consumption;
import projet.approche.objet.domain.valueObject.needs.Production;
import projet.approche.objet.domain.valueObject.resource.Resource;
import static projet.approche.objet.domain.valueObject.resource.ResourceType.*;

import java.util.List;

public enum BuildingType {
	WOODENCABIN("Wooden Cabin",
			"WC",
			1,
			new Consumption(1, List.of(new Resource(FOOD, 2))),
			new ConstructionNeeds(2, 1, List.of(new Resource(WOOD, 1))),
			2,
			2,
			new Consumption(1),
			new Production(1, List.of(
					new Resource(WOOD, 2),
					new Resource(FOOD, 2)))),
	HOUSE("House",
			"H",
			1,
			new Consumption(1, List.of(new Resource(FOOD, 4))),
			new ConstructionNeeds(4, 1,
					List.of(
							new Resource(WOOD, 2),
							new Resource(STONE, 2))),
			0,
			4,
			new Consumption(1),
			new Production(1)),
	APARTMENTBUILDING("Apartment Building",
			"A",
			1,
			new Consumption(1, List.of(new Resource(FOOD, 60))),
			new ConstructionNeeds(6,
					4, List.of(
							new Resource(WOOD, 50),
							new Resource(STONE, 50))),
			0,
			60,
			new Consumption(1),
			new Production(1)),
	FARM("Farm",
			"F",
			1,
			new Consumption(1, List.of(new Resource(FOOD, 5))),
			new ConstructionNeeds(2,
					4, List.of(
							new Resource(WOOD, 5),
							new Resource(STONE, 5))),
			3,
			5,
			new Consumption(1),
			new Production(1, List.of(
					new Resource(FOOD, 10)))),
	QUARRY("Quarry",
			"Q",
			1,
			new Consumption(1, List.of(new Resource(FOOD, 2))),
			new ConstructionNeeds(2,
					4, List.of(
							new Resource(WOOD, 50))),
			30,
			2,
			new Consumption(1),
			new Production(1, List.of(
					new Resource(STONE, 4),
					new Resource(IRON, 1),
					new Resource(COAL, 4),
					new Resource(GOLD, 2)))),
	LUMBERMILL("Lumber Mill",
			"L",
			1,
			new Consumption(1),
			new ConstructionNeeds(4,
					6, List.of(
							new Resource(WOOD, 50),
							new Resource(STONE, 50))),
			10,
			0,
			new Consumption(1, List.of(
					new Resource(WOOD, 4))),
			new Production(1, List.of(
					new Resource(LUMBER, 4)))),
	CEMENTPLANT("Cement Plant",
			"C",
			1,
			new Consumption(1),
			new ConstructionNeeds(4,
					6, List.of(
							new Resource(WOOD, 50),
							new Resource(STONE, 50))),
			10,
			0,
			new Consumption(1, List.of(
					new Resource(STONE, 4),
					new Resource(COAL, 4))),
			new Production(1, List.of(
					new Resource(CEMENT, 4)))),
	STEELMILL("Steel Mill",
			"S",
			1,
			new Consumption(1),
			new ConstructionNeeds(6,
					6, List.of(
							new Resource(WOOD, 100),
							new Resource(STONE, 50))),
			40,
			0,
			new Consumption(1, List.of(
					new Resource(IRON, 4),
					new Resource(COAL, 2))),
			new Production(1, List.of(
					new Resource(STEEL, 4)))),
	TOOLFACTORY("Tool Factory",
			"T", 
			1,
			new Consumption(1),
			new ConstructionNeeds(8,
					8, List.of(
							new Resource(WOOD, 50),
							new Resource(STONE, 50))),
			12,
			0,
			new Consumption(1, List.of(
					new Resource(STEEL, 4),
					new Resource(COAL, 4))),
			new Production(1, List.of(
					new Resource(TOOLS, 4))));

	public final String name;
	public final String shortName;
	private int level = 1; // current level of the building | levels are 1, 2, 3
	private final Consumption foodConsumption; // food consumption of the building
	private final ConstructionNeeds constructionNeeds; // needs for construction
	private final int workersNeeded; // min number of workers
	private final int workersMax; // max number of workers
	private final int inhabitantsNeeded; // min number of inhabitants
	private final int inhabitantsMax; // max number of inhabitants
	private final Consumption consumption;
	private final Production production;

	private BuildingType(String name, String shortName, int level,Consumption foodConsumption,
			ConstructionNeeds constructionNeeds, int workersNeeded,
			int inhabitantsNeeded, Consumption consumption, Production production) {
		this.name = name;
		this.shortName = shortName;
		this.level = level;
		this.foodConsumption = foodConsumption;
		this.constructionNeeds = constructionNeeds;
		this.workersNeeded = workersNeeded;
		this.workersMax = 2 * workersNeeded;
		this.inhabitantsNeeded = inhabitantsNeeded;
		this.inhabitantsMax = 2 * inhabitantsNeeded;
		this.consumption = consumption;
		this.production = production;
	}

	public ConstructionNeeds getConstructionNeeds() {
		return constructionNeeds;
	}

	public int getWorkersNeeded() {
		return workersNeeded;
	}

	public int getWorkersMax() {
		return workersMax;
	}

	public int getInhabitantsNeeded() {
		return inhabitantsNeeded;
	}

	public int getInhabitantsMax() {
		return inhabitantsMax;
	}

	public int getLevel() {
		return level;
	}

	public Consumption getFoodConsumption() {
		return foodConsumption;
	}

	public Consumption getConsumption() {
		return consumption;
	}

	public Production getProduction() {
		return production;
	}

	public static BuildingType fromString(String name) {
		for (BuildingType type : BuildingType.values()) {
			if (type.name.equals(name)) {
				return type;
			}
		}
		return null;
	}

	public String getStats() {
		return "[name=" + name + ", shortName=" + shortName + ", constructionNeeds=" + constructionNeeds
				+ ", workersNeeded=" + workersNeeded + ", workersMax=" + workersMax + ", inhabitantsNeeded="
				+ inhabitantsNeeded + ", inhabitantsMax=" + inhabitantsMax + ", consumption=" + consumption.resources
				+ ", production=" + production.resources + ", timeForProduction=" + production.time + "]";
	}
}