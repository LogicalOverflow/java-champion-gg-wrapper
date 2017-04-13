package com.lvack.championggwrapper.data.champion;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.staticdata.Role;
import com.lvack.championggwrapper.data.base.skill.SkillMetaData;
import com.lvack.championggwrapper.data.base.ranking.AdvancedRankingData;
import com.lvack.championggwrapper.data.base.ranking.ChampionRankingData;
import com.lvack.championggwrapper.data.base.ranking.RankingData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * GeneralChampionRoleDataClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class GeneralChampionRoleData extends ChampionRankingData<AdvancedRankingData> {
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
}
