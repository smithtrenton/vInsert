package org.vinsert.api;

import com.google.inject.AbstractModule;
import org.vinsert.api.impl.LoggerImpl;
import org.vinsert.api.impl.game.*;
import org.vinsert.api.impl.scene.LootsImpl;
import org.vinsert.api.impl.scene.NpcsImpl;
import org.vinsert.api.impl.scene.ObjectsImpl;
import org.vinsert.api.impl.scene.PlayersImpl;
import org.vinsert.api.impl.tabs.*;
import org.vinsert.core.input.impl.KeyboardImpl;
import org.vinsert.core.input.impl.MouseImpl;

/**
 * Dependency bindings for the API
 */
public final class APIModule extends AbstractModule {
    private MethodContext context;

    @Override
    @SuppressWarnings("unchecked")
    protected void configure() {
        bind(Logger.class).to(checkBinding(Logger.class, LoggerImpl.class)).asEagerSingleton();
        bind(Viewport.class).to(checkBinding(Viewport.class, ViewportImpl.class)).asEagerSingleton();
        bind(Widgets.class).to(checkBinding(Widgets.class, WidgetsImpl.class)).asEagerSingleton();
        bind(Npcs.class).to(checkBinding(Npcs.class, NpcsImpl.class)).asEagerSingleton();
        bind(Worlds.class).to(checkBinding(Worlds.class, WorldsImpl.class)).asEagerSingleton();
        bind(Objects.class).to(checkBinding(Objects.class, ObjectsImpl.class)).asEagerSingleton();
        bind(Players.class).to(checkBinding(Players.class, PlayersImpl.class)).asEagerSingleton();
        bind(Loots.class).to(checkBinding(Loots.class, LootsImpl.class)).asEagerSingleton();
        bind(Menu.class).to(checkBinding(Menu.class, MenuImpl.class)).asEagerSingleton();
        bind(Inventory.class).to(checkBinding(Inventory.class, InventoryImpl.class)).asEagerSingleton();
        bind(Equipment.class).to(checkBinding(Equipment.class, EquipmentImpl.class)).asEagerSingleton();
        bind(Skills.class).to(checkBinding(Skills.class, SkillsImpl.class)).asEagerSingleton();
        bind(Tabs.class).to(checkBinding(Tabs.class, TabsImpl.class)).asEagerSingleton();
        bind(Camera.class).to(checkBinding(Camera.class, CameraImpl.class)).asEagerSingleton();
        bind(Bank.class).to(checkBinding(Bank.class, BankImpl.class)).asEagerSingleton();
        bind(Magic.class).to(checkBinding(Magic.class, MagicImpl.class)).asEagerSingleton();
        bind(Prayers.class).to(checkBinding(Prayers.class, PrayersImpl.class)).asEagerSingleton();
        bind(Minimap.class).to(checkBinding(Minimap.class, MinimapImpl.class)).asEagerSingleton();
        bind(Mouse.class).to(checkBinding(Mouse.class, MouseImpl.class)).asEagerSingleton();
        bind(Keyboard.class).to(checkBinding(Keyboard.class, KeyboardImpl.class)).asEagerSingleton();
        bind(Settings.class).to(checkBinding(Settings.class, SettingsImpl.class)).asEagerSingleton();
        bind(Walking.class).to(checkBinding(Walking.class, WalkingImpl.class)).asEagerSingleton();
        bind(Shops.class).to(checkBinding(Shops.class, ShopsImpl.class)).asEagerSingleton();
        bind(Screen.class).to(checkBinding(Screen.class, ScreenImpl.class)).asEagerSingleton();
        bind(Composites.class).to(checkBinding(Composites.class, CompositesImpl.class)).asEagerSingleton();
        bind(Models.class).to(checkBinding(Models.class, ModelsImpl.class)).asEagerSingleton();
        bind(MethodContext.class).toInstance(context);
    }

    public Class checkBinding(Class bindable, Class standard) {
        return standard;
    }

    public void setContext(MethodContext context) {
        this.context = context;
    }
}