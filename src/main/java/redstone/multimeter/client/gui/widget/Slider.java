package redstone.multimeter.client.gui.widget;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class Slider extends SliderWidget implements IButton {
	
	private final Supplier<Text> textSupplier;
	private final Supplier<List<Text>> tooltipSupplier;
	private final Supplier<Double> valueSupplier;
	private final SlideAction onSlide;
	private final Consumer<Double> snap;
	
	private boolean dragging;
	
	public Slider(int x, int y, int width, int height, Supplier<Text> textSupplier, Supplier<Double> valueSupplier, SlideAction onSlide, Function<Double, Double> snap) {
		this(x, y, width, height, textSupplier, () -> Collections.emptyList(), valueSupplier, onSlide, snap);
	}
	
	public Slider(int x, int y, int width, int height, Supplier<Text> textSupplier, Supplier<List<Text>> tooltipSupplier, Supplier<Double> valueSupplier, SlideAction onSlide, Function<Double, Double> snap) {
		super(x, y, width, height, textSupplier.get(), valueSupplier.get());
		
		this.textSupplier = textSupplier;
		this.tooltipSupplier = tooltipSupplier;
		this.valueSupplier = valueSupplier;
		this.onSlide = onSlide;
		this.snap = v -> setValue(snap.apply(v));
		
		this.updateMessage();
	}
	
	@Override
	protected void applyValue() {
		snap();
		onSlide.onSlide(this);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public void mouseMove(double mouseX, double mouseY) {
		super.mouseMoved(mouseX, mouseY);
	}
	
	@Override
	public boolean mouseClick(double mouseX, double mouseY, int button) {
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseRelease(double mouseX, double mouseY, int button) {
		return isHovered(mouseX, mouseY) && super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	@Override
	public boolean mouseScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (isDraggingMouse() || Math.abs(scrollX) < 1.0D) {
			return false;
		}
		
		double newValue = value - (scrollX / 100.0D);
		newValue = MathHelper.clamp(newValue, 0.0D, 1.0D);
		
		setValue(newValue);
		applyValue();
		updateMessage();
		
		return true;
	}
	
	@Override
	public boolean keyPress(int keyCode, int scanCode, int modifiers) {
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyRelease(int keyCode, int scanCode, int modifiers) {
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean typeChar(char chr, int modifiers) {
		return super.charTyped(chr, modifiers);
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
	public void onRemoved() {
		
	}
	
	@Override
	public void focus() {
		
	}
	
	@Override
	public void unfocus() {
		
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public void setX(int x) {
		this.x = x;
	}
	
	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	@Override
	public List<Text> getTooltip(int mouseX, int mouseY) {
		return tooltipSupplier.get();
	}
	
	@Override
	public void updateMessage() {
		snap();
		setMessage(textSupplier.get());
	}
	
	@Override
	public void update() {
		setValue(valueSupplier.get());
		updateMessage();
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double newValue) {
		value = newValue;
	}
	
	public void snap() {
		snap.accept(value);
	}
	
	public interface SlideAction {
		void onSlide(Slider slider);
	}
}