package de.hsharz.qwixx.model.dice;

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

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	public static <T> Pair<T> of(T first, T second) {
		return new Pair<>(first, second);
	}
}
