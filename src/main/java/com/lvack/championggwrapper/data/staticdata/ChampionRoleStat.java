package com.lvack.championggwrapper.data.staticdata;

import com.google.gson.annotations.SerializedName;

/**
 * ExtendedRoleStatClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

public enum ChampionRoleStat {
	@SerializedName("winPercent") WIN_PERCENT,
	@SerializedName("playPercent") PLAY_PERCENT,
	@SerializedName("banRate") BAN_RATE,
	@SerializedName("experience") EXPERIENCE,
	@SerializedName("kills") KILLS,
	@SerializedName("deaths") DEATHS,
	@SerializedName("assists") ASSISTS,
	@SerializedName("totalDamageDealtToChampions") TOTAL_DAMAGE_DEALT_TO_CHAMPIONS,
	@SerializedName("totalDamageTaken") TOTAL_DAMAGE_TAKEN,
	@SerializedName("totalHeal") TOTAL_HEAL,
	@SerializedName("largestKillingSpree") LARGEST_KILLING_SPREE,
	@SerializedName("minionsKilled") MINIONS_KILLED,
	@SerializedName("neutralMinionsKilledTeamJungle") NEUTRAL_MINIONS_KILLED_TEAM_JUNGLE,
	@SerializedName("neutralMinionsKilledEnemyJungle") NEUTRAL_MINIONS_KILLED_ENEMY_JUNGLE,
	@SerializedName("goldEarned") GOLD_EARNED,
	@SerializedName("overallPosition") OVERALL_POSITION,
	@SerializedName("overallPositionChange") OVERALL_POSITION_CHANGE,
	UNKNOWN;
}
