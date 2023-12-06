package projet.approche.objet.domain.valueObject.resource;

public class ResourceAmount implements ResourceItf {
	public final int value;

	public ResourceAmount(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Resource amount cannot be negative");
		}
		this.value = amount;
	}

	public ResourceAmount add(ResourceAmount resourceAmount) {
		return new ResourceAmount(this.value + resourceAmount.value);
	}

	public ResourceAmount sub(ResourceAmount resourceAmount) {
		return new ResourceAmount(this.value - resourceAmount.value);
	}

	public ResourceAmount mul(float multiplier) {
		return new ResourceAmount((int) (this.value * multiplier));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ResourceAmount) {
			ResourceAmount resourceAmount = (ResourceAmount) obj;
			return this.value == resourceAmount.value;
		}
		return false;
	}

	public boolean isGreaterOrEqual(ResourceAmount resourceAmount) {
		return this.value >= resourceAmount.value;
	}

	public boolean isLessOrEqual(ResourceAmount resourceAmount) {
		return this.value <= resourceAmount.value;
	}

	public boolean isLess(ResourceAmount resourceAmount) {
		return this.value < resourceAmount.value;
	}

	public boolean isGreater(ResourceAmount resourceAmount) {
		return this.value > resourceAmount.value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	public String toShortString() {
		if (value < 1000) {
			return Integer.toString(value);
		} else if (value < 1000000) {
			return Integer.toString(value / 1000) + "k";
		} else if (value < 1000000000) {
			return Integer.toString(value / 1000000) + "M";
		} else {
			return Integer.toString(value / 1000000000) + "G";
		}
	}
}
