/*
 * Copyright (C) 2024–2026 MisterCheezeCake
 *
 * This file is part of SaveMyKeybinds.
 *
 * SaveMyKeybinds is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * SaveMyKeybinds is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SaveMyKeybinds. If not, see <https://www.gnu.org/licenses/>.
 */
package wtf.cheeze.smkb.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public abstract class SMKBScreen extends Screen {
    private final Screen parent;

    public SMKBScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().gui.setScreen(parent);
    }

    // Widgets are added here because init() clears everything added before
    @Override
    public void init() {
        this.addRenderableWidget(new CenteredTextWidget(title, 10));
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (b) -> {
            this.onClose();
        }).bounds(centerX() - 100, (int) (screenHeight() * 0.9), 200, 20).build());
    }

    public static int centerX() {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2;
    }

    public static int screenHeight() {
        return Minecraft.getInstance().getWindow().getGuiScaledHeight();
    }
}
