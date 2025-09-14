/*
 * Copyright (c) 2014-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.util;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.wurstclient.WurstClient;

public class FakePlayerEntity extends OtherClientPlayerEntity
{
	public final ClientPlayerEntity player = WurstClient.MC.player;
	public final ClientWorld world = WurstClient.MC.world;
	public PlayerListEntry playerListEntry;
	
	public FakePlayerEntity()
	{
		super(WurstClient.MC.world, WurstClient.MC.player.getGameProfile());
		setUuid(UUID.randomUUID());
		copyPositionAndRotation(player);
		
		copyInventory();
		copyPlayerModel(player, this);
		copyRotation();
		resetCapeMovement();
		
		spawn();
	}
	
	@Override
	protected @Nullable PlayerListEntry getPlayerListEntry()
	{
		if(playerListEntry == null)
			playerListEntry = MinecraftClient.getInstance().getNetworkHandler()
				.getPlayerListEntry(getGameProfile().getId());
		
		return playerListEntry;
	}
	
	@Override
	protected void pushAway(Entity entity)
	{
		// Prevents pushing the real player away
	}
	
	public void copyInventory()
	{
		getInventory().clone(player.getInventory());
	}
	
	public void copyPlayerModel(Entity from, Entity to)
	{
		DataTracker fromTracker = from.getDataTracker();
		DataTracker toTracker = to.getDataTracker();
		Byte playerModel = fromTracker.get(PlayerEntity.PLAYER_MODEL_PARTS);
		toTracker.set(PlayerEntity.PLAYER_MODEL_PARTS, playerModel);
	}
	
	public void copyRotation()
	{
		headYaw = player.headYaw;
		bodyYaw = player.bodyYaw;
	}
	
	public void resetCapeMovement()
	{
		capeX = getX();
		capeY = getY();
		capeZ = getZ();
	}
	
	public void spawn()
	{
		world.addEntity(this);
	}
	
	public void despawn()
	{
		discard();
	}
	
	public void resetPlayerPosition()
	{
		player.refreshPositionAndAngles(getX(), getY(), getZ(), getYaw(),
			getPitch());
	}
}
