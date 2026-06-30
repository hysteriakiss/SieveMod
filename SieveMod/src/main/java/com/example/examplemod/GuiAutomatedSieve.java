package com.example.examplemod;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiAutomatedSieve extends GuiContainer {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(SieveMod.MODID, "textures/gui/automated_sieve.png");
    private final TileAutomatedSieve te;
    private boolean showRecipes = false;
    private int recipeScroll = 0;
    private int maxScroll = 0;
    private static final int RECIPES_PER_PAGE = 4;

    private static final List<RecipeEntry> recipeEntries = new ArrayList<>();

    static {
        addRecipe(Blocks.DIRT, new ItemStack(Items.DIAMOND), 5, new ItemStack(Items.IRON_INGOT), 15,
                new ItemStack(Items.GOLD_NUGGET, 2), 25, new ItemStack(Items.COAL), 45);
        addRecipe(Blocks.GRAVEL, new ItemStack(Items.FLINT), 40, new ItemStack(Items.IRON_NUGGET, 2), 25,
                new ItemStack(Items.GOLD_NUGGET), 15, new ItemStack(Items.COAL), 15);
        addRecipe(Blocks.SAND, new ItemStack(Items.DYE, 2, 4), 35, new ItemStack(Items.BONE), 25,
                new ItemStack(Items.CLAY_BALL, 2), 20, new ItemStack(Items.GOLD_NUGGET), 10);
        addRecipe(Blocks.SOUL_SAND, new ItemStack(Items.QUARTZ, 2), 35, new ItemStack(Items.NETHER_WART), 25,
                new ItemStack(Items.BLAZE_POWDER), 20, new ItemStack(Items.GHAST_TEAR), 10);
    }

    private static void addRecipe(net.minecraft.block.Block input,
                                  ItemStack o1, int c1, ItemStack o2, int c2,
                                  ItemStack o3, int c3, ItemStack o4, int c4) {
        recipeEntries.add(new RecipeEntry(new ItemStack(input),
                new ItemStack[]{o1, o2, o3, o4},
                new int[]{c1, c2, c3, c4}));
    }

    public GuiAutomatedSieve(InventoryPlayer playerInv, TileAutomatedSieve te) {
        super(new ContainerAutomatedSieve(playerInv, te));
        this.te = te;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void initGui() {
        super.initGui();
        int btnX = guiLeft + xSize - 18;
        int btnY = guiTop + 4;
        this.buttonList.add(new GuiButton(0, btnX, btnY, 14, 14, "\u2630"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            showRecipes = !showRecipes;
            recipeScroll = 0;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (showRecipes) {
            drawRecipeOverlay(mouseX, mouseY);
        }

        this.renderHoveredToolTip(mouseX, mouseY);
    }

    private int getOverlayW() { return 150; }
    private int getOverlayH() { return 154; }

    private int getOverlayX() { return guiLeft - getOverlayW() - 6; }
    private int getOverlayY() { return guiTop; }

    private void drawRecipeOverlay(int mouseX, int mouseY) {
        int overlayW = getOverlayW();
        int overlayH = getOverlayH();
        int ox = getOverlayX();
        int oy = getOverlayY();

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 200);

        drawRect(ox - 6, oy - 6, ox + overlayW + 6, oy + overlayH + 6, 0xCC000000);

        drawRect(ox - 2, oy - 2, ox + overlayW + 2, oy + overlayH + 2, 0xFF222222);
        drawRect(ox, oy, ox + overlayW, oy + overlayH, 0xFF1A1A2E);

        this.fontRenderer.drawStringWithShadow("\u00A7e\u00A7lSifting Recipes", ox + 4, oy + 4, 0xFFFFFF);

        String closeText = "\u00A7c[X]";
        int closeX = ox + overlayW - this.fontRenderer.getStringWidth("[X]") - 6;
        this.fontRenderer.drawStringWithShadow(closeText, closeX, oy + 4, 0xFFFF5555);

        maxScroll = Math.max(0, recipeEntries.size() - RECIPES_PER_PAGE);

        int startY = oy + 18;
        for (int i = 0; i < RECIPES_PER_PAGE && (i + recipeScroll) < recipeEntries.size(); i++) {
            RecipeEntry entry = recipeEntries.get(i + recipeScroll);
            int ry = startY + i * 30;

            drawRect(ox + 2, ry, ox + overlayW - 2, ry + 28, 0xFF252540);
            drawHorizontalLine(ox + 2, ox + overlayW - 3, ry, 0xFF444466);
            drawHorizontalLine(ox + 2, ox + overlayW - 3, ry + 27, 0xFF444466);

            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableDepth();
            this.itemRender.zLevel = 200.0F;

            this.itemRender.renderItemAndEffectIntoGUI(entry.input, ox + 6, ry + 4);
            this.itemRender.renderItemOverlays(this.fontRenderer, entry.input, ox + 6, ry + 4);

            this.fontRenderer.drawString("\u2192", ox + 26, ry + 10, 0xFFAAAAAA);

            int itemX = ox + 38;
            for (int j = 0; j < entry.outputs.length; j++) {
                ItemStack out = entry.outputs[j];
                int chance = entry.chances[j];

                this.itemRender.renderItemAndEffectIntoGUI(out, itemX, ry + 4);
                this.itemRender.renderItemOverlays(this.fontRenderer, out, itemX, ry + 4);

                String chanceText = chance + "%";
                GlStateManager.disableLighting();
                int textW = this.fontRenderer.getStringWidth(chanceText);
                this.fontRenderer.drawStringWithShadow(chanceText, itemX + 8 - textW / 2, ry + 20, 0xFF88CCFF);

                itemX += 26;
            }

            this.itemRender.zLevel = 0.0F;
            RenderHelper.disableStandardItemLighting();
        }

        if (maxScroll > 0) {
            String scrollText = "\u00A77" + (recipeScroll + 1) + "/" + (maxScroll + 1);
            int scrollPrevX = ox + 4;
            int scrollNextX = ox + overlayW - 20;
            int scrollY = oy + overlayH - 14;

            this.fontRenderer.drawStringWithShadow("\u00A7a<", scrollPrevX, scrollY, 0xFF55FF55);
            this.fontRenderer.drawStringWithShadow(scrollText, ox + overlayW / 2 - this.fontRenderer.getStringWidth(scrollText) / 2, scrollY, 0xFFAAAAAA);
            this.fontRenderer.drawStringWithShadow("\u00A7a>", scrollNextX, scrollY, 0xFF55FF55);
        }

        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (showRecipes && mouseButton == 0) {
            int overlayW = getOverlayW();
            int overlayH = getOverlayH();
            int ox = getOverlayX();
            int oy = getOverlayY();

            int closeX = ox + overlayW - this.fontRenderer.getStringWidth("[X]") - 6;
            if (mouseX >= closeX && mouseX <= closeX + this.fontRenderer.getStringWidth("[X]")
                    && mouseY >= oy + 4 && mouseY <= oy + 14) {
                showRecipes = false;
                return;
            }

            int scrollY = oy + overlayH - 14;
            if (mouseY >= scrollY && mouseY <= scrollY + 12) {
                if (mouseX < ox + overlayW / 2 && recipeScroll > 0) {
                    recipeScroll--;
                } else if (mouseX >= ox + overlayW / 2 && recipeScroll < maxScroll) {
                    recipeScroll++;
                }
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (showRecipes) {
            int d = org.lwjgl.input.Mouse.getEventDWheel();
            if (d != 0) {
                if (d > 0 && recipeScroll > 0) {
                    recipeScroll--;
                } else if (d < 0 && recipeScroll < maxScroll) {
                    recipeScroll++;
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawModalRectWithCustomSizedTexture(k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);

        ContainerAutomatedSieve c = (ContainerAutomatedSieve) this.inventorySlots;

        int pX = k + 74;
        int pY = l + 32;
        int pW = 24;
        int pH = 16;

        drawRect(pX, pY, pX + pW, pY + pH, 0xFF1A3D1A);

        float progress = (float) c.getProgress() / (float) c.getMaxProgress();
        int fillW = (int) (progress * (pW - 2));
        if (fillW > 0) {
            drawGradientRect(pX + 1, pY + 1, pX + 1 + fillW, pY + pH - 1,
                    0xFF44CC44, 0xFF228822);
            drawRect(pX + 1, pY + 1, pX + 1 + fillW, pY + 3, 0x55FFFFFF);
        }

        for (int i = 1; i <= 3; i++) {
            int tickX = pX + 1 + (int) (i * (pW - 2) / 4.0f);
            drawVerticalLine(tickX, pY + 1, pY + pH - 2, 0x33FFFFFF);
        }

        drawHorizontalLine(pX, pX + pW - 1, pY, 0xFF555555);
        drawHorizontalLine(pX, pX + pW - 1, pY + pH - 1, 0xFF555555);
        drawVerticalLine(pX, pY, pY + pH - 1, 0xFF555555);
        drawVerticalLine(pX + pW - 1, pY, pY + pH - 1, 0xFF555555);

        int eX = k + 8;
        int eY = l + 8;
        int eW = 10;
        int eH = 50;

        drawRect(eX, eY, eX + eW, eY + eH, 0xFF3D2A00);

        float energy = (float) c.getEnergy() / (float) c.getMaxEnergy();
        int fillH = (int) (energy * (eH - 2));
        if (fillH > 0) {
            int top = eY + 1 + (eH - 2 - fillH);
            int bottom = eY + eH - 1;
            drawGradientRect(eX + 1, top, eX + eW - 1, bottom,
                    0xFFFFDD33, 0xFFCC9900);
            drawRect(eX + 1, top, eX + eW - 1, top + 2, 0x44FFFFFF);
            drawRect(eX + 1, bottom - 2, eX + eW - 1, bottom, 0x33000000);
        }

        for (int i = 1; i <= 9; i++) {
            int tickY = eY + 1 + (int) (i * (eH - 2) / 10.0f);
            boolean major = (i % 2 == 0);
            if (major) {
                drawHorizontalLine(eX, eX + eW - 1, tickY, 0xAAFFFFFF);
            } else {
                drawHorizontalLine(eX + 1, eX + eW / 2, tickY, 0x55FFFFFF);
            }
        }

        drawHorizontalLine(eX, eX + eW - 1, eY, 0xFF555555);
        drawHorizontalLine(eX, eX + eW - 1, eY + eH - 1, 0xFF555555);
        drawVerticalLine(eX, eY, eY + eH - 1, 0xFF555555);
        drawVerticalLine(eX + eW - 1, eY, eY + eH - 1, 0xFF555555);

        this.fontRenderer.drawString("E", eX + (eW - 5) / 2, eY + eH + 2, 0xFFAAAAAA);
    }

    private static class RecipeEntry {
        final ItemStack input;
        final ItemStack[] outputs;
        final int[] chances;

        RecipeEntry(ItemStack input, ItemStack[] outputs, int[] chances) {
            this.input = input;
            this.outputs = outputs;
            this.chances = chances;
        }
    }
}
