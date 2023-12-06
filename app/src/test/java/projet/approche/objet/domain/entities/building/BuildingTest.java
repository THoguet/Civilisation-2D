// FILEPATH: /home/nessar/Projet-Approche-Objet/app/src/test/java/projet/approche/objet/domain/entities/building/BuildingTest.java

package projet.approche.objet.domain.entities.building;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import projet.approche.objet.domain.valueObject.building.BuildingType;
import projet.approche.objet.domain.valueObject.building.exceptions.BuildingAlreadyStartedException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotEnoughNeedsException;
import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceList;
import projet.approche.objet.domain.valueObject.resource.ResourceType;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class BuildingTest {
	@Test
	void testConstructor() {
		BuildingType type = BuildingType.fromString("House");
		Building building = new Building(type, 1);

		assertEquals(type, building.type);
		assertFalse(building.isBuildStarted());
		assertFalse(building.isBuilt());
	}

	@Test
	void testStartBuild() {
		BuildingType type = BuildingType.fromString("House");
		Building building = new Building(type, 1);
		ResourceList resources = new ResourceList(List.of(new Resource(ResourceType.fromString("Wood"), 100)));
		assertThrows(NotEnoughNeedsException.class, () -> building.startBuild(resources));
		ResourceList resources2 = new ResourceList(List.of(
				new Resource(ResourceType.fromString("Wood"), 100),
				new Resource(ResourceType.fromString("Stone"), 100)));
		assertThrows(NotEnoughNeedsException.class, () -> building.startBuild(resources2));
		ResourceList resources3 = new ResourceList(List.of(
				new Resource(ResourceType.fromString("Wood"), 100),
				new Resource(ResourceType.fromString("Stone"), 100),
				new Resource(ResourceType.fromString("Gold"), 100)));
		assertDoesNotThrow(() -> building.startBuild(resources3));
		assertTrue(building.isBuildStarted());
	}

	@Test
	void testStartBuildAlreadyStarted() {
		BuildingType type = BuildingType.fromString("House");
		Building building = new Building(type, 1);

		ResourceList resources = new ResourceList(List.of(
				new Resource(ResourceType.fromString("Wood"), 100),
				new Resource(ResourceType.fromString("Stone"), 100),
				new Resource(ResourceType.fromString("Gold"), 100)));

		assertDoesNotThrow(() -> building.startBuild(resources));
		assertThrows(BuildingAlreadyStartedException.class, () -> building.startBuild(resources));
	}

	@Test
	void testUpdateBuilding() {
		BuildingType type = BuildingType.fromString("House");
		Building building = new Building(type, 1);

		ResourceList resources = new ResourceList(List.of(
				new Resource(ResourceType.fromString("Wood"), 100),
				new Resource(ResourceType.fromString("Stone"), 100),
				new Resource(ResourceType.fromString("Gold"), 100)));

		assertDoesNotThrow(() -> building.startBuild(resources)); // start building
		for (int i = 0; i < type.getConstructionNeeds().time; i++) {
			assertDoesNotThrow(() -> building.update(resources));
		}
		assertTrue(building.isBuilt()); // building is built
	}

	@Test
	void testUpdateBuildingProduction() {
		BuildingType type = BuildingType.fromString("Lumber Mill");
		Building building = new Building(type, 1);

		ResourceList resourcesForBuild = new ResourceList(List.of(
				new Resource(ResourceType.fromString("Wood"), 100),
				new Resource(ResourceType.fromString("Stone"), 100),
				new Resource(ResourceType.fromString("Gold"), 100)));

		assertDoesNotThrow(() -> building.startBuild(resourcesForBuild));
		for (int i = 0; i < type.getConstructionNeeds().time; i++) {
			assertDoesNotThrow(() -> building.update(resourcesForBuild));
		}
		assertTrue(building.isBuilt()); // building is built

		ResourceList resources2 = new ResourceList(List.of(new Resource(ResourceType.fromString("Wood"), 5)));
		ResourceList resources3 = building.update(resources2);
		assertEquals(resources2, resources3); // no production as there is no worker / inhabitant

		building.addInhabitantToBuilding(100);
		resources3 = building.update(resources2);
		assertEquals(resources2, resources3); // no production as there is no worker

		building.addWorkerToBuilding(100);
		resources3 = building.update(resources2);
		ResourceList shouldBe = new ResourceList(List.of(
				new Resource(ResourceType.fromString("Wood"), 1),
				new Resource(ResourceType.fromString("Lumber"), 4)));
		assertNotEquals(resources2, resources3); // production
		assertTrue(resources3.contains(shouldBe)); // production and verification of the remaining resources

		ResourceList resources4 = new ResourceList(List.of(new Resource(ResourceType.fromString("Wood"), 3)));
		ResourceList resources5 = building.update(resources4);
		assertEquals(resources4, resources5); // no production as there is not enough resources
	}

	@Test
	void testAddInhabitantToBuilding() {
		BuildingType type = BuildingType.fromString("House");
		Building building = new Building(type, 1);

		building.addInhabitantToBuilding(5);
		assertEquals(5, building.getInhabitants());
	}

	@Test
	void testRemoveInhabitantFromBuilding() {
		BuildingType type = BuildingType.fromString("House");
		Building building = new Building(type, 1);

		building.addInhabitantToBuilding(5);
		building.addInhabitantToBuilding(-2);
		assertEquals(3, building.getInhabitants());
	}

	@Test
	void testAddWorkerToBuilding() {
		BuildingType type = BuildingType.fromString("House");
		Building building = new Building(type, 1);

		building.addWorkerToBuilding(5);
		assertEquals(5, building.getWorkers());
	}

	@Test
	void testRemoveWorkerFromBuilding() {
		BuildingType type = BuildingType.fromString("House");
		Building building = new Building(type, 1);

		building.addWorkerToBuilding(5);
		building.addWorkerToBuilding(-2);
		assertEquals(3, building.getWorkers());
	}

	@Disabled
	@Test
	void testCanUpgrade() {
		// fails for now
		BuildingType type = BuildingType.fromString("Wooden Cabin");
		Building building = new Building(type, 1);

		ResourceList inventory = new ResourceList(List.of(
				new Resource(ResourceType.fromString("Wood"), 50),
				new Resource(ResourceType.fromString("Stone"), 50),
				new Resource(ResourceType.fromString("Gold"), 50)));

		building.canUpgrade(inventory);
		assertTrue(building.canUpgrade(inventory) && building.getLevel() == 2
				&& building.getInhabitants() == 4);
	}

	@Test
	void testToString() {
		BuildingType type = BuildingType.fromString("House");
		Building building = new Building(type, 1);

		assertEquals(type.name + ":" + building.id, building.toString());
	}

	@Test
	void testToShortString() {
		BuildingType type = BuildingType.fromString("House");
		Building building = new Building(type, 1);

		assertEquals(type.shortName + ":" + building.id, building.toShortString());
	}
}