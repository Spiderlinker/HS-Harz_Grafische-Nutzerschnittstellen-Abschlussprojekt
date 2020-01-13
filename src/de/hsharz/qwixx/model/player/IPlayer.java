package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicePair;

/**
 * Diese Klasse repr�sentiert einen Spieler. <br>
 * Ein Spieler hat einen Namen (welcher �ber {@link #getName()} abgefragt werden
 * kann) und ein Spielfeld / Spielblock, auf dem er dieses Spiel spielt (welches
 * �ber die Methode {@link #getGameBoard()} geholt werden kann).
 * <p>
 * Die Methode {@link #chooseWhiteDices(List)} wird vom Spiel auf dem Spieler
 * aufgerufen, wenn dieser einen wei�en W�rfel w�hlen soll. Alle m�glichen
 * W�rfel (-paare) werden dem Spieler als Liste als Parameter gegeben. <br>
 * Die Methode {@link #chooseColorDices(List)} wird vom Spiel auf dem Spieler
 * aufgerufen, wenn dieser einen Farbw�rfel w�hlen soll. Alle m�glichen W�rfel
 * (-paare) werden dem Spieler als Liste als Parameter gegeben.
 * 
 * @author Oliver Lindemann
 */
public interface IPlayer {

	/**
	 * Der Spieler soll aus der gegebenen Liste mit W�rfelpaaren {@code dices} einen
	 * W�rfel ausw�hlen und diesen an den Aufrufer zur�ckliefern. <br>
	 * Falls der Spieler keinen W�rfel w�hlen will, so kann er auch einen
	 * {@link DicePair#EMPTY} oder einen {@link DicePair#MISS} zur�ck liefern. <br>
	 * Falls {@code null} zur�ckgeliefert werden sollte, so sind Fehler im weiteren
	 * Verlauf nicht ausgeschlossen.
	 * 
	 * @param dices W�rfelpaare, aus denen der Spieler ein W�rfelpaar ausw�hlen soll
	 * @return vom Spieler ausgew�hltes W�rfelpaar
	 */
	DicePair chooseWhiteDices(List<DicePair> dices);

	/**
	 * Der Spieler soll aus der gegebenen Liste mit W�rfelpaaren {@code dices} einen
	 * W�rfel ausw�hlen und diesen an den Aufrufer zur�ckliefern. <br>
	 * Falls der Spieler keinen W�rfel w�hlen will, so kann er auch einen
	 * {@link DicePair#EMPTY} oder einen {@link DicePair#MISS} zur�ck liefern. <br>
	 * Falls {@code null} zur�ckgeliefert werden sollte, so sind Fehler im weiteren
	 * Verlauf nicht ausgeschlossen.
	 * 
	 * @param dices W�rfelpaare, aus denen der Spieler ein W�rfelpaar ausw�hlen soll
	 * @return vom Spieler ausgew�hltes W�rfelpaar
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
