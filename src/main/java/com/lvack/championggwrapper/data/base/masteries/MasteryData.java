package com.lvack.championggwrapper.data.base.masteries;

import com.google.gson.annotations.SerializedName;
import lombok.Data;


@Data
public class MasteryData {
	@SerializedName("mastery")
	private int masteryId;
	@SerializedName("points")
	private int points;
}
