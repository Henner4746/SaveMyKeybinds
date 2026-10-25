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

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.intellij.lang.annotations.Language;
import wtf.cheeze.smkb.SaveMyKeybinds;
import wtf.cheeze.smkb.preset.PresetManager;

import java.util.regex.Pattern;

public class SavePresetScreen extends SMKBScreen {

    @Language("RegExp")
    public final static String[] FORBIDDEN_CHARS = {"\\\\", "/", ":", "<", ">", "\"", "\\|", "\\?", "\\*"};
    public static final Pattern FORBIDDEN_CHARS_PATTERN = Pattern.compile(String.join("|", FORBIDDEN_CHARS));

    public SavePresetScreen(Screen parent) {
        super(Component.translatable("smkb.screen.savePresets"), parent);
    }

    @Override
    public void init() {
        super.init();
        EditBox nameWidget = new EditBox(Minecraft.getInstance().font, centerX() - 150, 40, 300, 20, Component.literal("Preset"));
        Button saveWidget = Button.builder(Component.literal("Save"), (button) -> {
            var t = nameWidget.getValue();
            if (t.isBlank()) {
                SaveMyKeybinds.sendToast(Component.translatable("smkb.toast.invalid.empty.title").withStyle(ChatFormatting.RED), Component.translatable("smkb.toast.invalid.empty.body"));
                return;
            }
            if (FORBIDDEN_CHARS_PATTERN.matcher(t).find()) {
                SaveMyKeybinds.sendToast(Component.translatable("smkb.toast.invalid.special.title").withStyle(ChatFormatting.RED), Component.translatable("smkb.toast.invalid.special.body"));
                return;
            }
            if (PresetManager.savePreset(t) != 0) {
                onClose();
            }
        }).bounds(centerX() - 100, 70, 200, 20).build();
        this.addRenderableWidget(new CenteredTextWidget(Component.translatable("smkb.screen.enterName"), 27));
        this.addRenderableWidget(nameWidget);
        this.addRenderableWidget(saveWidget);
    }

    public static Button getButton() {
        return Button.builder(Component.translatable("smkb.screen.savePresets"), button -> {
            Minecraft.getInstance().gui.setScreen(new SavePresetScreen(Minecraft.getInstance().gui.screen()));
        }).width(74).build();
    }
}
