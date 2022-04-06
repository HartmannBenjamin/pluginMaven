package me.test.plugin.bomberman;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class Scoreboard {
    public static void set(Player p, int power, int speed, int number) {
        org.bukkit.scoreboard.Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("BomberMan", "dummy", "BomberMan - Sets");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score tiret = obj.getScore(ChatColor.WHITE + "------------------------------");
        tiret.setScore(1);

        Score powerPanel = obj.getScore(ChatColor.GRAY + "» Power : " + power);
        powerPanel.setScore(2);

        Score speedPanel = obj.getScore(ChatColor.GRAY + "» Speed : " + speed);
        speedPanel.setScore(3);

        Score numberPanel = obj.getScore(ChatColor.GRAY + "» Number of bombs : " + number);
        numberPanel.setScore(4);

        Score tiret2 = obj.getScore(ChatColor.WHITE + "------------------------------");
        tiret2.setScore(5);

        p.setScoreboard(board);
    }
}
