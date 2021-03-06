package com.lvack.championggwrapper.data.staticdata;


public enum StatOrder {
	MOST_BANNED("mostBanned"),
	MOST_WINNING("mostWinning"),
	LEAST_WINNING("leastWinning"),
	MOST_PLAYED("mostPlayed"),
	LEAST_PLAYED("leastPlayed"),
	BEST_RATED("bestRated"),
	WORST_RATED("worstRated");

	private final String serialized;

	StatOrder(String serialized) {
		this.serialized = serialized;
	}

	public @Override String toString() {
		return serialized;
	}
}
