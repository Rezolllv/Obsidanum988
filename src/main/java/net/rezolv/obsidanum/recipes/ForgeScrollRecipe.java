package net.rezolv.obsidanum.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.rezolv.obsidanum.Obsidanum;

import javax.annotation.Nullable;

public class ForgeScrollRecipe implements Recipe<SimpleContainer> {

    private final NonNullList<ItemStack> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    public ForgeScrollRecipe(NonNullList<ItemStack> inputItems, ItemStack output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
    }
    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    public static class Type implements RecipeType<ForgeScrollRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "forge_scroll";
    }
    public static class Serializer implements RecipeSerializer<ForgeScrollRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Obsidanum.MOD_ID, "forge_scroll");

        @Override
        public ForgeScrollRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(serializedRecipe, "ingredients");
            NonNullList<ItemStack> inputs = NonNullList.create();

            for (int i = 0; i < ingredients.size(); i++) {
                JsonObject ingredientObject = ingredients.get(i).getAsJsonObject();
                ItemStack itemStack = ShapedRecipe.itemStackFromJson(ingredientObject);
                inputs.add(itemStack);
            }

            return new ForgeScrollRecipe(inputs, output, recipeId);
        }

        @Override
        public @Nullable ForgeScrollRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int inputSize = buffer.readInt();
            NonNullList<ItemStack> inputs = NonNullList.withSize(inputSize, ItemStack.EMPTY);

            for (int i = 0; i < inputSize; i++) {
                inputs.set(i, buffer.readItem());
            }

            ItemStack output = buffer.readItem();
            return new ForgeScrollRecipe(inputs, output, recipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ForgeScrollRecipe recipe) {
            buffer.writeInt(recipe.inputItems.size());

            for (ItemStack stack : recipe.inputItems) {
                buffer.writeItemStack(stack, false);
            }

            buffer.writeItemStack(recipe.output, false);
        }
    }
}
