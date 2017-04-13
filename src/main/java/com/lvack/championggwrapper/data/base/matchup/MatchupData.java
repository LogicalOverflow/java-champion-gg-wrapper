package com.lvack.championggwrapper.data.base.matchup;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * MatchupDataClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class MatchupData extends SpecificMatchupData {
	@SerializedName("key")
	private String enemyKey;
}
