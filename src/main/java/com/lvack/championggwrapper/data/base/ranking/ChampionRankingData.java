package com.lvack.championggwrapper.data.base.ranking;

import com.google.gson.annotations.SerializedName;
import lombok.Data;


@Data
public class ChampionRankingData<T> {
	@SerializedName("winPercent")
	private T winPercent;
	@SerializedName("playPercent")
	private T playPercent;
	@SerializedName("banRate")
	private T banRate;
	@SerializedName("experience")
	private T experience;
	@SerializedName("goldEarned")
	private T goldEarned;
	@SerializedName("kills")
	private T kills;
	@SerializedName("deaths")
	private T deaths;
	@SerializedName("totalHeal")
	private T totalHeal;
	@SerializedName("visionWardsBoughtInGame")
	private T visionWardsBoughtInGame;
	@SerializedName("wardsPlaced")
	private T wardsPlaced;
	@SerializedName("wardsKilled")
	private T wardsKilled;
	@SerializedName("assists")
	private T assists;
	@SerializedName("totalDamageDealtToChampions")
	private T totalDamageDealtToChampions;
	@SerializedName("totalDamageTaken")
	private T totalDamageTaken;
	@SerializedName("neutralMinionsKilledTeamJungle")
	private T neutralMinionsKilledTeamJungle;
	@SerializedName("neutralMinionsKilledEnemyJungle")
	private T neutralMinionsKilledEnemyJungle;
	@SerializedName("largestKillingSpree")
	private T largestKillingSpree;
	@SerializedName("minionsKilled")
	private T minionsKilled;
}
