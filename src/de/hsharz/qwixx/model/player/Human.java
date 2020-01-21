package de.hsharz.qwixx.model.player;

import java.util.List;
import java.util.Objects;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicePair;

/**
 * Diese Klasse repr�sentiert einen menschlichen Spieler. <br>
 * Sobald dieser Spieler aufgefordert wird ein W�rfelpaar auszuw�hlen, wird
 * diese Entscheidung an den Benutzer weitergegeben. <br>
 * Die Eingabe des Benutzers erfolgt �ber das Interface
 * {@link HumanInputSupplier}, welches vor der Verwendung des Human-Players �ber
 * die Methode {@link #setHumanInputSupplier(HumanInputSupplier)} gesetzt werden
 * muss. Andernfalls kann es zu Fehlern f�hren.
 * <p>
 * Die Auswahl eines W�rfelpaares erfolgt folgenderma�en: <br>
 * Game -&gt; fragt diese Instanz, welches W�rfelpaar ausgew�hlt werden soll <br>
 * Human-Instanz -> gibt Frage an gesetzten {@link HumanInputSupplier} weiter
 * und ruft {@link #wait()} auf. <br>
 * {@link HumanInputSupplier} holt Entscheidung des Benutzers ein und ruft
 * {@link #notify()} auf. <br>
 * Human-Instanz fragt Eingabe des Nutzers vom {@link HumanInputSupplier} �ber
 * {@link HumanInputSupplier#getHumanInput()}
 * 
 * @author Oliver Lindemann
 */
public class Human extends Player {

	/** {@link HumanInputSupplier} f�r das Einholen der Entscheidung des Spielers */
	private HumanInputSupplier inputSupplier;

	/**
	 * Erzeugt einen neuen menschlichen Spieler mit dem gegebenen Namen {@code name}
	 * und dem Spielfeld {@code board}. <br>
	 * Der Name darf nicht null und nicht leer sein. <br>
	 * Das Spielfeld darf nicht null sein. <br>
	 * In beiden F�llen wird bei Versto� eine {@link NullPointerException} geworfen.
	 * <p>
	 * Bevor der Spieler in einem Spiel mitspielen kann, muss noch der
	 * {@link HumanInputSupplier} �ber die Methode
	 * {@link #setHumanInputSupplier(HumanInputSupplier)} gesetzt werden, damit
	 * Entscheidungen an den Benutzer weitergereicht werden k�nnen. Andernfalls kann
	 * es zu Fehlern f�hren.
	 * 
	 * @param name  Name des Spielers (nicht null und nicht leer)
	 * @param board Spielfeld des Spielers (nicht null)
	 */
	public Human(String name, GameBoard board) {
		super(name, board);
	}

	/**
	 * Setzt den {@link HumanInputSupplier} dieses Spielers. Das gegebene Objekt
	 * wird bei Entscheidungen benachrichtigt und nach einer Entscheidung gefragt.
	 * 
	 * @param inputSupplier {@link HumanInputSupplier} f�r diese Spieler (nicht
	 *                      null)
	 */
	public void setHumanInputSupplier(HumanInputSupplier inputSupplier) {
		this.inputSupplier = Objects.requireNonNull(inputSupplier);
	}

	@Override
	public DicePair chooseDicePair(List<DicePair> dices, DiceSelectionType type) {
		return waitForSelectionAndCrossField(dices, type);
	}

	/**
	 * Der Benutzer wird gefragt, welchen der gegebenen W�rfelpaare er w�hlen
	 * m�chte. Diese Methode wartet so lange, bis der Benutzer ein W�rfelpaar
	 * ausgew�hlt hat und der {@link HumanInputSupplier} diese Klasse dar�ber
	 * benachrichtigt (notify).
	 * 
	 * @param dices         W�rfel, aus denen der Benutzer ein W�rfelpaar
	 *                      ausgew�hlen soll
	 * @param selectionType Typ der Auswahl (Wei�e- oder Farbw�rfel)
	 * @return Vom Benutzer ausgew�hltes W�rfelpaar
	 */
	private DicePair waitForSelectionAndCrossField(List<DicePair> dices, DiceSelectionType selectionType) {
		DicePair selection = null;
		synchronized (this) {
			try {

				System.out.println("Waiting for human to make input: " + dices);
				inputSupplier.askForInput(dices, selectionType);

				while ((selection = inputSupplier.getHumanInput()) == null) {
					this.wait();
				}

			} catch (InterruptedException e) {
				System.err.println("Player left the game or system interrupted this thread! " + e.getMessage());
			}
		}

		return selection;
	}
}
