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
        if (args.length == 0) {
            return false;
        }

        if (sender instanceof Player player) {

            if ("enable".equalsIgnoreCase(args[0])) {
                TPARequestGUI.getInstance().getConfig().set(player.getUniqueId().toString(), true);
                player.sendMessage("§aEnabled TPAGUI!");
                TPARequestGUI.getInstance().saveConfig();
                return true;
            }
            if ("disable".equalsIgnoreCase(args[0])) {
                TPARequestGUI.getInstance().getConfig().set(player.getUniqueId().toString(), false);
                player.sendMessage("§cDisabled TPAGUI!");
                TPARequestGUI.getInstance().saveConfig();
                return true;
            }
        } else {
            sender.sendMessage("§c§l(!) §cOnly players can run this command!");
            return true;
        }
        return false;
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
