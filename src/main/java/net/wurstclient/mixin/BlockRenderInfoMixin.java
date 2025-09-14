/*
 * Copyright (c) 2014-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.wurstclient.event.EventManager;
import net.wurstclient.events.ShouldDrawSideListener.ShouldDrawSideEvent;

@Mixin(value = BlockRenderInfo.class, remap = false)
public abstract class BlockRenderInfoMixin
{
	@Shadow
	public BlockPos blockPos;
	@Shadow
	public BlockState blockState;
	
	/**
	 * This mixin hides and shows regular blocks when using X-Ray, if Indigo
	 * is running and Sodium is not installed.
	 */
	@Inject(at = @At("HEAD"), method = "shouldDrawSide", cancellable = true)
	public void onShouldDrawSide(Direction face,
		CallbackInfoReturnable<Boolean> cir)
	{
		ShouldDrawSideEvent event =
			new ShouldDrawSideEvent(blockState, blockPos);
		EventManager.fire(event);
		
		if(event.isRendered() != null)
			cir.setReturnValue(event.isRendered());
	}
}
