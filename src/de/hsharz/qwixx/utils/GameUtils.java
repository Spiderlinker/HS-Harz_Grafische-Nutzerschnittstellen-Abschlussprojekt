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
		 * 1. Zu wenig W�rfel? 2. Zu viele W�rfel?
		 * 
		 * 3. Ist einer der W�rfel in den wei�en W�rfel vorhanden? 4. Ist der andere
		 * W�rfel in den Farbw�rfeln vorhanden? --> m�glicherweise W�rfel tauschen
		 * 
		 * 
		 */

		checkTooFewDicesSelected(selectedDices, minDices);
		checkTooManyDicesSelected(selectedDices, maxDices);

		DicesSum first = selectedDices.getFirst();
		DicesSum second = selectedDices.getSecond();

		DicesSum whiteDice = getWhiteDice(allDices);
		List<DicesSum> colorDices = allDices.stream() //
				// Dice darf nicht EMPTY oder null sein und nicht der wei�e W�rfel
				.filter(d -> !isEmptyDiceOrNull(d) && !d.equals(whiteDice)) //
				.collect(Collectors.toList());

		// Check if first is a valid dice
		if (!isEmptyDiceOrNull(first) && first.getSum() != whiteDice.getSum() && !colorDices.contains(first)) {
			throw new IllegalArgumentException("Die ausgew�hlte W�rfelsumme wurde nicht gew�rfelt. " + first);
		}

		// check if second is a valid dice
		if (!isEmptyDiceOrNull(second) && second.getSum() != whiteDice.getSum() && !colorDices.contains(second)) {
			throw new IllegalArgumentException("Die ausgew�hlte W�rfelsumme wurde nicht gew�rfelt. " + second);
		}

		if (areTwoDicesSelected(selectedDices)) {

			if (!((first.getSum() == whiteDice.getSum() && colorDices.contains(second))
					|| (second.getSum() == whiteDice.getSum() && colorDices.contains(first)))) {
				throw new IllegalArgumentException(
						"Fehlerhafte W�rfel ausgew�hlt. Bitte korrigieren: " + selectedDices);
			}
		}

	}

	private static void checkTooFewDicesSelected(Pair<DicesSum> selectedDices, int minDices) {
		if (minDices == 1 && selectedDices.isEmpty()) {
			throw new IllegalArgumentException("Keine W�rfel ausgew�hlt. Min: " + minDices);
		}
	}

	private static void checkTooManyDicesSelected(Pair<DicesSum> selectedDices, int maxDices) {
		if (maxDices == 1 //
				&& !isEmptyDiceOrNull(selectedDices.getFirst()) //
				&& !isEmptyDiceOrNull(selectedDices.getSecond())) {
			throw new IllegalArgumentException("Zu viele W�rfel ausgew�hlt. Max: " + maxDices + "; Selected: 2");
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
