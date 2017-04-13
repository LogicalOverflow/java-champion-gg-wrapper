package com.lvack.championggwrapper.data.base.role;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.staticdata.Role;
import lombok.Data;


@Data
public class RoleData {
	@SerializedName("games")
	private int gameCount;
	@SerializedName("percentPlayed")
	private double percentPlayed;
	@SerializedName("name")
	private Role role;
}
