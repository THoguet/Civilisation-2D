package projet.approche.objet.domain.valueObject.needs;

import java.util.List;

import projet.approche.objet.domain.valueObject.resource.Resource;

public class Consumption extends Needs {
	public Consumption(int time, List<Resource> resources) {
		super(time, resources);
	}

	public Consumption(int time) {
		super(time);
	}

	@Override
	public String toString() {
		return "[time=" + time + ", resources=" + resources + "]";
	}
}
