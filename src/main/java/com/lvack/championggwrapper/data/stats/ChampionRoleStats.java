package com.lvack.championggwrapper.data.stats;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.staticdata.ChampionRoleStat;
import com.lvack.championggwrapper.data.staticdata.Role;
import lombok.Data;

import java.util.Map;


@Data
public class ChampionRoleStats {
	@SerializedName("key")
	private String key;
	@SerializedName("role")
	private Role role;
	@SerializedName("name")
	private String name;
	@SerializedName("general")
	private Map<ChampionRoleStat, Double> stats;
}
