package vn.phutcity.core;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class PhutCityCore extends JavaPlugin {

    private PhoneMenu phone;

    @Override
    public void onEnable() {
        phone = new PhoneMenu();
        Bukkit.getPluginManager().registerEvents(phone, this);
        for (String line : buildReport()) {
            getLogger().info(line);
        }
        getLogger().info("PhutCityCore (ban test) da bat.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = command.getName().toLowerCase();
        if (name.equals("pctest")) {
            for (String line : buildReport()) {
                sender.sendMessage(line);
            }
            return true;
        }
        if (name.equals("phone")) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage("Chỉ người chơi mới dùng được lệnh này.");
                return true;
            }
            String mode = args.length > 0 ? args[0].toLowerCase() : "auto";
            phone.open(p, mode);
            return true;
        }
        return false;
    }

    private List<String> buildReport() {
        List<String> r = new ArrayList<>();
        r.add("=== PHỤT CITY - Kiểm tra ===");
        r.add("Server: " + Bukkit.getVersion());
        r.add("Java: " + System.getProperty("java.version"));
        r.add("Online mode: " + Bukkit.getOnlineMode() + " (false = crack)");
        r.add("Geyser: " + (Bukkit.getPluginManager().getPlugin("Geyser-Spigot") != null ? "có" : "không"));
        r.add("Floodgate: " + (Bukkit.getPluginManager().getPlugin("floodgate") != null ? "có" : "không"));
        r.add("Dialog API: " + (hasClass("io.papermc.paper.dialog.Dialog") ? "có" : "không"));
        return r;
    }

    private boolean hasClass(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
                            }
