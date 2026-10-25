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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import wtf.cheeze.smkb.preset.PresetManager;

public class ListWidget extends ObjectSelectionList<ListWidget.Entry> {

    public ListWidget(Minecraft minecraft, int width, int height, int top, int entryHeight) {
        super(minecraft, width, height, top, entryHeight);
        this.reload();
    }

    public void reload() {
        clearEntries();
        for (var preset : PresetManager.getPresets()) {
            this.addEntry(new Entry(preset.split("\\.")[0]));
        }
    }

    public static class Entry extends ObjectSelectionList.Entry<Entry> {
        public final String text;

        public Entry(String text) {
            super();
            this.text = text;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            graphics.centeredText(
                    Minecraft.getInstance().font,
                    text,
                    SMKBScreen.centerX(),
                    getY() + 3,
                    !hovered && !isFocused() ? 0xFFC0C0C0: 0xFFFFFFFF
            );
        }

        @Override
        public Component getNarration() {
            return Component.literal(text);
        }
    }
}
