package cn.ksmcbrigade.example.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.wurstclient.clickgui.ClickGui;
import net.wurstclient.clickgui.screens.ClickGuiScreen;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/9/14 上午8:29
 */
public class ExampleClickGuiScreen extends ClickGuiScreen {

    public ExampleClickGuiScreen(ClickGui gui) {
        super(gui);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        if(this.client==null) this.client = MinecraftClient.getInstance();
        if (this.client.world == null) this.renderPanoramaBackground(context, deltaTicks);

        this.applyBlur(context);
        this.renderDarkening(context);
    }
}
