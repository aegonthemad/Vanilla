/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.component.player;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.gui.Screen;
import org.spout.api.gui.Widget;
import org.spout.api.gui.component.LabelComponent;
import org.spout.api.gui.component.RenderPartsHolderComponent;
import org.spout.api.gui.component.TexturedRectComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.math.Rectangle;
import org.spout.api.plugin.Platform;
import org.spout.api.render.Font;
import org.spout.api.render.RenderMaterial;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.data.VanillaData;

/**
 * Component attached to clients-only that updates the Heads Up Display.
 */
public class HUDComponent extends EntityComponent {
	private static final float SCALE = 0.75f; // TODO: Apply directly from engine
	private static final float START_X = -0.71f * SCALE;

	// The main HUD screen
	private final Screen HUD = new Screen();

	// The materials used to construct the render
	private final RenderMaterial hotbarMaterial = (RenderMaterial) Spout.getFilesystem().getResource("material://Vanilla/resources/gui/smt/HotbarGUIMaterial.smt");
	private final RenderMaterial iconsMaterial = (RenderMaterial) Spout.getFilesystem().getResource("material://Vanilla/resources/gui/smt/IconsGUIMaterial.smt");
	private final Font font = (Font) Spout.getFilesystem().getResource("font://Spout/resources/resources/fonts/ubuntu/Ubuntu-M.ttf");

	// The core elements of the main HUD
	private final Widget crosshair = new Widget();
	private final Widget hotbar = new Widget();
	private final Widget hearts = new Widget();
	private final Widget exp = new Widget();
	private final Widget bubbles = new Widget();
	private final Widget armor = new Widget();
	private final Widget hunger = new Widget();

	private final Random random = new Random();
	private final float maxSecsBubbles = VanillaData.AIR_SECS.getDefaultValue();

