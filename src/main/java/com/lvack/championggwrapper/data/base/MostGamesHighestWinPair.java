package com.lvack.championggwrapper.data.base;

import com.google.gson.annotations.SerializedName;
import lombok.Data;


@Data
public abstract class MostGamesHighestWinPair<T> {
	@SerializedName("mostGames")
	private T mostGames;
	@SerializedName("highestWinPercent")
	private T highestWinPercent;
}
