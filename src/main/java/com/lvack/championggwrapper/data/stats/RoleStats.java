package com.lvack.championggwrapper.data.stats;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.staticdata.Role;
import com.lvack.championggwrapper.data.staticdata.RoleStat;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class RoleStats {
	@SerializedName("totalNumber")
	private int gameCount;
	@SerializedName("patchPlay")
	private List<Double> playRate;
	@SerializedName("role")
	private Role role;
	@SerializedName("data")
	private Map<RoleStat, Double> data;
}
