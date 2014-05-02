package model;

public class Checker {
	/// typ pionka (zwykly lub krolowa)
	private final CheckerType type;
	/// kolor pionka (bialy lub czarny)
	private final CheckerColor color;
	
	Checker(CheckerColor color, CheckerType type) {
		this.type = type;
		this.color = color;
	}
}
