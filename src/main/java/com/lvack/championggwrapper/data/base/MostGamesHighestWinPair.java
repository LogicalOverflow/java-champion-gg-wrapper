package com.lvack.championggwrapper.data.base;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * MostGamesHighestWinPairClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public abstract class MostGamesHighestWinPair<T> {
	@SerializedName("mostGames")
	private T mostGames;
	@SerializedName("highestWinPercent")
	private T highestWinPercent;
}
