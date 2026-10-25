/*
 * Copyright (C) 2024 MisterCheezeCake
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
package wtf.cheeze.smkb.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.cheeze.smkb.screen.LoadPresetScreen;
import wtf.cheeze.smkb.screen.SavePresetScreen;

@Mixin(KeyBindsScreen.class)
public abstract class KeybindsScreenMixin {

	// Between "Reset Keys" and "Done"
	@Inject(method = "addFooter", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/LinearLayout;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 1))
	private void addCustomButton(CallbackInfo ci, @Local LinearLayout widget) {
		widget.addChild(SavePresetScreen.getButton());
		widget.addChild(LoadPresetScreen.getButton());
	}

	@Inject(method = "addFooter", at = @At("TAIL"))
	private void addCustomButton2(CallbackInfo ci, @Local LinearLayout widget) {
		widget.visitWidgets(button -> button.setWidth(100));
	}
}
