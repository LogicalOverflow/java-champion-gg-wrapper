package com.lvack.championggwrapper.data.base.summoner;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.base.WinRateGameCountRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class CommonSummonersPair extends WinRateGameCountRole {
	@SerializedName("summoner1")
	private String summonerSpell1;
	@SerializedName("summoner2")
	private String summonerSpell2;
}
