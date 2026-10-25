package wtf.cheeze.smkb.test;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import wtf.cheeze.smkb.SaveMyKeybinds;
import wtf.cheeze.smkb.preset.PresetManager;
import wtf.cheeze.smkb.screen.ListWidget;
import wtf.cheeze.smkb.screen.LoadPresetScreen;
import wtf.cheeze.smkb.screen.SavePresetScreen;

import java.nio.file.Files;

/**
 * Saves a preset, rebinds jump and loads the preset again, first directly and then through the buttons.
 * Screenshots end up in build/run/clientGameTest/screenshots for a visual check.
 */
public class SaveMyKeybindsClientTest implements FabricClientGameTest {
    private static final String PRESET = "gametest";
    private static final String DEFAULT_JUMP = "key.keyboard.space";
    private static final String OTHER_KEY = "key.keyboard.j";

    @Override
    public void runTest(ClientGameTestContext context) {
        testSaveAndLoadPreset(context);
        testPresetScreens(context);
    }

    private static void testSaveAndLoadPreset(ClientGameTestContext context) {
        context.runOnClient(client -> {
            KeyMapping jump = client.options.keyJump;
            check(jump.saveString().equals(DEFAULT_JUMP), "unexpected default jump key " + jump.saveString());
            check(PresetManager.savePreset(PRESET) == 1, "saving the preset failed");
            check(Files.isRegularFile(SaveMyKeybinds.FOLDER_PATH.resolve(PRESET + ".preset.json")), "the preset file was not written");

            setKey(jump, OTHER_KEY);
            check(PresetManager.loadPreset(PRESET) == 1, "loading the preset failed");
            check(jump.saveString().equals(DEFAULT_JUMP), "loading did not restore the jump key, got " + jump.saveString());
        });
    }

    private static void testPresetScreens(ClientGameTestContext context) {
        String variant = context.computeOnClient(client -> FabricLoader.getInstance().isModLoaded("controlling")) ? "controlling" : "vanilla";
        context.runOnClient(client -> setKey(client.options.keyJump, OTHER_KEY));

        // Controlling swaps this for its own screen when it is installed
        context.setScreen(() -> new KeyBindsScreen(new TitleScreen(), Minecraft.getInstance().options));
        context.waitTicks(2);
        context.takeScreenshot("smkb-" + variant + "-keybinds");

        context.clickScreenButton("smkb.screen.savePresets");
        context.waitFor(client -> client.gui.screen() instanceof SavePresetScreen);
        context.runOnClient(client -> findChild(client.gui.screen(), EditBox.class).setValue("ui"));
        context.takeScreenshot("smkb-" + variant + "-save");
        context.clickScreenButton("Save");
        context.waitFor(client -> client.gui.screen() instanceof KeyBindsScreen);
        check(Files.isRegularFile(SaveMyKeybinds.FOLDER_PATH.resolve("ui.preset.json")), "the save button did not write a preset");

        context.clickScreenButton("smkb.screen.loadPresets");
        context.waitFor(client -> client.gui.screen() instanceof LoadPresetScreen);
        context.runOnClient(client -> {
            ListWidget list = findChild(client.gui.screen(), ListWidget.class);
            list.setSelected(list.children().stream()
                    .filter(entry -> entry.text.equals(PRESET))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("the preset is missing from the list")));
        });
        context.waitTicks(2);
        context.takeScreenshot("smkb-" + variant + "-load");

        context.clickScreenButton("smkb.screen.load");
        context.waitFor(client -> client.gui.screen() instanceof KeyBindsScreen);
        context.waitTicks(2);
        check(context.computeOnClient(client -> client.options.keyJump.saveString()).equals(DEFAULT_JUMP), "the load button did not restore the jump key");
        context.takeScreenshot("smkb-" + variant + "-after-load");

        context.setScreen(TitleScreen::new);
    }

    private static void setKey(KeyMapping mapping, String key) {
        mapping.setKey(InputConstants.getKey(key));
        KeyMapping.resetMapping();
    }

    private static <T> T findChild(Screen screen, Class<T> type) {
        for (GuiEventListener child : screen.children()) {
            if (type.isInstance(child)) return type.cast(child);
        }
        throw new AssertionError("no " + type.getSimpleName() + " on " + screen);
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
