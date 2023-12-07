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

public class Building implements BuildingItf {

	public final long id;

	public final BuildingType type;
	private boolean buildStarted;
	private boolean isBuilt;
	private int time = 0; // time since last production / time since construction started
	private int inhabitants = 0; // current number of inhabitants in the building
	private int workers = 0; // current number of workers in the building
	private int level = 1; // current level of the building | levels are 1, 2, 3

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
		ResourceList returnList = new ResourceList();
		if (isBuilt) { // if the building is built verify if it can produce
			if (this.workers >= this.type.getWorkersNeeded() && this.inhabitants >= this.type.getInhabitantsNeeded()) {
				time++;
				if (time >= this.type.getProduction().time && time >= this.type.getConsumption().time) { // even if it
																											// should be
					// the same value
					if (this.type.getConsumption().isAffordable(inventory)) { // verify if the building have enough
																				// resources
						returnList = this.type.getConsumption().getRemainingResources(inventory); // consume resources
																									// from
						// inventory
						returnList = this.type.getProduction().harvestProduction(returnList); // produce resources in
																								// inventory
						this.time = 0; // reset the time since last production
						return returnList;
					}
				}
			}
		} else if (buildStarted) { // if the building is not built but the construction started
			time++;
			if (time >= type.getConstructionNeeds().time) {
				isBuilt = true;
				time = 0;
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

	public Needs getCostToBuild() {
		return this.type.getConstructionNeeds();
	}

	public int getTimeToBuild() {
		return this.type.getConstructionNeeds().time;
	}

	public boolean canUpgrade(ResourceList inventory) {
		if (this.level < 3) {
			//
			ResourceList missingResources = this.type.getConstructionNeeds().getMissingResources(inventory);
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
					// this.type.getProduction().multiplyResourceList(2));

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

				for (int i = 0; i < this.level; i++) {
					upgradeNeeds.addAll(constructionNeeds);
				}

				ConstructionNeeds nextConstructionNeeds = new ConstructionNeeds(
						this.type.getConstructionNeeds().time * this.level,
						this.type.getConstructionNeeds().goldAmountForConstruction * this.level,
						upgradeNeeds);

				// this.type.setConstructionNeeds(nextConstructionNeeds);

				this.level++;
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
		if (isBuilt) {
			return type.name + " (level " + level + ") :\n\nNumber of inhabitants : " + inhabitants
					+ "\nNumber of workers : " + workers + "\n"
					+ type.getConsumption()
					+ type.getProduction();
		} else if (buildStarted) {
			return type.name + ":\nUnder construction";
		}
		return "";
	}

	public String toShortString() {
		return type.shortName + ":" + id;
	}
}
