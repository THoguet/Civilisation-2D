package projet.approche.objet.domain.entities.building;

import projet.approche.objet.domain.valueObject.building.BuildingType;
import projet.approche.objet.domain.valueObject.building.exceptions.BuildingAlreadyStartedException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotEnoughNeedsException;
import projet.approche.objet.domain.valueObject.needs.ConstructionNeeds;
import projet.approche.objet.domain.valueObject.needs.Needs;
import projet.approche.objet.domain.valueObject.needs.Production;
import projet.approche.objet.domain.valueObject.needs.Consumption;
import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceList;

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
		this.initConstructionNeeds = this.type.getConstructionNeeds();
		this.initProduction = this.type.getProduction();
		this.initConsumption = this.type.getConsumption();

		this.pTime = this.initConstructionNeeds.time;
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
		this.inhabitants += inhabitantsAdd;
	}

	public void addWorkerToBuilding(int workersAdd) {
		this.workers += workersAdd;
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
	
	public ConstructionNeeds getConstructionNeeds() {
		// Increase in construction time for next upgrade
		// as well as increase in gold amount and resources needed

		List<Resource> resources = new ArrayList<>();
		ResourceList copyResources = this.initConstructionNeeds.resources;
		resources = copyResources.multiplyResourceList(this.getLevel()+1).getResources();
		return new ConstructionNeeds(this.initConstructionNeeds.time * (this.type.getLevel()+1), 
			this.initConstructionNeeds.goldAmountForConstruction * (this.getLevel()+1), resources);
	}

	public Production getProduction() {
		List<Resource> resources = new ArrayList<>();
		ResourceList copyResources = initProduction.resources;
		resources = copyResources.multiplyResourceList(this.getLevel()+1).getResources();
		this.pTime = (int)(this.pTime*1.5);
		return new Production((int)(this.initConstructionNeeds.time*1.5), resources);
	}

	public Consumption getConsumption() {
		List<Resource> resources = new ArrayList<>();
		ResourceList copyResources = this.initConsumption.resources;
		resources = copyResources.multiplyResourceList(this.getLevel()).getResources();
		return new Consumption(this.initConsumption.time, resources);
	}

	public boolean canUpgrade(ResourceList inventory) {
		if (this.getLevel() < 3) {
			//
			ResourceList missingResources = this.getConstructionNeeds().getMissingResources(inventory);
			// returns the missing resources and how much is lacking for the next upgrade
			// technically an upgrade consumes resources so use getRemainingResources
			if (!missingResources.isEmpty()) {
				// add an exception : you are missing resources for the upgrade
				return false;
			}

			// increase construction time for next upgrade
			if (this.type.getConstructionNeeds().isAffordable(inventory)) {
				if (!this.type.equals(BuildingType.HOUSE) || !this.type.equals(BuildingType.APARTMENTBUILDING)) {
					// increase workers slowly
					// produce more with less workers
					this.addWorkerToBuilding(1);
					// this.type.setWorkersMax(this.type.getWorkersMax() * 2);

					// increase production
					// produce double the resources in the same amount of time
					// multiplier will always be 2
					// Production newProduction = new Production(this.type.getProduction().time,

					// this.type.setProduction(newProduction);
				}

				// increase consumption
				if (this.type.equals(BuildingType.LUMBERMILL) || this.type.equals(BuildingType.CEMENTPLANT)
						|| this.type.equals(BuildingType.STEELMILL) || this.type.equals(BuildingType.TOOLFACTORY)) {
					//
					// Consumption newConsumption = new Consumption(this.type.getConsumption().time,
					// this.type.getConsumption().multiplyResourceList(2));

					// this.type.setConsumption(newConsumption);
				} else {
					// double the number of inhabs with each upgrade
					this.addInhabitantToBuilding(this.getInhabitants());

					// this.type.setInhabitantsMax(this.type.getInhabitantsMax() * 2);
					// increase food consumption of the building
					// TODO: only consume food if there are inhabitants + workers

					// food consumption is multiplied by 2 since the number of inhabs doubled
					// Consumption newFoodConsumption = new
					// Consumption(this.type.getConsumption().time,
					// this.type.getFoodConsumption().multiplyResourceList(2));
					// this.type.setFoodConsumtion(newFoodConsumption);
				}

				// turn into a list
				// set variable for next upgrade
				List<Resource> constructionNeeds = this.type.getConstructionNeeds().resources.getResources();

				List<Resource> upgradeNeeds = constructionNeeds;

				for (int i = 0; i < this.type.getLevel(); i++) {
					upgradeNeeds.addAll(constructionNeeds);
				}

				ConstructionNeeds nextConstructionNeeds = new ConstructionNeeds(
						this.type.getConstructionNeeds().time * this.type.getLevel(),
						this.type.getConstructionNeeds().goldAmountForConstruction * this.type.getLevel(),
						upgradeNeeds);

				// this.type.setConstructionNeeds(nextConstructionNeeds);

				this.increaseLevel();
				return true;
			} else {
				// add an exception : do not have enough resources to upgrade
				return false;
			}
		}
		// add an exception : already at max level
		return false;
	}

	public String toString() {
		return this.type.name + ":" + this.id;
	}

	public String toShortString() {
		return this.type.shortName + ":" + this.id;
	}
}