package me.snowznz.tparequestgui.commands;

import me.snowznz.tparequestgui.TPARequestGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TPAGUI implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (!TPARequestGUI.getInstance().getConfig().contains(player.getUniqueId().toString()) || TPARequestGUI.getInstance().getConfig().getBoolean(player.getUniqueId().toString())) {
                    player.sendMessage("§aTPAGUI is enabled!");
                } else {
                    player.sendMessage("§cTPAGUI is disabled!");
                }
            }
            if ("enable".equalsIgnoreCase(args[0])) {
                TPARequestGUI.getInstance().getConfig().set(player.getUniqueId().toString(), true);
                player.sendMessage("§aEnabled TPAGUI!");
                TPARequestGUI.getInstance().saveConfig();
            }
            if ("disable".equalsIgnoreCase(args[0])) {
                TPARequestGUI.getInstance().getConfig().set(player.getUniqueId().toString(), false);
                player.sendMessage("§cDisabled TPAGUI!");
                TPARequestGUI.getInstance().saveConfig();
            }
        } else {
            sender.sendMessage("§c§l(!) §cOnly players can run this command!");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("enable");
            completions.add("disable");
        } else {
            return null;
        }

        return completions;
    }
}
