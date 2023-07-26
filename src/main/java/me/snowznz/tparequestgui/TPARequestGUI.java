package me.snowznz.tparequestgui;

import net.ess3.api.events.TPARequestEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class TPARequestGUI extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {

    Inventory inventory;
    File file = new File(getDataFolder(), "user_data.yml");
    YamlConfiguration userData = YamlConfiguration.loadConfiguration(file);
    FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        // Config
        config.addDefault("gui-title", "&aTPA Request from &6{user}");
        config.options().copyDefaults(true);
        saveConfig();

        // Register events
        getServer().getPluginManager().registerEvents(this, this);

        // Register commands
        getCommand("tpagui").setExecutor(this);
    }

    @EventHandler
    public void onTPARequest(TPARequestEvent event) {

        Player player = Bukkit.getPlayer(event.getTarget().getUUID());

        if (!userData.contains(player.getUniqueId().toString()) || userData.getBoolean(player.getUniqueId().toString())) {
            String guiTitle = config.getString("gui-title");
            if (guiTitle.contains("{user}")) {
                guiTitle = guiTitle.replace("{user}", event.getRequester().getPlayer().getName());
                guiTitle = guiTitle.replace("&", "§");
            }
            inventory = Bukkit.createInventory(player, 3 * 9, guiTitle);
            ItemStack accept_item = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
            ItemMeta confirm_item_meta = accept_item.getItemMeta();
            confirm_item_meta.setDisplayName("§aAccept");
            accept_item.setItemMeta(confirm_item_meta);

            inventory.setItem(11, accept_item);

            ItemStack deny_item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
            ItemMeta deny_item_meta = deny_item.getItemMeta();
            deny_item_meta.setDisplayName("§cDeny");
            deny_item.setItemMeta(deny_item_meta);

            inventory.setItem(15, deny_item);

            ItemStack exit_item = new ItemStack(Material.BARRIER, 1);
            ItemMeta exit_item_meta = deny_item.getItemMeta();
            exit_item_meta.setDisplayName("§4Exit");
            exit_item.setItemMeta(exit_item_meta);

            inventory.setItem(26, exit_item);

            player.openInventory(inventory);
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!event.getInventory().equals(inventory)) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getCurrentItem().getType().isAir()) return;

        if (event.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE) && event.getCurrentItem().getItemMeta().getDisplayName().equals("§aAccept")) {
            event.getView().close();
            player.performCommand("tpaccept");
        } else if (event.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE) && event.getCurrentItem().getItemMeta().getDisplayName().equals("§cDeny")) {
            event.getView().close();
            player.performCommand("tpdeny");
        } else if (event.getCurrentItem().getType().equals(Material.BARRIER) && event.getCurrentItem().getItemMeta().getDisplayName().equals("§4Exit")) {
            event.getView().close();
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (!userData.contains(player.getUniqueId().toString()) || userData.getBoolean(player.getUniqueId().toString())) {
                    player.sendMessage("§aTPAGUI is enabled!");
                } else {
                    player.sendMessage("§cTPAGUI is disabled!");
                }
            } else if ("enable".equalsIgnoreCase(args[0])) {
                userData.set(player.getUniqueId().toString(), true);
                player.sendMessage("§aEnabled TPAGUI!");
                try {
                    userData.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if ("disable".equalsIgnoreCase(args[0])) {
                userData.set(player.getUniqueId().toString(), false);
                player.sendMessage("§cDisabled TPAGUI!");
                try {
                    userData.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            if ("reload".equalsIgnoreCase(args[0])) {
                if (sender.hasPermission("tpagui.reload")) {
                    reloadConfig();
                    sender.sendMessage("§aReloaded config!");
                } else {
                    sender.sendMessage("§c§l(!) §cYou don't have permission to use the command!");
                }
            } else {
                sender.sendMessage("§c§l(!) §cOnly players can run this command!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("enable");
            completions.add("disable");
            if (sender.hasPermission("tpagui.reload")) {
                completions.add("reload");
            }
        } else {
            return null;
        }

        return completions;
    }
}
