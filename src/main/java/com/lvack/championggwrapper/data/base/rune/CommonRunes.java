package com.lvack.championggwrapper.data.base.rune;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.base.WinRateGameCountRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * CommonRunesClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class CommonRunes extends WinRateGameCountRole {
	@SerializedName("runes")
	private List<RunesData> runes;
}
