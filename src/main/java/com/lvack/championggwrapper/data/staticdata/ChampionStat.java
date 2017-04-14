package com.lvack.championggwrapper.data.staticdata;

import com.google.gson.annotations.SerializedName;

public enum ChampionStat {
	@SerializedName("winPercent")WIN_PERCENT,
	@SerializedName("playPercent")PLAY_PERCENT,
	@SerializedName("banRate")BAN_RATE,
	@SerializedName("experience")EXPERIENCE,
	@SerializedName("goldEarned")GOLD_EARNED,
	@SerializedName("kills")KILLS,
	@SerializedName("deaths")DEATHS,
	@SerializedName("totalHeal")TOTAL_HEAL,
	@SerializedName("visionWardsBoughtInGame")VISION_WARDS_BOUGHT_IN_GAME,
	@SerializedName("wardsPlaced")WARDS_PLACED,
	@SerializedName("wardsKilled")WARDS_KILLED,
	@SerializedName("assists")ASSISTS,
	@SerializedName("totalDamageDealtToChampions")TOTAL_DAMAGE_DEALT_TO_CHAMPIONS,
	@SerializedName("totalDamageTaken")TOTAL_DAMAGE_TAKEN,
	@SerializedName("neutralMinionsKilledTeamJungle")NEUTRAL_MINIONS_KILLED_TEAM_JUNGLE,
	@SerializedName("neutralMinionsKilledEnemyJungle")NEUTRAL_MINIONS_KILLED_ENEMY_JUNGLE,
	@SerializedName("largestKillingSpree")LARGEST_KILLING_SPREE,
	@SerializedName("minionsKilled")MINIONS_KILLED,
	UNKNOWN
}
