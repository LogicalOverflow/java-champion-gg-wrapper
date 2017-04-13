package com.lvack.championggwrapper.data.base.matchup;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * SpecificMatchupDataClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

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
