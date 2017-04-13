package com.lvack.championggwrapper.data.base.ranking;

import com.google.gson.annotations.SerializedName;
import lombok.Data;


@Data
public class RankingData {
	@SerializedName("position")
	private int position;
	@SerializedName("change")
	private int change;
}
