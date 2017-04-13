package com.lvack.championggwrapper.data.base;

import com.google.gson.annotations.SerializedName;
import lombok.Data;


@Data
public abstract class WinRateGameCount {
	@SerializedName("winPercent")
	private double winPercent;
	@SerializedName("games")
	private int gameCount;
}
