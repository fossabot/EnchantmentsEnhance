//Thanks to Omniscimus.
package me.bukkit.effect;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class SpawnFirework {

	private final Random random = new Random();
	private final Type[] types = { Type.BALL, Type.BALL_LARGE, Type.BURST, Type.STAR };
	private final Color[] colors = { Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GREEN, Color.LIME, Color.MAROON,
			Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.WHITE, Color.TEAL,
			Color.YELLOW };

	public void launch(Player player, int times) {
		for (int i = 0; i < times; i++) {
			Firework fw = (Firework) player.getWorld().spawn(player.getLocation(), Firework.class);
			FireworkMeta fwMeta = fw.getFireworkMeta();
			fwMeta.addEffect(FireworkEffect.builder().flicker(random.nextBoolean())
					.withColor(colors[random.nextInt(14)]).withFade(colors[random.nextInt(14)])
					.with(types[random.nextInt(3)]).trail(random.nextBoolean()).build());
			fwMeta.setPower(random.nextInt(3));
			fw.setFireworkMeta(fwMeta);
		}
	}
}
