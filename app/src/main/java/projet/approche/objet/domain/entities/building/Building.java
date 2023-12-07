package projet.approche.objet.domain.entities.building;

import projet.approche.objet.domain.valueObject.building.BuildingType;
import projet.approche.objet.domain.valueObject.building.exceptions.BuildingAlreadyStartedException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotEnoughNeedsException;
import projet.approche.objet.domain.valueObject.needs.ConstructionNeeds;
import projet.approche.objet.domain.valueObject.needs.Needs;
import projet.approche.objet.domain.valueObject.needs.Production;
import projet.approche.objet.domain.valueObject.needs.Consumption;
import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceAmount;
import projet.approche.objet.domain.valueObject.resource.ResourceList;
import projet.approche.objet.domain.valueObject.resource.ResourceType;

import java.util.List;
import java.util.ArrayList;

public class Building implements BuildingItf {

	public final long id;

	public final BuildingType type;
	private boolean buildStarted;
	private boolean isBuilt;
	private int time = 0; // time since last production / time since construction started
	private int inhabitants = 0; // current number of inhabitants in the building
	private int workers = 0; // current number of workers in the building
	private int level = 1; // current level of the building 
	private ConstructionNeeds initConstructionNeeds ;
	private Production initProduction;
	private Consumption initConsumption;

	private ConstructionNeeds curConstructionNeeds ;
	private Production curProduction;
	private Consumption curConsumption;
	private Resource foodConsumption;

	private int pTime; // increase in production time for next upgrade

	public int getInhabitants() {
		return inhabitants;
	}

	public int getWorkers() {
		return workers;
	}

	public int getLevel() {
		return level;
	}

	public Building(BuildingType buildingType, long id) {
		this.type = buildingType;
		this.id = id;
		this.initConstructionNeeds = buildingType.getConstructionNeeds();
		this.initProduction = buildingType.getProduction();
		this.initConsumption = buildingType.getConsumption();

		this.pTime = this.initProduction.time;
	}

	public boolean isBuildStarted() {
		return buildStarted;
	}

	public boolean isBuilt() {
		return isBuilt;
	}

	/**
	 * Updates the building. If the building is built, it will produce and consume
	 * resources if possible. If the building is not built but the construction
	 * started, it will continue the construction. If the building is not built and
	 * the construction did not start, nothing is done.
	 * 
	 * @param inventory the resources available to produce and consume
	 * @return the remaining resources after the production and consumption
	 */
	public ResourceList update(ResourceList inventory) {
		if (isBuilt) { // if the building is built verify if it can produce
			if (this.workers >= this.type.getWorkersNeeded() && this.inhabitants >= this.type.getInhabitantsNeeded()) {
				time++;
				if (time >= this.type.getProduction().time && time >= this.type.getConsumption().time) { // even if it
																											// should be
					// the same value
					if (this.type.getConsumption().isAffordable(inventory)) { // verify if the building have enough
																				// resources
						inventory = this.type.getConsumption().getRemainingResources(inventory); // consume resources
																									// from
						// inventory
						inventory = this.type.getProduction().havestProduction(inventory); // produce resources in
																							// inventory
						// TODO : correct havestProduction to harvestProduction everywhere
						this.time = 0; // reset the time since last production
						return inventory;
					}
				}
			}
		} else if (buildStarted) { // if the building is not built but the construction started
			time++;
			if (time >= type.getConstructionNeeds().time) {
				isBuilt = true;
			}
		}
		// else the building is not built and the construction did not start so nothing
		// is done
		return inventory;
	}

	/**
	 * Starts the construction of the building.
	 * 
	 * @param resources the resources available to start the construction
	 * @return the remaining resources after the construction started
	 * @throws NotEnoughNeedsException         if the resources are not enough to
	 *                                         start the
	 *                                         construction
	 * @throws BuildingAlreadyStartedException if the building is already started
	 */
	public ResourceList startBuild(ResourceList resources)
			throws NotEnoughNeedsException, BuildingAlreadyStartedException {
		if (this.buildStarted)
			throw new BuildingAlreadyStartedException("building of " + this + " already started");
		if (this.type.getConstructionNeeds().isAffordable(resources)) {
			this.buildStarted = true;
			return this.type.getConstructionNeeds().getRemainingResources(resources);
		}
		throw new NotEnoughNeedsException("to build " + this);
	}

