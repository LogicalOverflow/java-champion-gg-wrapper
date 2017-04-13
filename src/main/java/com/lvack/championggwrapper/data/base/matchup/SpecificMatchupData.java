package com.lvack.championggwrapper.data.base.matchup;

import com.google.gson.annotations.SerializedName;
import lombok.Data;


@Data
public class SpecificMatchupData {
	@SerializedName("games")
	private int gameCount;
	@SerializedName("statScore")
	private double statScore;
	@SerializedName("winRate")
	private double winRate;
	@SerializedName("winRateChange")
	private double winRateChange;
}
