package model;

import java.util.ArrayList;

public interface AIGameState {
	int getValue();
	void setValue(int value);
	ArrayList<? extends AIMove> getMoves();
	AIMove getLastMove();
	int computeValue();
	void updateValueFromChild(int value);
	boolean isMaxPlayerColor();
	boolean isMinPlayerColor();
	boolean hasMaxPlayerWon();
	boolean hasMinPlayerWon();
	boolean equals(Object object);
	int hashCode();
	boolean isEndState();
}
