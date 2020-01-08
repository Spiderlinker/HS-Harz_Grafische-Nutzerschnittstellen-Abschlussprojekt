package de.hsharz.qwixx.utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.dice.pair.Pair;

public class GameUtils {

	private GameUtils() {
		// Utility class
	}

	/**
	 * @param selectedDices
	 * @param allDices
	 * @param minDices
	 * @param maxDices
	 */
	public static void checkDiceSelection(Pair<DicesSum> selectedDices, List<DicesSum> allDices, int minDices,
			int maxDices) {

		/*
		 * 1. Zu wenig Würfel? 2. Zu viele Würfel?
		 * 
		 * 3. Ist einer der Würfel in den weißen Würfel vorhanden? 4. Ist der andere
		 * Würfel in den Farbwürfeln vorhanden? --> möglicherweise Würfel tauschen
		 * 
		 * 
		 */

		checkTooFewDicesSelected(selectedDices, minDices);
		checkTooManyDicesSelected(selectedDices, maxDices);

		DicesSum first = selectedDices.getFirst();
		DicesSum second = selectedDices.getSecond();

		DicesSum whiteDice = getWhiteDice(allDices);
		List<DicesSum> colorDices = allDices.stream() //
				// Dice darf nicht EMPTY oder null sein und nicht der weiße Würfel
				.filter(d -> !isEmptyDiceOrNull(d) && !d.equals(whiteDice)) //
				.collect(Collectors.toList());

		// Check if first is a valid dice
		if (!isEmptyDiceOrNull(first) && first.getSum() != whiteDice.getSum() && !colorDices.contains(first)) {
			throw new IllegalArgumentException("Die ausgewählte Würfelsumme wurde nicht gewürfelt. " + first);
		}

		// check if second is a valid dice
		if (!isEmptyDiceOrNull(second) && second.getSum() != whiteDice.getSum() && !colorDices.contains(second)) {
			throw new IllegalArgumentException("Die ausgewählte Würfelsumme wurde nicht gewürfelt. " + second);
		}

		if (areTwoDicesSelected(selectedDices)) {

			if (!((first.getSum() == whiteDice.getSum() && colorDices.contains(second))
					|| (second.getSum() == whiteDice.getSum() && colorDices.contains(first)))) {
				throw new IllegalArgumentException(
						"Fehlerhafte Würfel ausgewählt. Bitte korrigieren: " + selectedDices);
			}
		}

	}

	private static void checkTooFewDicesSelected(Pair<DicesSum> selectedDices, int minDices) {
		if (minDices == 1 && selectedDices.isEmpty()) {
			throw new IllegalArgumentException("Keine Würfel ausgewählt. Min: " + minDices);
		}
	}

	private static void checkTooManyDicesSelected(Pair<DicesSum> selectedDices, int maxDices) {
		if (maxDices == 1 //
				&& !isEmptyDiceOrNull(selectedDices.getFirst()) //
				&& !isEmptyDiceOrNull(selectedDices.getSecond())) {
			throw new IllegalArgumentException("Zu viele Würfel ausgewählt. Max: " + maxDices + "; Selected: 2");
		}
	}

	private static DicesSum getWhiteDice(List<DicesSum> dices) {
		Optional<DicesSum> whiteDice = dices.stream(). //
				filter(d -> DiceColor.WHITE.equals(d.getColor())) //
				.findFirst();

		if (whiteDice.isPresent()) {
			return whiteDice.get();
		}
		throw new IllegalArgumentException("A white DicesSum is required!");
	}

	private static boolean areTwoDicesSelected(Pair<DicesSum> selectedDices) {
		return !isEmptyDiceOrNull(selectedDices.getFirst()) && !isEmptyDiceOrNull(selectedDices.getSecond());
	}

	private static boolean isEmptyDiceOrNull(DicesSum dice) {
		return dice == null || DicesSum.EMPTY.equals(dice);
	}

}
