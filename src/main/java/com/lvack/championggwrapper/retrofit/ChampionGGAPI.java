package com.lvack.championggwrapper.retrofit;

import com.lvack.championggwrapper.annotations.*;
import com.lvack.championggwrapper.data.base.item.CommonItems;
import com.lvack.championggwrapper.data.base.matchup.ChampionRoleMatchupData;
import com.lvack.championggwrapper.data.base.rune.CommonRunes;
import com.lvack.championggwrapper.data.base.skill.CommonSillOrder;
import com.lvack.championggwrapper.data.base.summoner.CommonSummonersPair;
import com.lvack.championggwrapper.data.champion.DetailedChampionRoleData;
import com.lvack.championggwrapper.data.champion.GeneralChampionRoleData;
import com.lvack.championggwrapper.data.champion.HighLevelChampionData;
import com.lvack.championggwrapper.data.staticdata.Role;
import com.lvack.championggwrapper.data.staticdata.RoleStatOrder;
import com.lvack.championggwrapper.data.staticdata.StatOrder;
import com.lvack.championggwrapper.data.stats.BasicChampionStats;
import com.lvack.championggwrapper.data.stats.ChampionRoleStats;
import com.lvack.championggwrapper.data.stats.OrderedPagedStats;
import com.lvack.championggwrapper.data.stats.RoleStats;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;


public interface ChampionGGAPI {
	@GET("/champion") @Permits
	APIResponse<List<HighLevelChampionData>> getHighLevelChampionData();

	@GET("/champion/{key}") @Permits
	APIResponse<List<DetailedChampionRoleData>> getDetailedChampionRoleData(@Path("key") @ChampionKey String key);

	@GET("/champion/{key}/general") @Permits
	APIResponse<List<GeneralChampionRoleData>> getGeneralChampionRoleData(@Path("key") @ChampionKey String key);

	@GET("/champion/{key}/matchup") @Permits
	APIResponse<List<ChampionRoleMatchupData>> getChampionRoleMatchupData(@Path("key") @ChampionKey String key);

	@GET("/champion/{key}/items/finished/mostPopular") @Permits
	APIResponse<List<CommonItems>> getMostPopularItems(@Path("key") @ChampionKey String key);

	@GET("/champion/{key}/items/finished/mostWins") @Permits
	APIResponse<List<CommonItems>> getMostWinningItems(@Path("key") @ChampionKey String key);

	@GET("/champion/{key}/items/starters/mostPopular") @Permits
	APIResponse<List<CommonItems>> getMostPopularStartingItems(@Path("key") @ChampionKey String key);

	@GET("/champion/{key}/items/starters/mostWins") @Permits
	APIResponse<List<CommonItems>> getMostWinningStartingItems(@Path("key") @ChampionKey String key);

	@GET("/champion/{key}/skills/mostPopular") @Permits
	APIResponse<List<CommonSillOrder>> getMostPopularSkillOrder(@Path("key") @ChampionKey String key);

	@GET("/champion/{key}/skills/mostWins") @Permits
	APIResponse<List<CommonSillOrder>> getMostWinningSkillOrder(@Path("key") @ChampionKey String key);

	@GET("/champion/{key}/summoners/mostPopular") @Permits
	APIResponse<List<CommonSummonersPair>> getMostPopularSummonerSpells(@Path("key") @ChampionKey String key);

	@GET("/champion/{key}/summoners/mostWins") @Permits
	APIResponse<List<CommonSummonersPair>> getMostWinningSummonerSpells(@Path("key") @ChampionKey String key);

	@GET("/champion/{key}/runes/mostPopular") @Permits
	APIResponse<List<CommonRunes>> getMostPopularRunes(@Path("key") @ChampionKey String key);

	@GET("/champion/{key}/runes/mostWins") @Permits
	APIResponse<List<CommonRunes>> getMostWinningRunes(@Path("key") @ChampionKey String key);

	@GET("/stats") @Permits
	APIResponse<List<ChampionRoleStats>> getBasicStats();

	@GET("/stats/role") @Permits
	APIResponse<List<RoleStats>> getRoleStats();

	@GET("/stats/champs/{key}") @Permits
	APIResponse<List<BasicChampionStats>> getBasicStats(@Path("key") @ChampionKey String key);

	@GET("/stats/champs/{orderedByStat}") APIResponse<OrderedPagedStats>
	getOrderedStats(@Path("orderedByStat") @OrderKey StatOrder order,
					@Query("page") @PageNumber Integer page, @Query("limit") @PageLimit Integer limit);

	@GET("/stats/role/{role}") APIResponse<OrderedPagedStats>
	getStatsForRole(@Path("role") @RoleKey Role role,
					@Query("page") @PageNumber Integer page, @Query("limit") @PageLimit Integer limit);

	@GET("/stats/role/{role}/{orderedByRoleStat}") APIResponse<OrderedPagedStats>
	getOrderedStatsForRole(@Path("role") @RoleKey Role role, @Path("orderedByRoleStat") @OrderKey RoleStatOrder order,
						   @Query("page") @PageNumber Integer page, @Query("limit") @PageLimit Integer limit);
}
