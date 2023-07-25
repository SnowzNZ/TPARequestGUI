package me.snowznz.tparequestgui;

import me.snowznz.tparequestgui.commands.TPAGUI;
import net.ess3.api.events.TPARequestEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class TPARequestGUI extends JavaPlugin implements Listener {

    private static TPARequestGUI instance;
    Inventory inventory;

    public static TPARequestGUI getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Define the instance
        instance = this;

        // Create config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // Register events
        getServer().getPluginManager().registerEvents(this, this);

        // Register commands
        getCommand("tpagui").setExecutor(new TPAGUI());
    }

    @EventHandler
    public void onTPARequest(TPARequestEvent event) {

        Player player = Bukkit.getPlayer(event.getTarget().getUUID());

        if (!TPARequestGUI.getInstance().getConfig().contains(player.getUniqueId().toString()) || TPARequestGUI.getInstance().getConfig().getBoolean(player.getUniqueId().toString())) {

            inventory = Bukkit.createInventory(player, 3 * 9, "§8TPA Request from " + event.getRequester().getPlayer().getName());
            ItemStack accept_item = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
            ItemMeta confirm_item_meta = accept_item.getItemMeta();
            confirm_item_meta.setDisplayName("§aAccept");
            accept_item.setItemMeta(confirm_item_meta);

            inventory.setItem(12, accept_item);

            ItemStack deny_item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
            ItemMeta deny_item_meta = deny_item.getItemMeta();
            deny_item_meta.setDisplayName("§cDeny");
            deny_item.setItemMeta(deny_item_meta);

            inventory.setItem(14, deny_item);

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
}
