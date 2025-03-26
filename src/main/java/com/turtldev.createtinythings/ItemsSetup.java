package com.turtldev.createtinythings;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;
import net.minecraft.world.item.crafting.ShapedRecipeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;


public class ItemsSetup {

    public static final String MODID = "create_tinythings";

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // New Compressed Chocolate Block
    public static final DeferredBlock<Block> COMPRESSED_CHOCOLATE = BLOCKS.registerSimpleBlock("compressed_chocolate", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN));
    public static final DeferredItem<BlockItem> COMPRESSED_CHOCOLATE_ITEM = ITEMS.registerSimpleBlockItem("compressed_chocolate", COMPRESSED_CHOCOLATE);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.create_tinythings"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get());
                output.accept(COMPRESSED_CHOCOLATE_ITEM.get()); // Add Compressed Chocolate item
            })
            .build());

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
            event.accept(COMPRESSED_CHOCOLATE_ITEM); // Add Compressed Chocolate item to the building blocks tab
        }
    }

    public static void register(final IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(ItemsSetup::addCreative);
        registerRecipes();
    }

    // Register Crafting Recipe for Compressed Chocolate
    public static void registerRecipes() {
        ShapedRecipeBuilder.shaped(COMPRESSED_CHOCOLATE.get())
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .define('#', Registry.ITEM.get(new ResourceLocation("create", "bar_of_chocolate")))
            .unlockedBy("has_chocolate_bar", InventoryChangeTrigger.TriggerInstance.hasItems(Registry.ITEM.get(new ResourceLocation("create", "bar_of_chocolate"))))
            .save(RecipeBuilder.simple(modid("compressed_chocolate")));
    }
}
