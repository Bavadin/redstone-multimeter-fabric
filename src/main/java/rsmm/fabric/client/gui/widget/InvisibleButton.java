package rsmm.fabric.client.gui.widget;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import rsmm.fabric.client.MultimeterClient;
import rsmm.fabric.client.gui.element.action.MousePress;

public class InvisibleButton extends Button {
	
	public InvisibleButton(MultimeterClient client, int x, int y, int width, int height, Supplier<Text> textSupplier, MousePress<Button> onPress) {
		this(client, x, y, width, height, textSupplier, () -> Collections.emptyList(), onPress);
	}
	
	public InvisibleButton(MultimeterClient client, int x, int y, int width, int height, Supplier<Text> textSupplier, Supplier<List<Text>> tooltipSupplier, MousePress<Button> onPress) {
		super(client, x, y, width, height, textSupplier, tooltipSupplier, onPress);
	}
	
	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		Text message = getMessage();
		
		if (!message.asString().isEmpty()) {
			MinecraftClient minecraftClient = client.getMinecraftClient();
			TextRenderer font = minecraftClient.textRenderer;
			
			int rgb = active ? (isHovered() ? 0xC0C0C0 : 0xFFFFFF) : 0x909090;
			int a = MathHelper.ceil(alpha * 255.0F);
			int color = rgb | (a << 24);
			
			int textWidth = font.getWidth(message);
			int textX = x + width - (width + textWidth) / 2;
			int textY = y + (height - font.fontHeight) / 2 + 1;
			
			RenderSystem.setShader(() -> GameRenderer.getPositionTexShader());
			RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			
			font.drawWithShadow(matrices, message, textX, textY, color);
		}
	}
}
