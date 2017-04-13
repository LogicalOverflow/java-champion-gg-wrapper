package com.lvack.championggwrapper.data.base.skill;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * SkillInfoClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public class SkillInfo {
	@SerializedName("name")
	private String name;
	@SerializedName("key")
	private String key;
}
