package cn.ksmcbrigade.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.wurstclient.WurstClient;
import net.wurstclient.clickgui.screens.ClickGuiScreen;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ExampleMod implements ModInitializer {

    public static final KeyBinding key = KeyBindingRegistryImpl.registerKeyBinding(new KeyBinding("ClickGui", GLFW.GLFW_KEY_RIGHT_CONTROL,KeyBinding.UI_CATEGORY));

    @Override
    public void onInitialize() {
        new Thread(()->{
            while (WurstClient.INSTANCE.gui==null) Thread.yield();
            WurstClient.INSTANCE.gui.txtColor = Color.BLACK.getRGB();
        }).start();
        WorldRenderEvents.END.register(worldRenderContext -> {
            if(key.isPressed()) MinecraftClient.getInstance().setScreen(new ClickGuiScreen(WurstClient.INSTANCE.getGui()));
        });
    }
}
