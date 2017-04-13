package com.lvack.championggwrapper.data.base.ranking;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * RankingDataClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public class RankingData {
	@SerializedName("position")
	private int position;
	@SerializedName("change")
	private int change;
}
