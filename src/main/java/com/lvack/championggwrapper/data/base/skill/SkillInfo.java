package com.lvack.championggwrapper.data.base.skill;

import com.google.gson.annotations.SerializedName;
import lombok.Data;


@Data
public class SkillInfo {
	@SerializedName("name")
	private String name;
	@SerializedName("key")
	private String key;
}
