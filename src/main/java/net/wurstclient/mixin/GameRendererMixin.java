/*
 * Copyright (c) 2014-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.mixin;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.wurstclient.event.EventManager;
import net.wurstclient.events.CameraTransformViewBobbingListener.CameraTransformViewBobbingEvent;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements AutoCloseable
{
	@Unique
	public boolean cancelNextBobView;
	
	/**
	 * Fires the CameraTransformViewBobbingEvent event and records whether the
	 * next view-bobbing call should be cancelled.
	 */
	@Inject(at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V",
		ordinal = 0),
		method = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V")
	public void onRenderWorldViewBobbing(RenderTickCounter tickCounter,
		CallbackInfo ci)
	{
		CameraTransformViewBobbingEvent event =
			new CameraTransformViewBobbingEvent();
		EventManager.fire(event);
		
		if(event.isCancelled())
			cancelNextBobView = true;
	}
	
	/**
	 * Cancels the view-bobbing call if requested by the last
	 * CameraTransformViewBobbingEvent.
	 */
	@Inject(at = @At("HEAD"),
		method = "bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V",
		cancellable = true)
	public void onBobView(MatrixStack matrices, float tickDelta,
		CallbackInfo ci)
	{
		if(!cancelNextBobView)
			return;
		
		ci.cancel();
		cancelNextBobView = false;
	}
	
	/**
	 * This mixin is injected into a random method call later in the
	 * renderWorld() method to ensure that cancelNextBobView is always reset
	 * after the view-bobbing call.
	 */
	@Inject(at = @At("HEAD"), method = "renderHand(FZLorg/joml/Matrix4f;)V")
	public void onRenderHand(float tickDelta, boolean bl, Matrix4f matrix4f,
		CallbackInfo ci)
	{
		cancelNextBobView = false;
	}
	
	/*@Inject(at = @At(value = "RETURN", ordinal = 1),
		method = "getFov(Lnet/minecraft/client/render/Camera;FZ)F",
		cancellable = true)
	public void onGetFov(Camera camera, float tickDelta, boolean changingFov,
		CallbackInfoReturnable<Float> cir)
	{
		cir.setReturnValue(WurstClient.INSTANCE.getOtfs().zoomOtf
			.changeFovBasedOnZoom(cir.getReturnValueF()));
	}
	
	*//**
	 * This is the part that makes Liquids work.
	 *//*
	@WrapOperation(at = @At(value = "INVOKE",
		target = "Lnet/minecraft/entity/Entity;raycast(DFZ)Lnet/minecraft/util/hit/HitResult;",
		ordinal = 0),
		method = "findCrosshairTarget(Lnet/minecraft/entity/Entity;DDF)Lnet/minecraft/util/hit/HitResult;")
	public HitResult liquidsRaycast(Entity instance, double maxDistance,
		float tickDelta, boolean includeFluids, Operation<HitResult> original)
	{
		if(!WurstClient.INSTANCE.getHax().liquidsHack.isEnabled())
			return original.call(instance, maxDistance, tickDelta,
				includeFluids);
		
		return original.call(instance, maxDistance, tickDelta, true);
	}
	
	@WrapOperation(
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F",
			ordinal = 0),
		method = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V")
	public float onRenderWorldNauseaLerp(float delta, float start, float end,
		Operation<Float> original)
	{
		if(!WurstClient.INSTANCE.getHax().antiWobbleHack.isEnabled())
			return original.call(delta, start, end);
		
		return 0;
	}
	
	@Inject(at = @At("HEAD"),
		method = "getNightVisionStrength(Lnet/minecraft/entity/LivingEntity;F)F",
		cancellable = true)
	public static void onGetNightVisionStrength(LivingEntity entity,
		float tickDelta, CallbackInfoReturnable<Float> cir)
	{
		FullbrightHack fullbright =
			WurstClient.INSTANCE.getHax().fullbrightHack;
		
		if(fullbright.isNightVisionActive())
			cir.setReturnValue(fullbright.getNightVisionStrength());
	}
	
	@Inject(at = @At("HEAD"),
		method = "tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V",
		cancellable = true)
	public void onTiltViewWhenHurt(MatrixStack matrices, float tickDelta,
		CallbackInfo ci)
	{
		if(WurstClient.INSTANCE.getHax().noHurtcamHack.isEnabled())
			ci.cancel();
	}*/
}
