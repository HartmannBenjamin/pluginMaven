package me.test.plugin.bomberman.game.helpers;

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

        Score powerPanel = obj.getScore(ChatColor.GRAY + "» Power : " + power);
        powerPanel.setScore(1);

        Score speedPanel = obj.getScore(ChatColor.GRAY + "» Speed : " + speed);
        speedPanel.setScore(2);

        Score numberPanel = obj.getScore(ChatColor.GRAY + "» Number of bombs : " + number);
        numberPanel.setScore(3);

        Score tiret2 = obj.getScore(ChatColor.WHITE + "------------------------------");
        tiret2.setScore(4);

        p.setScoreboard(board);
    }
}
