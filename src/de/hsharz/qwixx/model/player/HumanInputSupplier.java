package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.dice.DicesSum;

public interface HumanInputSupplier {

	void askForInput(List<DicesSum> dices);

	DicesSum getHumanInput();

}
