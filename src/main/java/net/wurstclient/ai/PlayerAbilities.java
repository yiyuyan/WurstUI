/*
 * Copyright (c) 2014-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.ai;

import net.minecraft.client.MinecraftClient;
import net.wurstclient.WurstClient;
import net.wurstclient.hack.HackList;

public record PlayerAbilities(boolean invulnerable, boolean creativeFlying,
	boolean flying, boolean immuneToFallDamage, boolean noWaterSlowdown,
	boolean jesus, boolean spider)
{
	
	public static final WurstClient WURST = WurstClient.INSTANCE;
	public static final MinecraftClient MC = WurstClient.MC;
	
	public static PlayerAbilities get()
	{
		HackList hax = WURST.getHax();
		net.minecraft.entity.player.PlayerAbilities mcAbilities =
			MC.player.getAbilities();
		
		boolean invulnerable =
			mcAbilities.invulnerable || mcAbilities.creativeMode;
		boolean creativeFlying = mcAbilities.flying;
		boolean flying = mcAbilities.flying;
		boolean immuneToFallDamage = invulnerable;
		boolean noWaterSlowdown = false;
		boolean jesus = false;
		boolean spider = false;
		
		return new PlayerAbilities(invulnerable, creativeFlying, flying,
			immuneToFallDamage, noWaterSlowdown, jesus, spider);
	}
}
