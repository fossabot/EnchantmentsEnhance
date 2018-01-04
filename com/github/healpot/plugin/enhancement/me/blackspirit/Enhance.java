package com.github.healpot.plugin.enhancement.me.blackspirit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.healpot.plugin.enhancement.me.main.Main;

public class Enhance {

	public Enchantment getItemEnchantmentType(Main m, Player player, ItemStack item) {
		if ((item.getType() == Material.DIAMOND_SWORD || item.getType() == Material.DIAMOND_AXE)
				&& m.permissions.enhancingWeapon(m, player)) {
			return Enchantment.DAMAGE_ALL;
		} else if ((item.getType() == Material.DIAMOND_CHESTPLATE || item.getType() == Material.DIAMOND_HELMET
				|| item.getType() == Material.DIAMOND_LEGGINGS || item.getType() == Material.DIAMOND_BOOTS)
				&& m.permissions.enhancingArmor(m, player)) {
			return Enchantment.PROTECTION_ENVIRONMENTAL;
		} else
			return null;
	}

	public int getItemEnchantLevel(Main m, Player player, ItemStack item) {
		return item.getEnchantmentLevel(getItemEnchantmentType(m, player, item));
	}

	public boolean getValidationOfItem(Main m, Player player, ItemStack item) {
		if (getItemEnchantmentType(m, player, item) == null) {
			// player.sendMessage(ChatColor.RED +
			// m.settings.getLang().getString("Enhance.itemInvalid"));
			return false;
		} else if (getItemEnchantLevel(m, player, item) > 19) {
			// player.sendMessage(ChatColor.RED +
			// m.settings.getLang().getString("Enhance.itemMax"));
			return false;
		} else {
			return true;
		}
	}

	public void enhanceSuccess(Main m, ItemStack item, Player player, boolean forceEnhanced) {
		Enchantment enchant = getItemEnchantmentType(m, player, item);
		int enchantLevel = getItemEnchantLevel(m, player, item);
		item.addUnsafeEnchantment(enchant, enchantLevel + 1);
		m.playSound.playSound(player, "SUCCESS");
		m.spawnFirework.launch(m, player, m.getConfig().getInt("fireworkCount." + enchantLevel),
				m.settings.getConfig().getInt("fireworkRounds." + enchantLevel),
				m.settings.getConfig().getInt("fireworkDelay"));
		if (forceEnhanced) {
			player.sendMessage(ChatColor.GREEN + m.settings.getLang().getString("Enhance.forceEnhanceSuccess"));
		} else {
			m.failstack.resetLevel(m, player);
			player.sendMessage(ChatColor.GREEN + m.settings.getLang().getString("Enhance.enhanceSuccess"));
		}
		m.data.addLore(m, item, player,
				ChatColor.translateAlternateColorCodes('&', m.settings.getLang().getString("Lore.UntradeableLore")),
				true);
		m.renameItem(item, enchantLevel);
	}

	public void enhanceFail(Main m, ItemStack item, Player player) {
		Enchantment enchant = getItemEnchantmentType(m, player, item);
		int enchantLevel = getItemEnchantLevel(m, player, item);
		String str = m.settings.getLang().getString("Enhance.enhanceFailed");
		if (enchantLevel > 16) {
			item.addUnsafeEnchantment(enchant, enchantLevel - 1);
			m.renameItem(item, enchantLevel - 1);
			str += m.settings.getLang().getString("Enhance.downgraded");
			m.playSound.playSound(player, "DOWNGRADED");
		}
		m.playSound.playSound(player, "FAILED");
		player.sendMessage(ChatColor.RED + str);
		m.failstack.addLevel(m, player, m.settings.getConfig().getInt("failstackGained." + enchantLevel));
	}

	public void diceToEnhancement(Main m, ItemStack item, Player player) {
		if (getValidationOfItem(m, player, item) == true) {
			int enchantLevel = getItemEnchantLevel(m, player, item);
			double random = Math.random();
			double chance;
			chance = m.failstack.getChance(m, player, enchantLevel);
			if (random < chance) {
				enhanceSuccess(m, item, player, false);
			} else {
				enhanceFail(m, item, player);
			}
		}
	}

	public void forceToEnhancement(Main m, ItemStack item, Player player) {
		if (getValidationOfItem(m, player, item) == true) {
			enhanceSuccess(m, item, player, true);
		}
	}

	public void getChance(Main m, ItemStack item, Player player) {
		Enchantment enchant = getItemEnchantmentType(m, player, item);
		if (enchant != null) {
			player.sendMessage(ChatColor.GOLD + m.settings.getLang().getString("Enhance.currentFailstack")
					+ m.failstack.getLevel(m, player));
			String chance = String.format("%.2f",
					m.failstack.getChance(m, player, item.getEnchantmentLevel(enchant)) * 100);
			player.sendMessage(ChatColor.GOLD
					+ m.settings.getLang().getString("Enhance.successRate").replaceAll("%chance%", chance));
		} else {
			player.sendMessage(ChatColor.RED + m.settings.getLang().getString("Enhance.itemInvalid"));
		}
	}

	public List<String> getChanceAsList(Main m, ItemStack item, Player player) {
		Enchantment enchant = getItemEnchantmentType(m, player, item);
		ArrayList<String> result = new ArrayList<String>();
		if (enchant != null) {
			String fs = (m.settings.getLang().getString("Enhance.currentFailstack") + m.failstack.getLevel(m, player));
			String placeholder = String.format("%.2f",
					m.failstack.getChance(m, player, item.getEnchantmentLevel(enchant)) * 100);
			String chance = m.settings.getLang().getString("Enhance.successRate").replaceAll("%chance%", placeholder);
			result.add(fs);
			result.add(chance);
			return result;

		} else {
			result.add(m.settings.getLang().getString("Enhance.itemInvalid"));
			return result;
		}
	}

}