	public void addInhabitantToBuilding(int inhabitantsAdd) {
		// check if the building is not Lumbermill, Cementplant,Steelmill, Tool factory
		// otherwise throw an exception
		this.inhabitants += inhabitantsAdd;
		// every time an inhad is added the food consumption increases
		this.foodConsumption = new Resource(ResourceType.FOOD, new ResourceAmount(this.inhabitants + this.workers));
	}

	public void addWorkerToBuilding(int workersAdd) {
		// check if the building is not a House or Apartment building
		// otherwise throw an exception
		this.workers += workersAdd;
		// every time a worker is added the food consumption increases	
		this.foodConsumption = new Resource(ResourceType.FOOD, new ResourceAmount(this.inhabitants + this.workers));
	}

	public Resource getFoodConsumption(){
		return this.foodConsumption;
	}

	public boolean hasInhabitantorWorkers() {
		return this.inhabitants + this.workers > 0;
	}

	public void increaseLevel() {
		this.level++;
	}

	public Needs getCostToBuild() {
		return this.type.getConstructionNeeds();
	}

	public int getTimeToBuild() {
		return this.type.getConstructionNeeds().time;
	}

	public ConstructionNeeds getInitConstructionNeeds() {
		return this.initConstructionNeeds;
	}

	public Production getIniProduction() {
		return this.initProduction;
	}

	public Consumption getInitConsumption() {
		return this.initConsumption;
	}
	
	public ConstructionNeeds getConstructionNeeds() {
		// Increase in construction time for next upgrade
		// as well as increase in gold amount and resources needed

		List<Resource> resources = new ArrayList<>();
		ResourceList copyResources = this.initConstructionNeeds.resources;
		resources = copyResources.multiplyResourceList(this.getLevel()+1).getResources();
		// put 0 for the goldAmount because it will be multiplied in the above line
		return new ConstructionNeeds(this.initConstructionNeeds.time * (this.getLevel()+1), 
			0, resources);
	}

	public Production getProduction() {
		List<Resource> resources = new ArrayList<>();
		ResourceList copyResources = initProduction.resources;
		resources = copyResources.multiplyResourceList(this.getLevel()+1).getResources();
		this.pTime = (int)(this.pTime*1.5);
		return new Production(this.pTime, resources);
	}

	public Consumption getConsumption() {
		List<Resource> resources = new ArrayList<>();
		ResourceList copyResources = this.initConsumption.resources;
		resources = copyResources.multiplyResourceList(this.getLevel()+1).getResources();
		return new Consumption(this.initConsumption.time, resources);
	}

	public ResourceList canUpgrade(ResourceList inventory) {
		if (this.getLevel() < 3) {
			//
			ResourceList missingResources = this.getConstructionNeeds().getMissingResources(inventory);
			// returns the missing resources and how much is lacking for the next upgrade
			// technically an upgrade consumes resources so use getRemainingResources
			if (!missingResources.isEmpty()) {
				// add an exception : you are missing resources for the upgrade
				return inventory;
			}

			// increase construction time for next upgrade
			// if a building is being upgraded we assume that the new inhabs + workers 
			// are added as well
			if (this.getConstructionNeeds().isAffordable(inventory)) {
				if (!this.type.equals(BuildingType.HOUSE) || !this.type.equals(BuildingType.APARTMENTBUILDING)) {
					// increase workers slowly
					// produce more with less workers
					// add a percentage of workers at each upgrade = 50% increase
					this.addWorkerToBuilding((int)(this.workers * 0.5));
					this.curProduction = getProduction();
				}

				// increase consumption
				if (this.type.equals(BuildingType.LUMBERMILL) || this.type.equals(BuildingType.CEMENTPLANT)
						|| this.type.equals(BuildingType.STEELMILL) || this.type.equals(BuildingType.TOOLFACTORY)) {
					//
					this.curConsumption = getConsumption();
				} else {
					// double the number of inhabs with each upgrade
					this.addInhabitantToBuilding(this.getInhabitants());
				}

				inventory = this.getConstructionNeeds().getRemainingResources(inventory);

				this.increaseLevel();
				return inventory;
			} else {
				// add an exception : do not have enough resources to upgrade
				return inventory;
			}
		}
		// add an exception : already at max level
		return inventory;
	}

	public String toString() {
		return this.type.name + ":" + this.id;
	}

	public String toShortString() {
		return this.type.shortName + ":" + this.id;
	}
}