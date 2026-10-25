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
 * SkyblockTweaks is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SaveMyKeybinds. If not, see <https://www.gnu.org/licenses/>.
 */
package wtf.cheeze.smkb;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class SaveMyKeybinds implements ModInitializer {
	public static final String MOD_ID = "SaveMyKeybinds";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Path FOLDER_PATH = FabricLoader.getInstance().getConfigDir().resolve("savemykeybinds");

	@Override
	public void onInitialize() {
		if (!FOLDER_PATH.toFile().exists()) {
			try {
				FOLDER_PATH.toFile().mkdir();
			} catch (Exception e) {
				LOGGER.error("Failed to create folder for SaveMyKeybinds", e);
			}
		}
	}

	public static void sendToast(Component title, Component message) {
		SystemToast.add(Minecraft.getInstance().gui.toastManager(), SystemToast.SystemToastId.PERIODIC_NOTIFICATION, title, message);
	}

	public static String getTranslationKey(KeyMapping bind) {
		return bind.getName();
	}
}
