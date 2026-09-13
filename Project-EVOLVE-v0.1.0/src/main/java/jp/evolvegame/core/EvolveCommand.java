package jp.evolvegame.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class EvolveCommand implements CommandExecutor, TabCompleter {
    private final ProjectEvolvePlugin plugin;
    private final MatchManager match;

    public EvolveCommand(ProjectEvolvePlugin plugin, MatchManager match) {
        this.plugin = plugin;
        this.match = match;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "/evolve join, leave, start, status, setmonster, setevolution, reset");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join" -> {
                if (!(sender instanceof Player p)) return true;
                match.join(p);
            }
            case "leave" -> {
                if (!(sender instanceof Player p)) return true;
                match.leave(p);
            }
            case "status" -> sender.sendMessage(ChatColor.AQUA + "State=" + match.getState()
                    + " Stage=" + match.getMonsterStage()
                    + " Evolution=" + match.getEvolution()
                    + " Players=" + match.getJoined().size());
            case "start" -> {
                if (!admin(sender)) return true;
                match.start(sender);
            }
            case "reset" -> {
                if (!admin(sender)) return true;
                match.reset();
                sender.sendMessage(ChatColor.GREEN + "ゲーム状態をリセットしました。");
            }
            case "setmonster" -> {
                if (!admin(sender)) return true;
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "/evolve setmonster <player>");
                    return true;
                }
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "プレイヤーが見つかりません。");
                    return true;
                }
                match.setMonster(target);
                sender.sendMessage(ChatColor.GREEN + target.getName() + " をMonsterに設定しました。");
            }
            case "setevolution" -> {
                if (!admin(sender)) return true;
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "/evolve setevolution <amount>");
                    return true;
                }
                try {
                    match.addEvolution(Integer.parseInt(args[1]));
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "数値を指定してください。");
                }
            }
            default -> sender.sendMessage(ChatColor.RED + "Unknown subcommand.");
        }
        return true;
    }


    private boolean admin(CommandSender sender) {
        if (sender.hasPermission("evolve.admin")) return true;
        sender.sendMessage(ChatColor.RED + "権限がありません。");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("join", "leave", "status", "start", "reset", "setmonster", "setevolution").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("setmonster")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
