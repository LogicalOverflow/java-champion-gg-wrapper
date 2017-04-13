package com.lvack.championggwrapper.data.champion;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.base.item.ItemsData;
import com.lvack.championggwrapper.data.base.item.TrinketData;
import com.lvack.championggwrapper.data.base.masteries.Masteries;
import com.lvack.championggwrapper.data.base.matchup.MatchupData;
import com.lvack.championggwrapper.data.base.ranking.ChampionRankingData;
import com.lvack.championggwrapper.data.base.ranking.RankingData;
import com.lvack.championggwrapper.data.base.rune.RuneSetData;
import com.lvack.championggwrapper.data.base.skill.SkillsData;
import com.lvack.championggwrapper.data.base.summoner.SummonerPairsData;
import com.lvack.championggwrapper.data.staticdata.Role;
import lombok.Data;

import java.util.List;


@Data
public class DetailedChampionRoleData {
	@SerializedName("key")
	private String key;
	@SerializedName("role")
	private Role role;
	@SerializedName("overallPosition")
	private RankingData overallPosition;
	@SerializedName("items")
	private ItemsData items;
	@SerializedName("firstItems")
	private ItemsData firstItems;
	@SerializedName("championMatrix")
	private List<Double> championMatrix;
	@SerializedName("trinkets")
	private List<TrinketData> trinkets;
	@SerializedName("summoners")
	private SummonerPairsData summonerSpells;
	@SerializedName("runes")
	private RuneSetData runes;
	@SerializedName("experienceRate")
	private List<Double> experienceRate;
	@SerializedName("experienceSample")
	private List<Double> experienceSample;
	@SerializedName("patchWin")
	private List<Double> patchWin;
	@SerializedName("patchPlay")
	private List<Double> patchPlay;
	@SerializedName("dmgComposition")
	private DamageComposition damageComposition;
	@SerializedName("matchups")
	private List<MatchupData> matchups;
	@SerializedName("general")
	private ChampionRankingData<RankingData> rankingData;
	@SerializedName("skills")
	private SkillsData skills;
	@SerializedName("masteries")
	private Masteries masteries;
}
