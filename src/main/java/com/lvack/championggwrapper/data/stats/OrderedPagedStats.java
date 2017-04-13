package com.lvack.championggwrapper.data.stats;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * PagedResultClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public class OrderedPagedStats {
	@SerializedName("page")
	private int page;
	@SerializedName("limit")
	private int limit;
	@SerializedName("data")
	private List<ChampionRoleStats> data;
}
