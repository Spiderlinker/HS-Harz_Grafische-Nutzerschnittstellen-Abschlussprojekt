package de.hsharz.qwixx.model.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import de.hsharz.qwixx.model.dice.DiceColor;

/**
 * Diese Klasse dient zur Speicherung der Punktzahl eines Spielfeldes eines
 * Spielers. <br>
 * Hier wird die Punktzahl jeder Reihe nach ihrer Farbe gespeichert und die
 * Punktzahl der Fehlwürfe.
 * 
 * @author Oliver Lindemann
 */
public class UserScore {

	/** Punktezahl pro Fehlwurf */
	public static final int SCORE_PER_MISS = -5;

	/**
	 * Nicht-veränderbare-Liste mit Punkten pro angekreuztes Feld. <br>
	 * Die Punkte sind aufsteigend sortiert. Der Index dient hierbei als Anzahl der
	 * angekreuzten Felder.
	 */
	public static final List<Integer> SCORE_PER_CROSS = Collections
			.unmodifiableList(Arrays.asList(0, 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78));

	/**
	 * Liste beinhaltet die Punkte für die jeweilige Reihe bzw. der Farbe der Reihe.
	 * Die Map ist nach dem Schema <FarbeDerReihe, PunkteDerReihe> aufgebaut
	 */
	private Map<DiceColor, Integer> rowScores = new EnumMap<>(DiceColor.class);

	/** Gesamtpunktezahl für die Fehlwürfe */
	private int scoreMisses;

	/**
	 * Erzeugt einen neuen UserScore mit initialem Punktestand von 0 pro Reihe und
	 * Fehlwürfe
	 */
	public UserScore() {
		rowScores.put(DiceColor.RED, 0);
		rowScores.put(DiceColor.YELLOW, 0);
		rowScores.put(DiceColor.GREEN, 0);
		rowScores.put(DiceColor.BLUE, 0);
		scoreMisses = 0;
	}

	/**
	 * Setzt den Punktestand der Reihe der gegebenen Farbe {@code rowColor}. Der
	 * neue Punktestand wird anhand der gegebenen Anzahl angekreuzter Felder
	 * {@code amountFieldsCrossed} zusammen mit der Punkteliste
	 * {@link #SCORE_PER_CROSS} ermittelt. <br>
	 * 
	 * @param rowColor            Farbe der Reihe, dessen Punktzahl gesetzt werden
	 *                            soll
	 * @param amountFieldsCrossed Anzahl der gesetzten Kreuze in der gegebenen Reihe
	 */
	public void setScoreOfRow(DiceColor rowColor, int amountFieldsCrossed) {
		rowScores.put(rowColor, SCORE_PER_CROSS.get(amountFieldsCrossed));
	}

	/**
	 * Setzt den Punktestand der Fehlwürfe.
	 * 
	 * @param amountMisses Anzahl der Fehlwürfe.
	 */
	public void setScoreMisses(int amountMisses) {
		this.scoreMisses = SCORE_PER_MISS * amountMisses;
	}

	/**
	 * Liefert den Punktestand der Reihe der gegebenen Farbe {@code rowColor}. <br>
	 * Falls keine passende Reihe zu der gegebenen Farbe gefunden werden konnte, so
	 * wird 0 zurückgeliefert.
	 * 
	 * @param rowColor Farbe der Reihe, dessen Punktzahl geholt werden soll
	 * @return Punktzahl der Reihe zu der gegebenen Farbe, 0 falls keine Reihe
	 *         gefunden werden konnte
	 */
	public int getScoreOfRow(DiceColor rowColor) {
		Integer scoreOfRow = rowScores.get(rowColor);
		return scoreOfRow == null ? 0 : scoreOfRow;
	}

	/**
	 * @return Liefert die Punktzahl der Fehlwürfe
	 */
	public int getScoreMisses() {
		return scoreMisses;
	}

	/**
	 * @return Liefert die komplette Punktzahl. Dies beinhaltet die Punktzahl jeder
	 *         Reihe und die Punktzahl der Fehlwürfe
	 */
	public int getScoreComplete() {
		int score = getScoreMisses();
		for (int rowScore : rowScores.values()) {
			score += rowScore;
		}
		return score;

		// Alternativ: Stream
//		return rowScores.values().stream()//
//				.mapToInt(i -> i) //
//				.sum() //
//				+ getScoreMisses();
	}

}
