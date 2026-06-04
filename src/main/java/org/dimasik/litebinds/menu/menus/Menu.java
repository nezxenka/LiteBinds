package org.nezxenka.litebinds.menu.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.nezxenka.litebinds.LiteBinds;
import org.nezxenka.litebinds.database.ActionType;
import org.nezxenka.litebinds.database.PlayerActions;
import org.nezxenka.litebinds.menu.abst.AbstractMenu;
import org.nezxenka.litebinds.utils.Parser;

public class Menu extends AbstractMenu {

    @Override
    public AbstractMenu compile() {
        try {
            inventory = Bukkit.createInventory(
                this,
                27,
                Parser.color("Горячие клавиши")
            );
            Optional<PlayerActions> playerActionsOptional =
                LiteBinds.getInstance()
                    .getDatabaseManager()
                    .getPlayerActions(viewer.getName())
                    .get();
            PlayerActions playerActions;
            playerActions = playerActionsOptional.orElseGet(() ->
                new PlayerActions(
                    viewer.getName(),
                    ActionType.NONE,
                    ActionType.NONE,
                    ActionType.NONE
                )
            );

            int startSlot = 10;
            String[] displayNames = {
                "&x&0&0&B&C&C&CК&x&0&0&C&B&D&Dо&x&0&0&D&B&E&Eм&x&0&0&E&B&F&F &x&0&0&E&B&F&Fс&x&0&0&E&B&F&Fн&x&0&0&E&B&F&Fе&x&0&0&D&B&E&Eг&x&0&0&C&B&D&Dа",
                "&x&C&8&9&4&0&0С&x&D&2&9&B&0&0в&x&D&C&A&2&0&0е&x&E&6&A&A&0&0т&x&F&0&B&1&0&0и&x&F&B&B&9&0&0л&x&F&B&B&9&0&0ь&x&F&B&B&9&0&0н&x&F&B&B&9&0&0и&x&F&B&B&9&0&0к&x&F&B&B&9&0&0 &x&F&B&B&9&0&0Д&x&F&2&B&2&0&0ж&x&E&A&A&C&0&0е&x&E&1&A&6&0&0й&x&D&9&A&0&0&0к&x&D&0&9&A&0&0а",
                "&x&C&1&5&B&D&3Т&x&C&9&6&E&D&8р&x&D&0&8&1&D&Dа&x&D&0&8&1&D&Dп&x&C&9&6&E&D&8к&x&C&1&5&B&D&3а",
                "&x&E&F&E&F&E&FС&x&E&6&E&6&E&6т&x&D&C&D&C&D&Cа&x&D&3&D&3&D&3н",
                "&x&6&7&6&A&8&5В&x&6&C&6&F&8&Bз&x&7&1&7&4&9&2р&x&7&6&7&A&9&9ы&x&7&B&7&F&A&0в&x&8&1&8&5&A&7н&x&8&1&8&5&A&7а&x&8&1&8&5&A&7я&x&8&1&8&5&A&7 &x&8&1&8&5&A&7т&x&8&1&8&5&A&7р&x&7&B&7&F&A&0а&x&7&6&7&A&9&9п&x&7&1&7&4&9&2к&x&6&C&6&F&8&Bа",
                "&x&C&8&2&7&0&0В&x&D&2&2&9&0&0з&x&D&C&2&B&0&0р&x&E&6&2&D&0&0ы&x&F&0&2&F&0&0в&x&F&B&3&1&0&0н&x&F&B&3&1&0&0а&x&F&B&3&1&0&0я&x&F&B&3&1&0&0 &x&F&B&3&1&0&0ш&x&F&B&3&1&0&0т&x&F&0&2&F&0&0у&x&E&6&2&D&0&0ч&x&D&C&2&B&0&0к&x&D&2&2&9&0&0а",
                "&x&C&3&0&B&A&5Р&x&D&B&0&C&B&9ю&x&F&3&0&D&C&Eк&x&F&3&0&D&C&Eз&x&D&B&0&C&B&9а&x&C&3&0&B&A&5к &7(IV уровень)",
            };
            ActionType[] actionTypes = {
                ActionType.SNOWBALL,
                ActionType.JAKE,
                ActionType.ALTERNATIVE_TRAP,
                ActionType.STAN,
                ActionType.TRAP,
                ActionType.EXPLOSIVE,
                ActionType.BACKPACK,
            };
            Material[] materials = {
                Material.SNOWBALL,
                Material.JACK_O_LANTERN,
                Material.POPPED_CHORUS_FRUIT,
                Material.NETHER_STAR,
                Material.PRISMARINE_SHARD,
                Material.FIRE_CHARGE,
                Material.MAGENTA_SHULKER_BOX,
            };
            for (int i = 0; i < displayNames.length; i++) {
                String displayName = displayNames[i];
                ActionType actionType = actionTypes[i];
                ItemStack itemStack = new ItemStack(materials[i]);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(Parser.color(displayName));
                List<String> lore = new ArrayList<>();
                lore.add(Parser.color(""));
                lore.add(Parser.color(" &x&E&7&E&7&E&7Действие:"));
                if (actionType == playerActions.getActionDrop()) {
                    lore.add(
                        Parser.color(
                            "   &x&9&C&F&9&F&F● &x&D&5&D&B&D&CВыключено"
                        )
                    );
                    lore.add(Parser.color("   &6● Выбросить предмет [Q]"));
                    lore.add(
                        Parser.color(
                            "   &x&9&C&F&9&F&F● &x&D&5&D&B&D&CВторая рука &7[F]"
                        )
                    );
                    lore.add(
                        Parser.color(
                            "   &x&9&C&F&9&F&F● &x&D&5&D&B&D&CИспользовать &7[ПКМ]"
                        )
                    );
                    lore.add(Parser.color(""));
                    lore.add(
                        Parser.color(
                            " &x&0&0&D&8&F&F&l&n▍&f При &x&D&5&D&B&D&Cвыбрасывании Незеритового меча&f,"
                        )
                    );
                    lore.add(
                        Parser.color(
                            " &x&0&0&D&8&F&F&l▍&f вы активируете предмет " +
                                displayName
                        )
                    );
                } else if (actionType == playerActions.getActionSwap()) {
                    lore.add(
                        Parser.color(
                            "   &x&9&C&F&9&F&F● &x&D&5&D&B&D&CВыключено"
                        )
                    );
                    lore.add(
                        Parser.color(
                            "   &x&9&C&F&9&F&F● &x&D&5&D&B&D&CВыбросить предмет &7[Q]"
                        )
                    );
                    lore.add(Parser.color("   &6● Вторая рука [F]"));
                    lore.add(
                        Parser.color(
                            "   &x&9&C&F&9&F&F● &x&D&5&D&B&D&CИспользовать &7[ПКМ]"
                        )
                    );
                    lore.add(Parser.color(""));
                    lore.add(
                        Parser.color(
                            " &x&0&0&D&8&F&F&l&n▍&f При &x&D&5&D&B&D&Cвзятии во вторую руку Незеритового меча&f,"
                        )
                    );
                    lore.add(
                        Parser.color(
                            " &x&0&0&D&8&F&F&l▍&f вы активируете предмет " +
                                displayName
                        )
                    );
                } else if (actionType == playerActions.getActionInteract()) {
                    lore.add(
                        Parser.color(
                            "   &x&9&C&F&9&F&F● &x&D&5&D&B&D&CВыключено"
                        )
                    );
                    lore.add(
                        Parser.color(
                            "   &x&9&C&F&9&F&F● &x&D&5&D&B&D&CВыбросить предмет &7[Q]"
                        )
                    );
                    lore.add(
                        Parser.color(
                            "   &x&9&C&F&9&F&F● &x&D&5&D&B&D&CВторая рука &7[F]"
                        )
                    );
                    lore.add(Parser.color("   &6● Использовать [ПКМ]"));
                    lore.add(Parser.color(""));
                    lore.add(
                        Parser.color(
                            " &x&0&0&D&8&F&F&l&n▍&f При &x&D&5&D&B&D&Cнажатии ПКМ с Незеритовым мечем в руке&f,"
                        )
                    );
                    lore.add(
                        Parser.color(
                            " &x&0&0&D&8&F&F&l▍&f вы активируете предмет " +
                                displayName
                        )
                    );
                } else {
                    lore.add(Parser.color("   &6● Выключено"));
                    lore.add(
                        Parser.color(
                            "   &x&9&C&F&9&F&F● &x&D&5&D&B&D&CВыбросить предмет &7[Q]"
                        )
                    );
                    lore.add(
                        Parser.color(
                            "   &x&9&C&F&9&F&F● &x&D&5&D&B&D&CВторая рука &7[F]"
                        )
                    );
                    lore.add(
                        Parser.color(
                            "   &x&9&C&F&9&F&F● &x&D&5&D&B&D&CИспользовать &7[ПКМ]"
                        )
                    );
                    lore.add(Parser.color(""));
                    lore.add(
                        Parser.color(
                            " &x&0&0&D&8&F&F&l&n▍&f При &x&D&5&D&B&D&Cручной активации&f,"
                        )
                    );
                    lore.add(
                        Parser.color(
                            " &x&0&0&D&8&F&F&l▍&f вы активируете предмет " +
                                displayName
                        )
                    );
                }
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(startSlot + i, itemStack);
            }
            if (true) {
                ItemStack itemStack = new ItemStack(
                    Material.RED_STAINED_GLASS_PANE
                );
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(
                    Parser.color(" &x&0&5&F&B&0&0Выключить всё")
                );
                List<String> lore = new ArrayList<>();
                lore.add(Parser.color(""));
                lore.add(
                    Parser.color(
                        " &x&0&5&F&B&0&0▶ &fНажмите, чтобы сбросить горячие клавиши"
                    )
                );
                itemMeta.setLore(lore);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemMeta.addEnchant(Enchantment.SOUL_SPEED, 1, true);
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(22, itemStack);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}
