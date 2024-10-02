package model;

public class PremiumClient extends Client{

	int points;

	public PremiumClient(String name, int points) {
		super(name);
		this.points = points;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public static boolean pay(Amount totalAmount, PremiumClient clientePremium) {
		boolean Payable = false;
		if(clientePremium.getBalance().getValue() - totalAmount.getValue() > 0) {
			Payable = true;
		}
		return Payable;
	}
}
