package model;

public class Amount {
	double value;
	final String currency = "€";
	
	public Amount(double value) {
		super();
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getCurrency() {
		return currency;
	}

	@Override
	public String toString() {
		return  value + currency;
	}
	
}
