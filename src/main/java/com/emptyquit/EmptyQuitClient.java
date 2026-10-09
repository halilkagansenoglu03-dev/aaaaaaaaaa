package com.emptyquit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

/**
 * Client-side mod: if there are no other entities (players, mobs, animals,
 * items, ...) loaded around you for 10 seconds in a row, you are disconnected
 * from the server.
 *
 * Only runs on multiplayer servers, never in singleplayer.
 */
public class EmptyQuitClient implements ClientModInitializer {

	/** How long you may be alone before quitting, in seconds. */
	private static final int SECONDS_ALONE = 10;

	/** 20 ticks per second. */
	private static final int TICKS_ALONE = SECONDS_ALONE * 20;

	/** Consecutive ticks with zero other entities nearby. */
	private int emptyTicks = 0;

	@Override
	public void onInitializeClient() {
		// Fresh timer every time you join a server.
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> emptyTicks = 0);
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> emptyTicks = 0);

		ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
	}

	private void onTick(Minecraft mc) {
		if (mc.level == null || mc.player == null || mc.getConnection() == null) {
			emptyTicks = 0;
			return;
		}

		// Singleplayer / LAN host: never kick yourself out of your own world.
		if (mc.hasSingleplayerServer() || mc.getCurrentServer() == null) {
			emptyTicks = 0;
			return;
		}

		// Count every entity except you.
		int others = 0;
		for (Entity entity : mc.level.entitiesForRendering()) {
			if (entity != mc.player) {
				others++;
			}
		}

		if (others >= 1) {
			emptyTicks = 0;
			return;
		}

		emptyTicks++;
		if (emptyTicks >= TICKS_ALONE) {
			emptyTicks = 0;
			mc.getConnection().getConnection().disconnect(
					Component.literal("Empty Quit: no entities around you for " + SECONDS_ALONE + " seconds."));
		}
	}
}
