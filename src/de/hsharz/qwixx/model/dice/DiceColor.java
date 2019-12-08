package de.hsharz.qwixx.model.dice;

public enum DiceColor {

	WHITE("#FFFFFF"), RED("#B60E0F"), YELLOW("#E9A80D"), GREEN("#1F953F"), BLUE("#154E79");

	private String colorAsHex;
	
	private DiceColor(String colorAsHex){
		this.colorAsHex = colorAsHex;
	}
	
	public String getAsHex() {
		return colorAsHex;
	}
	
	
	static DiceColor getDominantColor(DiceColor c1, DiceColor c2) {
		return WHITE.equals(c1) ? c2 : c1;
	}

}
