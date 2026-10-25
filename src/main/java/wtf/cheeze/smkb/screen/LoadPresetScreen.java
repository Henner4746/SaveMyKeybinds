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

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import wtf.cheeze.smkb.mixin.ControllingKeybindsScreenAccessor;
import wtf.cheeze.smkb.mixin.KeybindsScreenAccessor;
import wtf.cheeze.smkb.preset.PresetManager;

public class LoadPresetScreen extends SMKBScreen {
    private ListWidget listWidget;
    private Button deleteButton;
    private Button loadButton;

    public LoadPresetScreen(Screen parent) {
        super(Component.translatable("smkb.screen.loadPresets"), parent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        deleteButton.active = listWidget.getSelected() != null;
        loadButton.active = listWidget.getSelected() != null;
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void init() {
        super.init();
        this.listWidget = new ListWidget(
                Minecraft.getInstance(),
                Minecraft.getInstance().getWindow().getGuiScaledWidth(),
                (int) (SMKBScreen.screenHeight() * 0.75f) - 25, 25, 20);
        int h = (int) (SMKBScreen.screenHeight() * 0.8f);
        this.deleteButton = Button.builder(Component.translatable("smkb.screen.delete"), button -> {
            var t = listWidget.getSelected();
            if (t == null) return;
            PresetManager.deletePreset(t.text);
            listWidget.reload();
        }).bounds(centerX() - 160 ,h,150,20).build();
        this.loadButton = Button.builder(Component.translatable("smkb.screen.load"), button -> {
            var t = listWidget.getSelected();
            if (t == null) return;
            if (PresetManager.loadPreset(t.text) != 0 ) onClose();
            Screen current = Minecraft.getInstance().gui.screen();
            // Another mod can still open the vanilla screen while Controlling is installed
            if (FabricLoader.getInstance().isModLoaded("controlling") && current instanceof com.blamejared.controlling.client.NewKeyBindsScreen) {
                ((ControllingKeybindsScreenAccessor) current).getNewKeyList().get().resetMappingAndUpdateButtons();
            } else if (current instanceof KeyBindsScreen) {
                ((KeybindsScreenAccessor) current).getKeyBindsList().resetMappingAndUpdateButtons();
            }
        }).bounds(centerX() + 10, h, 150, 20).build();
        this.addRenderableWidget(listWidget);
        this.addRenderableWidget(deleteButton);
        this.addRenderableWidget(loadButton);
    }

    public static Button getButton() {
        return Button.builder(Component.translatable("smkb.screen.loadPresets"), button -> {
            Minecraft.getInstance().gui.setScreen(new LoadPresetScreen(Minecraft.getInstance().gui.screen()));
        }).width(74).build();
    }
}
