package de.hsharz.qwixx.model.dice.pair;

public class Pair<T> {

	private T first;
	private T second;

	public Pair(T first, T second) {
		this.first = first;
		this.second = second;
	}

	public T getFirst() {
		return this.first;
	}

	public T getSecond() {
		return this.second;
	}
	
	public boolean isEmpty() {
		return first == null && second == null;
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	public static <T> Pair<T> of(T first, T second) {
		return new Pair<>(first, second);
	}
}
