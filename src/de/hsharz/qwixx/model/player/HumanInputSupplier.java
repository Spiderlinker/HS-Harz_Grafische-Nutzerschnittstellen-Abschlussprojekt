package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.dice.DicePair;

public interface HumanInputSupplier {

	void askForInput(List<DicePair> dices, DiceSelectionType selectionType);

	DicePair getHumanInput();

}
