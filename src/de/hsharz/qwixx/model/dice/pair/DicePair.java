package de.hsharz.qwixx.model.dice.pair;

import de.hsharz.qwixx.model.dice.DicesSum;

public class DicePair extends Pair<DicesSum> {

	public DicePair(DicesSum first, DicesSum second) {
		super(first, second);
	}

	@Override
	public DicesSum getFirst() {
		return getNotNullDicesSum(super.getFirst());
	}

	@Override
	public DicesSum getSecond() {
		return getNotNullDicesSum(super.getSecond());
	}

	private DicesSum getNotNullDicesSum(DicesSum sum) {
		return sum == null ? DicesSum.EMPTY : sum;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() || (DicesSum.EMPTY.equals(getFirst()) && DicesSum.EMPTY.equals(getSecond()));
	}

	public static DicePair of(DicesSum first, DicesSum second) {
		return new DicePair(first, second);
	}
}
