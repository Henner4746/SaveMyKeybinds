package wtf.cheeze.smkb.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

public class CenteredTextWidget extends StringWidget {
    public CenteredTextWidget(Component message, int x, int y) {
        super(x - Minecraft.getInstance().font.width(message) / 2, y, Minecraft.getInstance().font.width(message), 9, message, Minecraft.getInstance().font);
    }

    public CenteredTextWidget(Component message, int y) {
        this(message, SMKBScreen.centerX(), y);
    }
}
