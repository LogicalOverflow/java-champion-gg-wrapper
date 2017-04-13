package com.lvack.championggwrapper.data.base.ranking;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * AdvancedRakingDataClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class AdvancedRankingData extends RankingData {
	@SerializedName("val")
	private String val;
}
