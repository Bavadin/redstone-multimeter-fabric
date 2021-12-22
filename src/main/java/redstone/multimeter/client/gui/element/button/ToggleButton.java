package redstone.multimeter.client.gui.element.button;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import redstone.multimeter.client.MultimeterClient;

public class ToggleButton extends Button {
	
	public ToggleButton(MultimeterClient client, int x, int y, int width, int height, Supplier<Boolean> getter, Consumer<Button> toggle) {
		super(client, x, y, width, height, () -> {
			Boolean value = getter.get();
			Formatting color = value ? Formatting.GREEN : Formatting.RED;
			
			return new LiteralText(value.toString()).setStyle(new Style().setFormatting(color));
		}, () -> null, button -> { toggle.accept(button); return true; });
	}
}
