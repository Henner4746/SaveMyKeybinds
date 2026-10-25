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

import com.blamejared.controlling.client.NewKeyBindsScreen;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.CommonComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.cheeze.smkb.screen.LoadPresetScreen;
import wtf.cheeze.smkb.screen.SavePresetScreen;

@IfModLoaded("controlling")
@Mixin(NewKeyBindsScreen.class)
public abstract class ControllingKeybindsScreenMixin extends KeyBindsScreen {

    private ControllingKeybindsScreenMixin(Screen a, Options b) {
        super(a, b);
    }

    @Shadow public abstract Button resetButton();

    // Replaces the last footer row (reset, done) so the preset buttons fit in
    @Inject(method = "addFooter", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 2))
    public void addFooter(CallbackInfo ci, @Local GridLayout.RowHelper adder) {
        ci.cancel();

        var bottomLeft = adder.addChild(LinearLayout.horizontal());
        bottomLeft.spacing(4);
        bottomLeft.addChild(SavePresetScreen.getButton());
        bottomLeft.addChild(LoadPresetScreen.getButton());

        var bottomRight = adder.addChild(LinearLayout.horizontal());
        bottomRight.spacing(4);

        var reset = this.resetButton();
        reset.setWidth(74);
        bottomRight.addChild(reset);

        var done = Button.builder(CommonComponents.GUI_DONE, button -> {
            this.onClose();
        }).build();
        done.setWidth(74);
        bottomRight.addChild(done);
    }
}
