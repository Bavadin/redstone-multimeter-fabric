package redstone.multimeter.client.gui.element.tutorial;

import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.gui.toast.ToastGui;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import redstone.multimeter.client.tutorial.instance.StagedTutorialInstance;

public class StagedTutorialToast extends TutorialToast {

	private final StagedTutorialInstance tutorial;

	private float lastProgress;
	private float progress;
	private long ageAtLastProgressUpdate;

	public StagedTutorialToast(StagedTutorialInstance tutorial, Text title, Text description) {
		super(title, description);

		this.tutorial = tutorial;
	}

	@Override
	protected void drawDecoration(ToastGui toasts, long animationTime) {
		float newProgress = tutorial.getProgress();

		if (newProgress != progress) {
			lastProgress = progress;
			progress = newProgress;
			ageAtLastProgressUpdate = animationTime;
		}

		int width = width();
		int height = height();
		int edge = EDGE - 1;

		float renderProgress = (float)MathHelper.clampedLerp(lastProgress, progress, (animationTime - ageAtLastProgressUpdate) / 100.0F);

		int x = edge;
		int y = height - edge;
		int w = width - 2 * edge;
		int w2 = (int)(w * renderProgress);
		int h = 1;

		GuiElement.fill(x, y, x + w, y + h, 0xFFFFFFFF);
		GuiElement.fill(x, y, x + w2, y + h, 0xFF500050);
	}
}
