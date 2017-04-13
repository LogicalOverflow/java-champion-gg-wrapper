package com.lvack.championggwrapper.data.staticdata;


public enum RoleStatOrder {
	MOST_IMPROVED("mostImproved"),
	LEAST_IMPROVED("leastImproved"),
	MOST_WINNING("mostWinning"),
	LEAST_WINNING("leastWinning"),
	BEST_PERFORMANCE("bestPerformance"),
	WORST_PERFORMANCE("worstPerformance");

	private final String serialized;

	RoleStatOrder(String serialized) {
		this.serialized = serialized;
	}

	public @Override String toString() {
		return serialized;
	}
}
