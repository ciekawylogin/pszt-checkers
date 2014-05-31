package model;

import java.util.ArrayList;

public interface AIGameState {
	int getValue();
	ArrayList<? extends AIMove> getMoves();
	AIMove getLastMove();
	int computeValue();
	void updateValueFromChild(int value);
}
