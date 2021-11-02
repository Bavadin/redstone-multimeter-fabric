package redstone.multimeter.client.gui.element;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import redstone.multimeter.client.MultimeterClient;
import redstone.multimeter.client.gui.CursorType;
import redstone.multimeter.interfaces.mixin.IMinecraftClient;

public abstract class RSMMScreen extends Screen implements IParentElement {
	
	private final List<IElement> content;
	private final boolean drawTitle;
	
	private boolean focused;
	private IElement focusedElement;
	private boolean dragging;
	
	protected MultimeterClient multimeterClient;
	
	protected RSMMScreen(Text title, boolean drawTitle) {
		super(title);
		
		this.content = new ArrayList<>();
		this.drawTitle = drawTitle;
	}
	
	@Override
	public final void mouseMoved(double mouseX, double mouseY) {
		mouseMove(mouseX, mouseY);
	}
	
	@Override
	public final boolean mouseClicked(double mouseX, double mouseY, int button) {
		return isHovered(mouseX, mouseY) && mouseClick(mouseX, mouseY, button);
	}
	
	@Override
	public final boolean mouseReleased(double mouseX, double mouseY, int button) {
		return mouseRelease(mouseX, mouseY, button);
	}
	
	@Override
	public final boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return mouseDrag(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	@Override
	public final boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		return false;
	}
	
	@Override
	public final boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return keyPress(keyCode, scanCode, modifiers);
	}
	
	@Override
	public final boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		return keyRelease(keyCode, scanCode, modifiers);
	}
	
	@Override
	public final boolean charTyped(char chr, int modifiers) {
		return typeChar(chr, modifiers);
	}

	@Override
	public boolean isDraggingMouse() {
		return dragging;
	}
	
	@Override
	public void setDraggingMouse(boolean dragging) {
		this.dragging = dragging;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		renderContent(matrices, mouseX, mouseY, delta);
		
		if (drawTitle) {
			int width = textRenderer.getWidth(title);
			int x = getX() + (getWidth() - width) / 2;
			int y = getY() + 6;
			
			drawTextWithShadow(matrices, textRenderer, title, x, y, 0xFFFFFFFF);
		}
		
		List<Text> tooltip = getTooltip(mouseX, mouseY);
		
		if (tooltip != null && !tooltip.isEmpty()) {
			renderTooltip(matrices, tooltip, mouseX, mouseY + 15);
		}
	}
	
	@Override
	protected final void init() {
		multimeterClient = ((IMinecraftClient)client).getMultimeterClient();
		
		removeChildren();
		initScreen();
		update();
	}
	
	protected abstract void initScreen();
	
	@Override
	public void tick() {
		IParentElement.super.tick();
	}
	
	@Override
	public void removed() {
		onRemoved();
		setCursor(multimeterClient, CursorType.ARROW);
	}
	
	@Override
	public boolean keyPress(int keyCode, int scanCode, int modifiers) {
		if (IParentElement.super.keyPress(keyCode, scanCode, modifiers)) {
			return true;
		}
		if (shouldCloseOnEsc() && keyCode == GLFW.GLFW_KEY_ESCAPE) {
			onClose();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean isFocused() {
		return focused;
	}
	
	@Override
	public void setFocused(boolean focused) {
		this.focused = focused;
		
		if (!isFocused()) {
			setFocusedElement(null);
		}
	}
	
	@Override
	public List<IElement> getChildren() {
		return content;
	}
	
	@Override
	public IElement getFocusedElement() {
		return focusedElement != null && focusedElement.isFocused() ? focusedElement : null;
	}
	
	@Override
	public void setFocusedElement(IElement element) {
		IElement focused = focusedElement;
		
		if (element == focused) {
			return;
		}
		
		if (focused != null) {
			focused.setFocused(false);
		}
		
		this.focusedElement = element;
		
		if (element != null) {
			element.setFocused(true);
		}
	}
	
	@Override
	public final int getX() {
		return 0;
	}
	
	@Override
	public final void setX(int x) {
		
	}
	
	@Override
	public final int getY() {
		return 0;
	}
	
	@Override
	public final void setY(int y) {
		
	}
	
	@Override
	public final int getWidth() {
		return width;
	}
	
	@Override
	public final int getHeight() {
		return height;
	}
	
	@Override
	public boolean isVisible() {
		return true;
	}
	
	@Override
	public void setVisible(boolean visible) {
		
	}
	
	protected void addContent(IElement element) {
		content.add(element);
	}
	
	protected void renderContent(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		IParentElement.super.render(matrices, mouseX, mouseY, delta);
	}
	
	public static void setCursor(MultimeterClient client, CursorType type) {
		GLFW.glfwSetCursor(client.getMinecraftClient().getWindow().getHandle(), type.getCursor());
	}
	
	public static boolean isControlPressed() {
		return hasControlDown() && !hasShiftDown() && !hasAltDown();
	}
}
