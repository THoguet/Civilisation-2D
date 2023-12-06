package projet.approche.objet.domain.valueObject.needs;

import java.util.List;

import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceList;

public class Production extends Needs {
	public Production(int time, List<Resource> resources) {
		super(time, resources);
	}

	public Production(int time) {
		super(time);
	}

	public ResourceList getProduction() {
		return this.resources;
	}

	/**
	 * Add the production to the resources.
	 *
	 * @param toAdd the resources where the production will be added
	 * @return a new ResourceList with the production added
	 */
	public ResourceList havestProduction(ResourceList toAdd) {
		return this.resources.add(toAdd);
	}

	@Override
	public String toString() {
		return "[time=" + time + ", resources=" + resources + "]";
	}
}
