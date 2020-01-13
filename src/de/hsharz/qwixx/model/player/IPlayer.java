package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicePair;

/**
 * Diese Klasse repräsentiert einen Spieler. <br>
 * Ein Spieler hat einen Namen (welcher über {@link #getName()} abgefragt werden
 * kann) und ein Spielfeld / Spielblock, auf dem er dieses Spiel spielt (welches
 * über die Methode {@link #getGameBoard()} geholt werden kann).
 * <p>
 * Die Methode {@link #chooseWhiteDices(List)} wird vom Spiel auf dem Spieler
 * aufgerufen, wenn dieser einen weißen Würfel wählen soll. Alle möglichen
 * Würfel (-paare) werden dem Spieler als Liste als Parameter gegeben. <br>
 * Die Methode {@link #chooseColorDices(List)} wird vom Spiel auf dem Spieler
 * aufgerufen, wenn dieser einen Farbwürfel wählen soll. Alle möglichen Würfel
 * (-paare) werden dem Spieler als Liste als Parameter gegeben.
 * 
 * @author Oliver Lindemann
 */
public interface IPlayer {

	/**
	 * Der Spieler soll aus der gegebenen Liste mit Würfelpaaren {@code dices} einen
	 * Würfel auswählen und diesen an den Aufrufer zurückliefern. <br>
	 * Falls der Spieler keinen Würfel wählen will, so kann er auch einen
	 * {@link DicePair#EMPTY} oder einen {@link DicePair#MISS} zurück liefern. <br>
	 * Falls {@code null} zurückgeliefert werden sollte, so sind Fehler im weiteren
	 * Verlauf nicht ausgeschlossen.
	 * 
	 * @param dices Würfelpaare, aus denen der Spieler ein Würfelpaar auswählen soll
	 * @return vom Spieler ausgewähltes Würfelpaar
	 */
	DicePair chooseWhiteDices(List<DicePair> dices);

	/**
	 * Der Spieler soll aus der gegebenen Liste mit Würfelpaaren {@code dices} einen
	 * Würfel auswählen und diesen an den Aufrufer zurückliefern. <br>
	 * Falls der Spieler keinen Würfel wählen will, so kann er auch einen
	 * {@link DicePair#EMPTY} oder einen {@link DicePair#MISS} zurück liefern. <br>
	 * Falls {@code null} zurückgeliefert werden sollte, so sind Fehler im weiteren
	 * Verlauf nicht ausgeschlossen.
	 * 
	 * @param dices Würfelpaare, aus denen der Spieler ein Würfelpaar auswählen soll
	 * @return vom Spieler ausgewähltes Würfelpaar
	 */
	DicePair chooseColorDices(List<DicePair> dices);

	/**
	 * @return Liefert das Spielfeld / den Spielblock dieses Spielers
	 */
	GameBoard getGameBoard();

	/**
	 * @return Liefert den Namen dieses Spielers
	 */
	String getName();

}
