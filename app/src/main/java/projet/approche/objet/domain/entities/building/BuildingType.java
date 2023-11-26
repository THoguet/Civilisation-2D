package projet.approche.objet.domain.entities.building;

import projet.approche.objet.domain.valueObject.ConstructionNeeds;
import projet.approche.objet.domain.valueObject.Needs;
import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceType;

public enum BuildingType {

	WOODENCABIN("Wooden Cabin",
			"WC",
			new ConstructionNeeds(
					new Needs(1,
							new Resource(ResourceType.GOLD, 1))),
			2,
			2,
			new Needs(1), // needs for production
			new Needs(1, // production
					new Resource(ResourceType.WOOD, 2),
					new Resource(ResourceType.FOOD, 2))),
	FARM("Farm",
			"F",
			new ConstructionNeeds(
					new Needs(1,
							new Resource(ResourceType.GOLD, 1))),
			2,
			2,
			new Needs(1), // needs for production
			new Needs(1, // production
					new Resource(ResourceType.WOOD, 2),
					new Resource(ResourceType.FOOD, 2))),
	QUARRY("Quarry",
			"Q",
			new ConstructionNeeds(
					new Needs(1,
							new Resource(ResourceType.GOLD, 1))),
			2,
			2,
			new Needs(1), // needs for production
			new Needs(1, // production
					new Resource(ResourceType.WOOD, 2),
					new Resource(ResourceType.FOOD, 2))),
	HOUSE("House",
			"H",
			new ConstructionNeeds(
					new Needs(1,
							new Resource(ResourceType.GOLD, 1))),
			2,
			2,
			new Needs(1), // needs for production
			new Needs(1, // production
					new Resource(ResourceType.WOOD, 2),
					new Resource(ResourceType.FOOD, 2))),
	APPARTMENTBUILDING("Appartment Building",
			"AB",
			new ConstructionNeeds(
					new Needs(1,
							new Resource(ResourceType.GOLD, 1))),
			2,
			2,
			new Needs(1), // needs for production
			new Needs(1, // production
					new Resource(ResourceType.WOOD, 2),
					new Resource(ResourceType.FOOD, 2)));

	public final String name;
	public final String shortName;
	public final ConstructionNeeds constructionNeeds;
	public final int workersNeeded; // max number of workers
	//public final int workers; // current number of workers
	public final int inhabitantsNeeded; // max number of inhabitants
	//public final int inhabitants; // current number of inhabitants
	public final Needs productionNeeds;
	public final Needs production;

	private BuildingType(String name, String shotName, ConstructionNeeds constructionNeeds, /*int workersN,*/ int workers,
			/*int inhabitantsN,*/ int inhabitants, Needs productionNeeds, Needs production) {
		this.name = name;
		this.shortName = shotName;
		this.constructionNeeds = constructionNeeds;
		this.workersNeeded = workers;
		//this.workers = workers;
		this.inhabitantsNeeded = inhabitants;
		//this.inhabitants = inhabitants;
		this.productionNeeds = productionNeeds;
		this.production = production;
	}
}
