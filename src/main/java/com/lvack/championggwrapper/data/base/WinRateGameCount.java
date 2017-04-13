package com.lvack.championggwrapper.data.base;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * WinRateGameCountClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public abstract class WinRateGameCount {
	@SerializedName("winPercent")
	private double winPercent;
	@SerializedName("games")
	private int gameCount;
}
