package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.dice.DicesSum;

public interface HumanInputSupplier {

	void askForInput(IPlayer player, List<DicesSum> dices);

	DicesSum getHumanInput();
	
}
