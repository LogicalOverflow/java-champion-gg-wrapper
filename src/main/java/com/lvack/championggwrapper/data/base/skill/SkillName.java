package com.lvack.championggwrapper.data.base.skill;

import com.google.gson.annotations.SerializedName;
import lombok.Data;


@Data
public class SkillName {
	@SerializedName("name")
	private String name;
}
