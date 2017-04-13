package com.lvack.championggwrapper.data.champion;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.base.role.RoleData;
import lombok.Data;

import java.util.List;

/**
 * BasicChampionDataClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */


@Data
public class HighLevelChampionData {
	@SerializedName("key")
	private String key;
	@SerializedName("lastUpdated")
	private long lastUpdated;
	@SerializedName("name")
	private String name;
	@SerializedName("roles")
	private List<RoleData> roles;
}
