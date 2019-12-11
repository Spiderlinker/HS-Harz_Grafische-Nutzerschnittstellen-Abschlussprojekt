package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.dice.pair.Pair;

public interface HumanInputSupplier {

	void askForInput(IPlayer player, List<DicesSum> dices, int minDices, int maxDices);

	Pair<DicesSum> getHumanInput();
	
}