	private int hungerTicks;
	private int heartAnimationTicks;
	private boolean animateHearts;

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new IllegalStateException("May only attach this component to players!");
		}
		if (Spout.getPlatform() != Platform.CLIENT) {
			throw new IllegalStateException("This component is only attached to clients!");
		}

		initHUD();
		// TODO: Call these methods in DrowningComponent, HealthComponent, etc.
		setArmor(15);
		setHealth(getData().get(VanillaData.HEALTH));
		setHotbarSlot(0);
		setHunger(15, false);
	}

	@Override
	public boolean canTick() {
		return true; // getOwner().get(Human.class) == null ? false : getOwner().get(Human.class).isSurvival();
	}

	@Override
	public void onTick(float dt) {
		// Animate air meter
		final float secsBubbles = getData().get(VanillaData.AIR_SECS);
		if (secsBubbles == maxSecsBubbles) {
			hideBubbles();
		} else {
			final float nbBubExact = secsBubbles / maxSecsBubbles * 10f;
			final int nbBub = (int) nbBubExact;
			int bubId = 0;
			for (RenderPart bub : bubbles.get(RenderPartsHolderComponent.class).getRenderParts()) {
				if (bubId > nbBub) {
					bub.setSource(new Rectangle(34f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Empty
				} else if (bubId == nbBub) {
					if (nbBubExact - nbBub >= 0.02f) {
						bub.setSource(new Rectangle(34f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Empty
					} else {
						bub.setSource(new Rectangle(25f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Explode
					}
				} else {
					bub.setSource(new Rectangle(16f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Full
				}
				bubId++;
			}
		}
		bubbles.update();

		// Animate health bar
		List<RenderPart> heartParts = hearts.get(RenderPartsHolderComponent.class).getRenderParts();
		if (animateHearts) {
			float x = 0;
			if (heartAnimationTicks == 3) {
				// Flash off
				x = 16f;
				heartAnimationTicks++;
			} else if (heartAnimationTicks == 6) {
				// Flash on
				x = 25f;
				heartAnimationTicks++;
			} else if (heartAnimationTicks == 9) {
				// Flash off final time
				x = 16f;
				heartAnimationTicks = 0;
				animateHearts = false;
			} else {
				heartAnimationTicks++;
			}

			if (x != 0) {
				float y = VanillaConfiguration.HARDCORE_MODE.getBoolean() ? 45f / 256f : 0;
				for (int i = 10; i < 20; i++) {
					heartParts.get(i).setSource(new Rectangle(x / 256f, y, 9f / 256f, 9f / 256f));
				}

				hearts.update();
			}
		}

		float x = START_X;
		float y = -0.77f;
		float dx = 0.06f * SCALE;

		if (getData().get(VanillaData.HEALTH) <= 4) {
			List<RenderPart> parts = hearts.get(RenderPartsHolderComponent.class).getRenderParts();
			for (int i = 0; i < 10; i++) {
				RenderPart heart = parts.get(i);
				RenderPart heartBg = parts.get(i + 10);

				if (random.nextBoolean()) {
					y = -0.765f; // Twitch up
				} else {
					y = -0.775f; // Twitch down
				}
				heart.setSprite(new Rectangle(x + 0.005f, y, 0.065f * SCALE, 0.065f));
				heartBg.setSprite(new Rectangle(x, y, 0.065f * SCALE, 0.065f));

				x += dx;
			}

			hearts.update();
		}

		// Animate hunger bar
		int saturation = 0; // TODO: getData().get(VanillaData.FOOD_SATURATION);
		if (saturation <= 0) {
			List<RenderPart> parts = hunger.get(RenderPartsHolderComponent.class).getRenderParts();
			if (hungerTicks == 98) {
				x = 0.09f * SCALE;
				y = -0.77f;
				for (int i = 0; i < 10; i++) {
					RenderPart part = parts.get(i);
					RenderPart partBg = parts.get(i + 10);

					int rand = random.nextInt(3);
					if (rand == 0) {
						y = -0.765f; // Twitch up
					} else if (rand == 1) {
						y = -0.775f; // Twitch down
					}
					part.setSprite(new Rectangle(x, y, 0.075f * SCALE, 0.07f));
					partBg.setSprite(new Rectangle(x, y, 0.075f * SCALE, 0.07f));

					x += dx;
				}
				hungerTicks++;

				hunger.update();
			} else if (hungerTicks == 100) {
				// Reset hunger bar to normal
				x = 0.09f * SCALE;
				for (int i = 0; i < 10; i++) {
					parts.get(i).setSprite(new Rectangle(x, -0.77f, 0.075f * SCALE, 0.07f));
					parts.get(i + 10).setSprite(new Rectangle(x, -0.77f, 0.075f * SCALE, 0.07f));
					x += dx;
				}

				hungerTicks = 0;
			} else {
				hungerTicks++;
			}
		}
	}

	public void hideBubbles() {
		for (RenderPart bubble : bubbles.get(RenderPartsHolderComponent.class).getRenderParts()) {
			bubble.setSource(new Rectangle(34f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Empty
		}
	}

	public void showBubbles() {
		for (RenderPart bubble : bubbles.get(RenderPartsHolderComponent.class).getRenderParts()) {
			bubble.setSource(new Rectangle(16f / 256f, 18f / 256f, 9f / 256f, 9f / 256f)); // Full
		}
	}

	public void openHUD() {
		((Client) Spout.getEngine()).getScreenStack().openScreen(HUD);
	}

	/**
	 * Sets the amount of armor to display.
	 * @param amount Amount of armor to display
	 */
	public void setArmor(int amount) {
		RenderPartsHolderComponent armorRect = armor.get(RenderPartsHolderComponent.class);

		if (amount == 0) {
			for (RenderPart armorPart : armorRect.getRenderParts()) {
				armorPart.setSource(new Rectangle(52f / 256f, 9f / 256f, 12f / 256f, 12f / 256f)); // No icon
			}
		} else {
			for (int i = 0; i < 10; i++) {
				float x = 0;
				if (amount >= 2) {
					x = 43f; // Full
					amount -= 2;
				} else if (amount == 1) {
					x = 25f; // Half
					amount = 0;
				} else if (amount == 0) {
					x = 16f; // Empty
				}
				armorRect.get(i).setSource(new Rectangle(x / 256f, 9f / 256f, 9f / 256f, 9f / 256f));
			}
		}

		armor.update();
	}

	/**
	 * Modify the advancement of the xp bar.
	 * @param percent The advancement between 0 and 1
	 */
	public void setExp(float percent) {
		final RenderPart rect = exp.get(RenderPartsHolderComponent.class).get(1);
		rect.setSprite(new Rectangle(START_X, -0.82f, 1.81f * SCALE * percent, 0.04f));
		rect.setSource(new Rectangle(0, 69f / 256f, 182f / 256f * percent, 5f / 256f));
		exp.update();
	}

	/**
	 * Sets the health display.
	 * @param amount Amount of health to display
	 */
	public void setHealth(int amount) {
		setHealth(amount, false, false, false);
	}

	/**
	 * Sets the health display.
	 * @param amount Amount of health to display
	 * @param animate Whether to animate health bar
	 */
	public void setHealth(int amount, boolean animate) {
		setHealth(amount, animate, false, false);
	}

	/**
	 * Sets the health display.
	 * @param amount Amount of health to display
	 * @param animate Whether to animate health bar
	 * @param poison Whether health bar should use poisoned texture
	 * @param wither Whether health bar should use wither texture
	 */
	public void setHealth(int amount, boolean animate, boolean poison, boolean wither) {
		RenderPartsHolderComponent heartsRect = hearts.get(RenderPartsHolderComponent.class);
		float y = VanillaConfiguration.HARDCORE_MODE.getBoolean() ? 45f / 256f : 0;

		float baseX = 53f;
		if (poison) {
			baseX = 89f;
		} else if (wither) {
			// TODO: Wither texture x = 115f
		}

		for (int i = 0; i < 10; i++) {
			float x = baseX;
			if (amount == 0) {
				x = 124f; // Empty
			} else if (amount == 1) {
				x += 9f; // Half
				amount = 0;
			} else {
				x = baseX; // Full
				amount -= 2;
			}
			heartsRect.get(i).setSource(new Rectangle(x / 256f, y, 9f / 256f, 9f / 256f));
		}

		if (animate) {
			for (int i = 10; i < 20; i++) {
				heartsRect.get(i).setSource(new Rectangle(25f / 256f, y, 9f / 256f, 9f / 256f)); // Flash on
			}

			animateHearts = true;
		}

		hearts.update();
	}

	/**
	 * Sets the selected hotbar slot.
	 * @param slot Index of the slot to set
	 */
	public void setHotbarSlot(int slot) {
		if (slot < 0 || slot > 8) {
			throw new IllegalArgumentException("Slot must be between 0 and 8");
		}

		final RenderPart rect = hotbar.get(RenderPartsHolderComponent.class).get(1);
		rect.setSprite(new Rectangle(-0.72f * SCALE + (slot * .1175f), -1.005f, 0.24f * SCALE, 0.24f * SCALE));
		hotbar.update();
	}

	/**
	 * Sets the amount of hunger to display.
	 * @param amount Amount of hunger to display
	 * @param hungry Whether bar should use hunger effect texture
	 */
	public void setHunger(int amount, boolean hungry) {
		RenderPartsHolderComponent hungerRect = hunger.get(RenderPartsHolderComponent.class);

		float foregroundX = 52f;
		float backgroundX = 16f;
		if (hungry) {
			foregroundX = 88f;
			backgroundX = 133f;
		}

		if (amount == 0) {
			for (int i = 0; i < 10; i++) {
				hungerRect.get(i).setSource(new Rectangle(142f / 256f, 27f / 256f, 9f / 256f, 9f / 256f)); // Foreground
			}
			for (int i = 10; i < 20; i++) {
				hungerRect.get(i).setSource(new Rectangle(backgroundX / 256f, 27f / 256f, 9f / 256f, 9f / 256f)); // Background
			}
		} else {
			for (int i = 9; i >= 0; i--) {
				float x = foregroundX;
				if (amount == 0) {
					x = 142f; // Empty
				} else if (amount == 1) {
					x += 9f; // Half
					amount = 0;
				} else {
					x = foregroundX; // Full
					amount -= 2;
				}
				hungerRect.get(i).setSource(new Rectangle(x / 256f, 27f / 256f, 9f / 256f, 9f / 256f));
			}

			for (int i = 19; i >= 10; i--) {
				hungerRect.get(i).setSource(new Rectangle(backgroundX / 256f, 27f / 256f, 9f / 256f, 9f / 256f));
			}
		}

		hunger.update();
	}

	private void initHUD() {
		HUD.setTakesInput(false);

		float x = START_X;

		// Setup crosshairs
		final TexturedRectComponent crosshairRect = crosshair.add(TexturedRectComponent.class);
		crosshairRect.setRenderMaterial(iconsMaterial);
		crosshairRect.setColor(Color.WHITE);
		crosshairRect.setSprite(new Rectangle(-0.0625f * SCALE, -0.0625f, 0.125f * SCALE, 0.125f));
		crosshairRect.setSource(new Rectangle(0f / 256f, 0f / 256f, 16f / 256f, 16f / 256f));
		HUD.attachWidget(VanillaPlugin.getInstance(), crosshair);

		// Setup the hotbar
		final RenderPartsHolderComponent hotbarRect = hotbar.add(RenderPartsHolderComponent.class);
		final RenderPart hotbarBgRect = new RenderPart();
		hotbarBgRect.setRenderMaterial(hotbarMaterial);
		hotbarBgRect.setColor(Color.WHITE);
		hotbarBgRect.setSprite(new Rectangle(x, -1f, 1.42f * SCALE, 0.17f));
		hotbarBgRect.setSource(new Rectangle(0, 0, 0.71f, 0.085f));
		hotbarRect.add(hotbarBgRect, 1);

		final RenderPart hotbarSlotRect = new RenderPart();
		hotbarSlotRect.setRenderMaterial(hotbarMaterial);
		hotbarSlotRect.setColor(Color.WHITE);
		hotbarSlotRect.setSource(new Rectangle(0, 22f / 256f, 30f / 256f, 24f / 256f));
		hotbarRect.add(hotbarSlotRect, 0);
		HUD.attachWidget(VanillaPlugin.getInstance(), hotbar);

		// Experience level text
		final LabelComponent lvlTxt = exp.add(LabelComponent.class);
		exp.setGeometry(new Rectangle(-0.02f, -0.79f, 0, 0));
		lvlTxt.setFont(font);
		lvlTxt.setText(new ChatArguments(ChatStyle.BRIGHT_GREEN, "50"));

		// Setup survival-specific HUD components
		boolean survival = true; // getOwner().get(Human.class).isSurvival()
		if (survival) {
			// Experience bar
			final RenderPartsHolderComponent expRect = exp.add(RenderPartsHolderComponent.class);
			final RenderPart expBgRect = new RenderPart();
			expBgRect.setRenderMaterial(iconsMaterial);
			expBgRect.setColor(Color.WHITE);
			expBgRect.setSprite(new Rectangle(x, -0.82f, 1.81f * SCALE, 0.04f));
			expBgRect.setSource(new Rectangle(0, 64f / 256f, 0.91f, 0.019f));
			expRect.add(expBgRect);

			final RenderPart expBarRect = new RenderPart();
			expBarRect.setRenderMaterial(iconsMaterial);
			expBarRect.setColor(Color.WHITE);
			expRect.add(expBarRect);
			setExp(0);

			float dx = 0.06f * SCALE;

			// Health bar
			final RenderPartsHolderComponent heartsRect = hearts.add(RenderPartsHolderComponent.class);
			float y = VanillaConfiguration.HARDCORE_MODE.getBoolean() ? 45f / 256f : 0;
			for (int i = 0; i < 10; i++) {
				final RenderPart heart = new RenderPart();
				heart.setRenderMaterial(iconsMaterial);
				heart.setColor(Color.WHITE);
				heart.setSprite(new Rectangle(x + 0.005f, -0.77f, 0.065f * SCALE, 0.065f));
				heart.setSource(new Rectangle(53f / 256f, y, 9f / 256f, 9f / 256f));
				heartsRect.add(heart);
				x += dx;
			}

			x = START_X;
			for (int i = 0; i < 10; i++) {
				final RenderPart heartBg = new RenderPart();
				heartBg.setRenderMaterial(iconsMaterial);
				heartBg.setColor(Color.WHITE);
				heartBg.setSprite(new Rectangle(x, -0.77f, 0.065f * SCALE, 0.065f));
				heartBg.setSource(new Rectangle(16f / 256f, y, 9f / 256f, 9f / 256f));
				heartsRect.add(heartBg);
				x += dx;
			}

			HUD.attachWidget(VanillaPlugin.getInstance(), hearts);

			// Air meter
			final RenderPartsHolderComponent bubblesRect = bubbles.add(RenderPartsHolderComponent.class);
			x = 0.09f * SCALE;
			for (int i = 0; i < 10; i++) {
				final RenderPart bubble = new RenderPart();
				bubble.setRenderMaterial(iconsMaterial);
				bubble.setColor(Color.WHITE);
				bubble.setSprite(new Rectangle(x, -0.69f, 0.06f * SCALE, 0.06f));
				bubble.setSource(new Rectangle(16f / 256f, 18f / 256f, 9f / 256f, 9f / 256f));
				bubblesRect.add(bubble);
				x += dx;
			}
			HUD.attachWidget(VanillaPlugin.getInstance(), bubbles);

			// Hunger bar
			final RenderPartsHolderComponent hungerRect = hunger.add(RenderPartsHolderComponent.class);
			x = 0.09f * SCALE;
			for (int i = 0; i < 10; i++) {
				final RenderPart hunger = new RenderPart();
				hunger.setRenderMaterial(iconsMaterial);
				hunger.setColor(Color.WHITE);
				hunger.setSprite(new Rectangle(x, -0.77f, 0.075f * SCALE, 0.07f));
				hunger.setSource(new Rectangle(52f / 256f, 27f / 256f, 9f / 256f, 9f / 256f));
				hungerRect.add(hunger);
				x += dx;
			}

			x = 0.09f * SCALE;
			for (int i = 0; i < 10; i++) {
				final RenderPart hungerBg = new RenderPart();
				hungerBg.setRenderMaterial(iconsMaterial);
				hungerBg.setColor(Color.WHITE);
				hungerBg.setSprite(new Rectangle(x, -0.77f, 0.075f * SCALE, 0.07f));
				hungerBg.setSource(new Rectangle(16f / 256f, 27f / 256f, 9f / 256f, 9f / 256f));
				hungerRect.add(hungerBg);
				x += dx;
			}
			HUD.attachWidget(VanillaPlugin.getInstance(), hunger);

			// Armor bar
			final RenderPartsHolderComponent armorRect = armor.add(RenderPartsHolderComponent.class);
			x = START_X;
			for (int i = 0; i < 10; i++) {
				final RenderPart armorPart = new RenderPart();
				armorPart.setRenderMaterial(iconsMaterial);
				armorPart.setColor(Color.WHITE);
				armorPart.setSprite(new Rectangle(x, -0.7f, 0.06f * SCALE, 0.06f));
				armorPart.setSource(new Rectangle(52f / 256f, 9f / 256f, 12f / 256f, 12f / 256f));
				armorRect.add(armorPart);
				x += dx;
			}
			HUD.attachWidget(VanillaPlugin.getInstance(), armor);
		}

		HUD.attachWidget(VanillaPlugin.getInstance(), exp);
	}
}
