package com.lvack.championggwrapper.data.base.masteries;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * MasteryDataClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public class MasteryData {
	@SerializedName("mastery")
	private int masteryId;
	@SerializedName("points")
	private int points;
}
