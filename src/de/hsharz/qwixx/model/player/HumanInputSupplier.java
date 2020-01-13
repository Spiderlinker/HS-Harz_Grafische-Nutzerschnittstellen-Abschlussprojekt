package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.dice.DicePair;

/**
 * Diese Klasse dient der Auswahl eines Würfelpaares aus einer Liste mit
 * Würfelpaaren durch den Benutzer. Über die Methode
 * {@link #askForInput(List, DiceSelectionType)} wird dem Benutzer eine Liste
 * mit Würfelpaaren präsentiert, aus der er ein Würfelpaar auswählen soll.
 * Dieses ausgewählte Paar wird dann über {@link #getHumanInput()}
 * bereitgestellt.
 * 
 * @author Oliver Lindemann
 */
public interface HumanInputSupplier {

	/**
	 * Der Benutzer soll aus den gegebenen Würfelpaaren {@code dices} ein Würfelpaar
	 * auswählen. Dieses ausgewählte Paar kann später über die Methode
	 * {@link #getHumanInput()} geholt werden.
	 * 
	 * @param dices         Liste mit Würfelpaaren, aus den der Benutzer ein Paar
	 *                      aussuchen soll
	 * @param selectionType Typ der gegebenen Würfel
	 */
	void askForInput(List<DicePair> dices, DiceSelectionType selectionType);

	/**
	 * @return Liefert das vom Benutzer ausgewählte Würfelpaar. Falls der Benutzer
	 *         kein Würfelpaar gewählt hat oder die Methode vor dem Aufruf der
	 *         Methode {@link #askForInput(List, DiceSelectionType)} aufgerufen
	 *         wird, so wird {@code null} geliefert.
	 */
	DicePair getHumanInput();

}
