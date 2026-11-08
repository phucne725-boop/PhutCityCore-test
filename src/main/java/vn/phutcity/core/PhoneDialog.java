package vn.phutcity.core;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/** Menu ESC cho Java. Tach rieng de de sua neu API Dialog thay doi. */
final class PhoneDialog {

    private PhoneDialog() {}

    static void open(Player player) {
        List<ActionButton> buttons = new ArrayList<>();
        for (PhoneMenu.App app : PhoneMenu.APPS) {
            buttons.add(ActionButton.builder(Component.text(app.name()))
                    .action(DialogAction.customClick(
                            (view, audience) -> audience.sendMessage(PhoneMenu.devMessage(app.name())),
                            ClickCallback.Options.builder().build()))
                    .build());
        }

        Dialog dialog = Dialog.create(factory -> factory.empty()
                .base(DialogBase.builder(Component.text("Điện thoại PHỤT CITY"))
                        .body(List.of(DialogBody.plainMessage(Component.text("Chọn một ứng dụng"))))
                        .build())
                .type(DialogType.multiAction(buttons, null, 1)));

        player.showDialog(dialog);
    }
}
