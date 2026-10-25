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
package wtf.cheeze.smkb.preset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import wtf.cheeze.smkb.SaveMyKeybinds;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class PresetManager {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static int savePreset(String name) {
        try {
            Preset preset = new Preset();
            Path presetFile = SaveMyKeybinds.FOLDER_PATH.resolve(name + ".preset.json");
            FileWriter writer = new FileWriter(presetFile.toFile());
            writer.write(GSON.toJson(preset));
            writer.close();
            SaveMyKeybinds.sendToast(Component.translatable("smkb.toast.save.success.title").withStyle(ChatFormatting.GREEN), Component.translatable("smkb.toast.save.success.body", name));
        } catch (Exception e) {
            SaveMyKeybinds.LOGGER.error("Failed to write preset file", e);
            SaveMyKeybinds.sendToast(Component.translatable("smkb.toast.save.failure.title").withStyle(ChatFormatting.RED), Component.translatable("smkb.toast.save.failure.body", name));
            return 0;
        }
        return 1;
    }

    public static int loadPreset(String name) {
        try {
            Path presetFile = SaveMyKeybinds.FOLDER_PATH.resolve(name + ".preset.json");
            String content = Files.readString(presetFile);
            Preset preset = GSON.fromJson(content, Preset.class);
            Minecraft minecraft = Minecraft.getInstance();
            for (KeyMapping keybinding: minecraft.options.keyMappings) {
                var presetBinding = preset.keybinds.get(SaveMyKeybinds.getTranslationKey(keybinding));
                if (presetBinding != null) {
                    keybinding.setKey(InputConstants.getKey(presetBinding.key));
                } else if (preset.strict) {
                    keybinding.setKey(InputConstants.UNKNOWN);
                }
            }
            // Apply the new keys right away and keep them after a restart
            KeyMapping.resetMapping();
            minecraft.options.save();
        SaveMyKeybinds.sendToast(Component.translatable("smkb.toast.load.success.title").withStyle(ChatFormatting.GREEN), Component.translatable("smkb.toast.load.success.body", name));
        } catch (Exception e) {
            SaveMyKeybinds.LOGGER.error("Failed to read preset file", e);
            SaveMyKeybinds.sendToast(Component.translatable("smkb.toast.load.failure.title").withStyle(ChatFormatting.RED), Component.translatable("smkb.toast.load.failure.body", name));
            return 0;
        }
        return 1;
    }

    public static int deletePreset(String name) {
        try {
            Path presetFile = SaveMyKeybinds.FOLDER_PATH.resolve(name + ".preset.json");
            Files.delete(presetFile);
            SaveMyKeybinds.sendToast(Component.translatable("smkb.toast.delete.success.title").withStyle(ChatFormatting.GREEN), Component.translatable("smkb.toast.delete.success.body", name));
        } catch (Exception e) {
            SaveMyKeybinds.LOGGER.error("Failed to delete preset file", e);
            SaveMyKeybinds.sendToast(Component.translatable("smkb.toast.delete.failure.title").withStyle(ChatFormatting.RED), Component.translatable("smkb.toast.delete.failure.body", name));
            return 0;
        }
        return 1;
    }

    public static String[] getPresets() {
        return SaveMyKeybinds.FOLDER_PATH.toFile().list((dir, name) -> name.endsWith(".preset.json"));
    }
}
