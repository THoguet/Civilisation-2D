package projet.approche.objet.domain.valueObject.resource;

public enum ResourceType implements ResourceItf {
	GOLD("Gold", "G"),
	FOOD("Food", "F"),
	WOOD("Wood", "W"),
	STONE("Stone", "S"),
	COAL("Coal", "C"),
	IRON("Iron", "I"),
	STEEL("Steel", "St"),
	CEMENT("Cement", "Ce"),
	LUMBER("Lumber", "L"),
	TOOLS("Tools", "T");

	public final String name;
	public final String shortName;

	private ResourceType(String name, String shortName) {
		this.name = name;
		this.shortName = shortName;
	}

	@Override
	public String toString() {
		return name;
	}

	public String toShortString() {
		return shortName;
	}

	public static ResourceType fromString(String name) {
		for (ResourceType resourceType : ResourceType.values()) {
			if (resourceType.name.equals(name)) {
				return resourceType;
			}
		}
		throw new IllegalArgumentException("No resource type with name " + name);
	}
}
