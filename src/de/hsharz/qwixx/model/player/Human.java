package de.hsharz.qwixx.model.player;

import java.util.List;
import java.util.Objects;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicePair;

/**
 * Diese Klasse repräsentiert einen menschlichen Spieler. <br>
 * Sobald dieser Spieler aufgefordert wird ein Würfelpaar auszuwählen, wird
 * diese Entscheidung an den Benutzer weitergegeben. <br>
 * Die Eingabe des Benutzers erfolgt über das Interface
 * {@link HumanInputSupplier}, welches vor der Verwendung des Human-Players über
 * die Methode {@link #setHumanInputSupplier(HumanInputSupplier)} gesetzt werden
 * muss. Andernfalls kann es zu Fehlern führen.
 * <p>
 * Die Auswahl eines Würfelpaares erfolgt folgendermaßen: <br>
 * Game -&gt; fragt diese Instanz, welches Würfelpaar ausgewählt werden soll <br>
 * Human-Instanz -> gibt Frage an gesetzten {@link HumanInputSupplier} weiter
 * und ruft {@link #wait()} auf. <br>
 * {@link HumanInputSupplier} holt Entscheidung des Benutzers ein und ruft
 * {@link #notify()} auf. <br>
 * Human-Instanz fragt Eingabe des Nutzers vom {@link HumanInputSupplier} über
 * {@link HumanInputSupplier#getHumanInput()}
 * 
 * @author Oliver Lindemann
 */
public class Human extends Player {

	/** {@link HumanInputSupplier} für das Einholen der Entscheidung des Spielers */
	private HumanInputSupplier inputSupplier;

	/**
	 * Erzeugt einen neuen menschlichen Spieler mit dem gegebenen Namen {@code name}
	 * und dem Spielfeld {@code board}. <br>
	 * Der Name darf nicht null und nicht leer sein. <br>
	 * Das Spielfeld darf nicht null sein. <br>
	 * In beiden Fällen wird bei Verstoß eine {@link NullPointerException} geworfen.
	 * <p>
	 * Bevor der Spieler in einem Spiel mitspielen kann, muss noch der
	 * {@link HumanInputSupplier} über die Methode
	 * {@link #setHumanInputSupplier(HumanInputSupplier)} gesetzt werden, damit
	 * Entscheidungen an den Benutzer weitergereicht werden können. Andernfalls kann
	 * es zu Fehlern führen.
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
	 * @param inputSupplier {@link HumanInputSupplier} für diese Spieler (nicht
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
	 * Der Benutzer wird gefragt, welchen der gegebenen Würfelpaare er wählen
	 * möchte. Diese Methode wartet so lange, bis der Benutzer ein Würfelpaar
	 * ausgewählt hat und der {@link HumanInputSupplier} diese Klasse darüber
	 * benachrichtigt (notify).
	 * 
	 * @param dices         Würfel, aus denen der Benutzer ein Würfelpaar
	 *                      ausgewählen soll
	 * @param selectionType Typ der Auswahl (Weiße- oder Farbwürfel)
	 * @return Vom Benutzer ausgewähltes Würfelpaar
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
