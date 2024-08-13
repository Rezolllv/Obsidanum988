package net.rezolv.obsidanum.entity;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.entity.meat_beetle.MeetBeetleModel;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElementalModel;

public class ModModelLayers {
    public static final ModelLayerLocation OBSIDIAN_ELEMENTAL = new ModelLayerLocation(
            new ResourceLocation("obsidanum", "obsidian_elemental"), "main");

    public static void registerLayersElemental(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(OBSIDIAN_ELEMENTAL, ObsidianElementalModel::createBodyLayer);
    }
    public static final ModelLayerLocation MEET_BEETLE = new ModelLayerLocation(
            new ResourceLocation("obsidanum", "meet_beetle"), "main");

    public static void registerLayersBeetle(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MEET_BEETLE, MeetBeetleModel::createBodyLayer);
    }
}
