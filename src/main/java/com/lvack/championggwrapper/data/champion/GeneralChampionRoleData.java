package com.lvack.championggwrapper.data.champion;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.base.ranking.AdvancedRankingData;
import com.lvack.championggwrapper.data.base.ranking.RankingData;
import com.lvack.championggwrapper.data.base.skill.SkillMetaData;
import com.lvack.championggwrapper.data.staticdata.Role;
import lombok.Data;


@Data
public class GeneralChampionRoleData {
	@SerializedName("name")
	private String name;
	@SerializedName("role")
	private Role role;
	@SerializedName("overallPosition")
	private RankingData overallPosition;
	@SerializedName("skills")
	private SkillMetaData skills;
	@SerializedName("dmgComposition")
	private DamageComposition damageComposition;
	@SerializedName("winPercent")
	private AdvancedRankingData winPercent;
	@SerializedName("playPercent")
	private AdvancedRankingData playPercent;
	@SerializedName("banRate")
	private AdvancedRankingData banRate;
	@SerializedName("experience")
	private AdvancedRankingData experience;
	@SerializedName("goldEarned")
	private AdvancedRankingData goldEarned;
	@SerializedName("kills")
	private AdvancedRankingData kills;
	@SerializedName("deaths")
	private AdvancedRankingData deaths;
	@SerializedName("totalHeal")
	private AdvancedRankingData totalHeal;
	@SerializedName("visionWardsBoughtInGame")
	private AdvancedRankingData visionWardsBoughtInGame;
	@SerializedName("wardsPlaced")
	private AdvancedRankingData wardsPlaced;
	@SerializedName("wardsKilled")
	private AdvancedRankingData wardsKilled;
	@SerializedName("assists")
	private AdvancedRankingData assists;
	@SerializedName("totalDamageDealtToChampions")
	private AdvancedRankingData totalDamageDealtToChampions;
	@SerializedName("totalDamageTaken")
	private AdvancedRankingData totalDamageTaken;
	@SerializedName("neutralMinionsKilledTeamJungle")
	private AdvancedRankingData neutralMinionsKilledTeamJungle;
	@SerializedName("neutralMinionsKilledEnemyJungle")
	private AdvancedRankingData neutralMinionsKilledEnemyJungle;
	@SerializedName("largestKillingSpree")
	private AdvancedRankingData largestKillingSpree;
	@SerializedName("minionsKilled")
	private AdvancedRankingData minionsKilled;
}
