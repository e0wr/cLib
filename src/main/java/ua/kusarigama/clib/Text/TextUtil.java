package ua.kusarigama.clib.Text;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ua.kusarigama.clib.CLib;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
    public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    public int getInt(String path, Object... args) {
        return CLib.getInstance().getConfig().getInt(path, 1);
    }

    public boolean getBoolean(String path, Object... args) {
        return CLib.getInstance().getConfig().getBoolean(path, false);
    }

    public String getMessage(String path, Object... args) {
        String message = CLib.getInstance().getConfig().getString(path, "");

        if (args.length > 0) {
            message = String.format(message, args);
        }

        message = translateColors(message);

        return message;
    }


    public void sendMessage(Player player, String path, Object... args) {
        String message = CLib.getInstance().getConfig().getString(path, "");

        if (args.length > 0) {
            message = String.format(message, args);
        }

        message = translateColors(message);

        String actionPattern = "\\{text: <(.*?)>, (.*?)\\}";
        Pattern pattern = Pattern.compile(actionPattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(message);

        TextComponent finalMessage = new TextComponent();
        int lastEnd = 0;

        while (matcher.find()) {
            String plainText = message.substring(lastEnd, matcher.start());
            if (!plainText.isEmpty()) {
                finalMessage.addExtra(new TextComponent(TextComponent.fromLegacyText(plainText)));
            }

            String text = matcher.group(1);
            String actions = matcher.group(2);

            TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(text));
            applyActions(textComponent, actions);

            finalMessage.addExtra(textComponent);
            lastEnd = matcher.end();
        }

        if (lastEnd < message.length()) {
            String remainingText = message.substring(lastEnd);
            if (!remainingText.isEmpty()) {
                finalMessage.addExtra(new TextComponent(TextComponent.fromLegacyText(remainingText)));
            }
        }

        player.spigot().sendMessage(finalMessage);
    }

    private void applyActions(TextComponent textComponent, String actions) {
        Pattern actionPattern = Pattern.compile("(hover|url|run_command|suggest_command): <(.*?)>");
        Matcher matcher = actionPattern.matcher(actions);

        while (matcher.find()) {
            String action = matcher.group(1);
            String value = matcher.group(2);

            switch (action) {
                case "hover":
                    BaseComponent[] hover = TextComponent.fromLegacyText(translateColors(value));
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover));
                    break;
                case "url":
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, value));
                    break;
                case "run_command":
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, value));
                    break;
                case "suggest_command":
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, value));
                    break;
            }
        }
    }

    public String getMessage(String path, Boolean prefix, Object... args) {
        String pprefix = CLib.getInstance().getConfig().getString("prefix");
        String message = pprefix+CLib.getInstance().getConfig().getString(path, "");

        if (args.length > 0) {
            message = String.format(message, args);
        }
        message = translateColors(message);
        return message;
    }

    public String getDefMessage(String path, Object... args) {
        return CLib.getInstance().getConfig().getString(path, "");
    }

    public List<String> getList(String path, Object... args) {
        List<String> lore = CLib.getInstance().getConfig().getStringList(path);

        for (int i = 0; i < lore.size(); ++i) {
            String line = lore.get(i);

            for (int j = 0; j < args.length; j += 2) {
                String key = args[j].toString();
                String value = args[j + 1].toString();
                line = line.replace(key, value);
            }

            lore.set(i, translateColors(line));
        }

        return lore;
    }

    public List<String> getList(String path, Player player, Object... args) {
        List<String> lore = CLib.getInstance().getConfig().getStringList(path);

        for (int i = 0; i < lore.size(); ++i) {
            String line = lore.get(i);

            for (int j = 0; j < args.length; j += 2) {
                String key = args[j].toString();
                String value = args[j + 1].toString();
                line = line.replace(key, value);
            }

            line = formApi(line, player);
            lore.set(i, translateColors(line));
        }

        return lore;
    }



    public String formApi(String text, Player p) {
        String s = text;
        if (p != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            s = PlaceholderAPI.setPlaceholders(p, text);
        }

        return s;
    }

    public void sendActionBar(Player player, String message) {
        String actionBarMessage = translateColors(message);
    }

    public String deftranslateColors(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        if (Bukkit.getBukkitVersion().contains("1.17") || (Bukkit.getBukkitVersion().contains("1.16"))) {
            Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, ChatColor.of(color) + "");
                matcher = pattern.matcher(message);
            }
        }

        return message;
    }

    public String translateColors(String text) {
        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));
        StringBuilder finalText = new StringBuilder();
        for (int i = 0; i < texts.length; ++i) {
            if (texts[i].equalsIgnoreCase("&")) {
                if (texts[++i].charAt(0) == '#') {
                    finalText.append(net.md_5.bungee.api.ChatColor.of((String)texts[i].substring(0, 7)) + texts[i].substring(7));
                    continue;
                }
                finalText.append(ChatColor.translateAlternateColorCodes((char)'&', (String)("&" + texts[i])));
                continue;
            }
            finalText.append(texts[i]);
        }
        return finalText.toString();
    }

    public void startWithCheck(Player player, String sz) {
        String s2;
        if (sz.startsWith("[MESSAGE]")) {
            s2 = sz.replace("[MESSAGE] ", "").replace("[MESSAGE]", "");
            player.sendMessage(translateColors(s2));
        }

        String[] split;
        if (sz.startsWith("[TITLE]")) {
            s2 = sz.replace("[TITLE] ", "").replace("[TITLE]", "");
            split = s2.split(";");
            player.sendTitle(translateColors(split[0]), translateColors(split[1]), 20, 15, 20);
        }

        if (sz.startsWith("[SOUND]")) {
            s2 = sz.replace("[SOUND] ", "").replace("[SOUND]", "");
            split = s2.split(";");
            Sound sound = Sound.valueOf(split[0]);
            int volume = Integer.parseInt(split[1]);
            int pitch = Integer.parseInt(split[2]);
            player.playSound(player.getLocation(), sound, (float)volume, (float)pitch);
        }

    }

    public void startWithCheck(Player player, String sz, String cd) {
        String s2;
        if (sz.startsWith("[MESSAGE]")) {
            s2 = sz.replace("[MESSAGE] ", "").replace("[MESSAGE]", "");
            player.sendMessage(translateColors(s2));
        }

        String[] split;
        if (sz.startsWith("[TITLE]")) {
            s2 = sz.replace("[TITLE] ", "").replace("[TITLE]", "");
            split = s2.split(";");
            player.sendTitle(translateColors(split[0]), translateColors(split[1]), 20, 15, 20);
        }

        if (sz.startsWith("[SOUND]")) {
            s2 = sz.replace("[SOUND] ", "").replace("[SOUND]", "");
            split = s2.split(";");
            Sound sound = Sound.valueOf(split[0]);
            int volume = Integer.parseInt(split[1]);
            int pitch = Integer.parseInt(split[2]);
            player.playSound(player.getLocation(), sound, (float)volume, (float)pitch);
        }

    }

    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(translateColors("&7[API] " + message));
    }

    public void sendError(String error) {
        Bukkit.getConsoleSender().sendMessage(translateColors("&#FF0000[ERROR] " + error));
    }

    public void sendWarn(String warn) {
        Bukkit.getConsoleSender().sendMessage(translateColors("&e[WARN] " + warn));
    }

    public void sendDev(String dev) {
        Bukkit.getConsoleSender().sendMessage(translateColors("&#4319DD[DEV] " + dev));
    }
}
