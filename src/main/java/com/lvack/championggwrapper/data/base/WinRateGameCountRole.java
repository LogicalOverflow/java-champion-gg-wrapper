package com.lvack.championggwrapper.data.base;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.staticdata.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class WinRateGameCountRole extends WinRateGameCount {
	@SerializedName("role")
	Role role;
}
