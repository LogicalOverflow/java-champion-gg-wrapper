package com.lvack.championggwrapper.data.base.role;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.staticdata.Role;
import lombok.Data;

/**
 * RoleDataClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public class RoleData {
	@SerializedName("games")
	private int gameCount;
	@SerializedName("percentPlayed")
	private double percentPlayed;
	@SerializedName("name")
	private Role role;
}
