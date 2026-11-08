package vn.phutcity.core;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public final class PhoneMenu implements Listener {

    public record App(String name, Material icon, int slot) {}

    public static final List<App> APPS = List.of(
            new App("Ngân hàng", Material.GOLD_INGOT, 10),
            new App("Danh bạ", Material.WRITABLE_BOOK, 11),
            new App("Cảnh sát", Material.SHIELD, 12),
            new App("Cấp cứu", Material.GOLDEN_APPLE, 14),
            new App("Việc làm", Material.IRON_PICKAXE, 15),
            new App("Xe & Nhà", Material.MINECART, 16)
    );

    public static final class Holder implements InventoryHolder {
        private Inventory inventory;

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }

    public void open(Player p, String mode) {
        if (mode.equals("inv")) {
            openInventory(p);
        } else if (mode.equals("esc")) {
            openDialogOrFallback(p);
        } else if (isBedrock(p)) {
            openInventory(p);
        } else {
            openDialogOrFallback(p);
        }
    }

    private void openDialogOrFallback(Player p) {
        try {
            PhoneDialog.open(p);
        } catch (Throwable t) {
            p.sendMessage(Component.text("Không mở được menu ESC, chuyển sang menu inventory.", NamedTextColor.YELLOW));
            Bukkit.getLogger().warning("[PhutCityCore] Dialog loi: " + t);
            openInventory(p);
        }
    }

    public void openInventory(Player p) {
        Holder holder = new Holder();
        Inventory inv = Bukkit.createInventory(holder, 27, Component.text("Điện thoại PHỤT CITY"));
        holder.inventory = inv;
        for (App app : APPS) {
            ItemStack item = new ItemStack(app.icon());
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(app.name(), NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false));
            item.setItemMeta(meta);
            inv.setItem(app.slot(), item);
        }
        p.openInventory(inv);
    }

    public static boolean isBedrock(Player p) {
        try {
            Class<?> c = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            Object api = c.getMethod("getInstance").invoke(null);
            Object result = c.getMethod("isFloodgatePlayer", UUID.class).invoke(api, p.getUniqueId());
            return Boolean.TRUE.equals(result);
        } catch (Throwable t) {
            return false;
        }
    }

    public static Component devMessage(String appName) {
        return Component.text("[Điện thoại] " + appName + ": đang phát triển.", NamedTextColor.GRAY);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getView().getTopInventory().getHolder() instanceof Holder)) return;
        e.setCancelled(true);
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (e.getClickedInventory() != e.getView().getTopInventory()) return;
        for (App app : APPS) {
            if (app.slot() == e.getSlot()) {
                p.sendMessage(devMessage(app.name()));
                return;
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getView().getTopInventory().getHolder() instanceof Holder) {
            e.setCancelled(true);
        }
    }
                      }
