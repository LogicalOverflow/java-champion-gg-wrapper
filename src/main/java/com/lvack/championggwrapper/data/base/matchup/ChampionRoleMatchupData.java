package com.lvack.championggwrapper.data.base.matchup;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;


@Data
public class ChampionRoleMatchupData {
	@SerializedName("role")
	private String role;
	@SerializedName("matchups")
	private List<MatchupData> matchups;
}
