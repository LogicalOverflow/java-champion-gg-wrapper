package com.lvack.championggwrapper.data.base.summoner;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.base.WinRateGameCount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class SummonerPairData extends WinRateGameCount {
	@SerializedName("summoner1")
	private SummonerSpellData summonerSpell1;
	@SerializedName("summoner2")
	private SummonerSpellData summonerSpell2;
}
