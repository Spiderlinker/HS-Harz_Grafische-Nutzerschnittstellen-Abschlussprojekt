package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.dice.DicePair;

/**
 * Diese Klasse dient der Auswahl eines W�rfelpaares aus einer Liste mit
 * W�rfelpaaren durch den Benutzer. �ber die Methode
 * {@link #askForInput(List, DiceSelectionType)} wird dem Benutzer eine Liste
 * mit W�rfelpaaren pr�sentiert, aus der er ein W�rfelpaar ausw�hlen soll.
 * Dieses ausgew�hlte Paar wird dann �ber {@link #getHumanInput()}
 * bereitgestellt.
 * 
 * @author Oliver Lindemann
 */
public interface HumanInputSupplier {

	/**
	 * Der Benutzer soll aus den gegebenen W�rfelpaaren {@code dices} ein W�rfelpaar
	 * ausw�hlen. Dieses ausgew�hlte Paar kann sp�ter �ber die Methode
	 * {@link #getHumanInput()} geholt werden.
	 * 
	 * @param dices         Liste mit W�rfelpaaren, aus den der Benutzer ein Paar
	 *                      aussuchen soll
	 * @param selectionType Typ der gegebenen W�rfel
	 */
	void askForInput(List<DicePair> dices, DiceSelectionType selectionType);

	/**
	 * @return Liefert das vom Benutzer ausgew�hlte W�rfelpaar. Falls der Benutzer
	 *         kein W�rfelpaar gew�hlt hat oder die Methode vor dem Aufruf der
	 *         Methode {@link #askForInput(List, DiceSelectionType)} aufgerufen
	 *         wird, so wird {@code null} geliefert.
	 */
	DicePair getHumanInput();

}
