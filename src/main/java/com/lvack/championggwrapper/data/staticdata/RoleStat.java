package com.lvack.championggwrapper.data.staticdata;

import com.google.gson.annotations.SerializedName;


public enum RoleStat {
	@SerializedName("winRate")WIN_RATE,
	@SerializedName("goldEarned")GOLD_EARNED,
	@SerializedName("kills")KILLS,
	@SerializedName("assists")ASSISTS,
	@SerializedName("deaths")DEATHS,
	@SerializedName("heal")HEAL,
	@SerializedName("damageDealt")DAMAGE_DEALT,
	@SerializedName("damageTaken")DAMAGE_TAKEN,
	@SerializedName("minionsKilled")MINIONS_KILLED,
	UNKNOWN
}
