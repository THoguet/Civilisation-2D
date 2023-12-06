// FILEPATH: /home/nessar/Projet-Approche-Objet/app/src/test/java/projet/approche/objet/application/ApplicationTest.java

package projet.approche.objet.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projet.approche.objet.domain.valueObject.game.GameStarter;
import projet.approche.objet.domain.valueObject.resource.ResourceType;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
	private App application;
	private GameStarter gameStarter;

	@BeforeEach
	void setUp() {
		gameStarter = GameStarter.EASY;
		application = new App(gameStarter, 10);
	}

	@Test
	void testGetResourcesTypes() {
		assertNotNull(application.getResourcesTypes());
	}

	@Test
	void testGetResourceQuantity() {
		assertEquals(gameStarter.startingResources.get(ResourceType.WOOD).amount.value,
				application.getResourceQuantity("WOOD"));
	}

	@Test
	void testGetResourceProduction() {
		assertEquals(0, application.getResourceProduction("WOOD"));
	}

	@Test
	void testGetInhabitantsNumber() {
		assertEquals(gameStarter.inhabitants, application.getInhabitantsNumber());
	}

	@Test
	void testGetWorkersNumber() {
		assertEquals(gameStarter.workers, application.getWorkersNumber());
	}

	@Test
	void testGetInHabitantsInBuildings() {
		assertEquals(0, application.getInHabitantsInBuildings());
	}

	// TODO: verify exceptions
}