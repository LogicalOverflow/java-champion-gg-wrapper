package com.lvack.championggwrapper.data.base.item;

import com.lvack.championggwrapper.data.base.MostGamesHighestWinPair;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class ItemsData extends MostGamesHighestWinPair<ItemSet> {
}
