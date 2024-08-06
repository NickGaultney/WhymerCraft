package com.whymertech.worldresetondeath;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities class, lots of useful stuff
 */
public class Utils
{

    public static Plugin plugin;

    /**
     * @hidden
     */
    public Utils(Plugin plugin)
    {

        Utils.plugin = plugin;
    }

    /**
     * Checks if the server is running Paper or spigot.
     * @return true/false
     */
    public static boolean isPaper()
    {
        boolean isPaper = false;
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            isPaper = true;
        } catch (ClassNotFoundException ignored) {
        }

        return isPaper;
    }

    public static void sendConsoleParsed(String s)
    {

        if(isPaper())
        {
            MiniMessage mm = MiniMessage.miniMessage();
            Bukkit.getConsoleSender().sendMessage(mm.deserialize(s).toString());
        }
        else {
            Bukkit.getConsoleSender().sendMessage(Utils.format(s));
        }

    }

    public static String parse(String s)
    {
        if(isPaper())
        {
            MiniMessage mm = MiniMessage.miniMessage();
            return mm.serialize(mm.deserialize(s));
        }
        else {
            return Utils.format(s);
        }
    }

    public static void sendParsed(Player p, String s)
    {

        if(isPaper())
        {
            MiniMessage mm = MiniMessage.miniMessage();
            p.sendMessage(mm.deserialize(s).toString());
        }
        else {
            p.sendMessage(Utils.format(s));
        }

    }


    /**
     * Formats a message with legacy color codes and also HEX
     * @param message the message you want to apply colors to
     * @return the formatted string
     */
    public static String format(String message) {
//ceva nu merge aici daca e stringu prea mic cred si de aici se fute globalu??

        if (message == null || message.length() == 0) return message;

        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        String color;

        while (matcher.find()) {
            color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }


        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);

    }

    /**
     * @hidden
     */
    public static void sendNoAccess(Player p)
    {
        String i = ChatColor.translateAlternateColorCodes('&', "&c&lOops");
        String m = ChatColor.translateAlternateColorCodes('&', "&fYou don't have permission");
        sendSound(p);
        p.sendTitle(i, m, 30, 50, 30);
    }

    public static String getLang(String path)
    {
        return plugin.getConfig().getString("messages." + path);
    }

    /**
     * Check a string for numbers
     * @param s the string
     * @return true/false
     */
    public static boolean containsNumbers(String s)
    {

        for(char c : s.toCharArray())
        {

            if(isInt(c))
            {
                return true;
            }

        }

        return false;
    }

    /**
     * Check if a plugin is enabled
     * @param plugin the plugin to check
     * @return true/false
     */
    public static boolean isEnabled(String plugin)
    {

        return Bukkit.getPluginManager().getPlugin(plugin) != null && Bukkit.getPluginManager().getPlugin(plugin).isEnabled();

    }


    /**
     * Sets a capital letter on the first word of the string
     * @param s the string
     * @return the formatted string
     */
    public static String setCapitals(String s)
    {

        if(s.length() < 1) return s;

        String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
        return cap;
    }


    /**
     * Generate a random number 1-100
     * @return a random number 1-100
     */
    public static int chance()
    {

        Random r = new Random();
        return r.nextInt(101);

    }

    public static double randDouble(double min, double max)
    {
        double x = ThreadLocalRandom.current().nextDouble(min, max);

        return x;
    }

    public static double round(double value)
    {
        String mny = new DecimalFormat("##.##").format(value);
        double d = Double.parseDouble(mny);
        return d;
    }

    public static float round(float value)
    {
        String mny = new DecimalFormat("##.#").format(value);
        float d = Float.parseFloat(mny);
        return d;
    }

    /**
     * Gets the location for the particles
     * @return the location for the particles
     */
    public static Location getParticleLocation(Location loc)
    {

        double x,y,z;

        x = randDouble(0.1, 0.9);
        y = randDouble(1, 1.9);
        z = randDouble(0.1, 0.9);

        return new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
    }

    /**
     * Generates a random integer between two values
     * @param min the min value
     * @param max the max value
     * @return the random integer
     */
    public static int randInt(int min, int max)
    {
        int x = ThreadLocalRandom.current().nextInt(min, max + 1);

        return x;
    }

    /**
     * Get the highest block at a location
     * @param world the world
     * @param x the x coordinate
     * @param z the z coordinate
     * @return the location with the highest block
     */
    public static Location getHighestBlock(World world, int x, int z, Location backup)
    {

        int i = 255;

        while (i >= 60) {
            if (!new Location(world, x, i, z).getBlock().isEmpty())
            {
                return new Location(world, x, i, z).add(0.0D, 1.0D, 0.0D);
            }
            i--;
        }

        return backup;
    }


    /**
     * @hidden
     */
    public static void sendNotPlayer()
    {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lConsole > &fTrebuie sa fii jucator pentru a accesa aceasta comanda."));
    }

    /**
     * @hidden
     */
    public static void sendTargetNull(Player p)
    {
        String i = ChatColor.translateAlternateColorCodes('&', "&c&lOops");
        String m = ChatColor.translateAlternateColorCodes('&', "&fPlayer is offline");
        sendSound(p);
        p.sendTitle(i, m, 30, 50, 30);
    }

    /**
     * @hidden
     */
    public static void sendSound(Player p)
    {
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
    }

    /**
     * Send a player a certain sound
     * @param p the player
     * @param s the sound
     */
    public static void sendSound(Player p, Sound s)
    {
        p.playSound(p.getLocation(), s, 1.0F, 1.0F);
    }

    /**
     * @hidden
     */
    public static void sendSoundHigh(Player p)
    {
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
    }

    /**
     * @hidden
     */
    public static void sendBreakSound(Player p)
    {
        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0F, 1.0F);
    }

    /**
     * @hidden
     */
    public static void sendLevelupSound(Player p)
    {
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
    }

    /**
     * @hidden
     */
    public static void sendNotInt(Player p)
    {
        String i = ChatColor.translateAlternateColorCodes('&', "&c&lOops");
        String m = ChatColor.translateAlternateColorCodes('&', "&fArgumentul trebuie sa fie un numar");
        sendSound(p);
        p.sendTitle(i, m, 30, 50, 30);
    }

    /**
     * Check if a string contains only letters
     * @param s the string
     * @return true/false
     */
    public static boolean validString(String s)
    {
        String regex = "^[a-zA-Z]*";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }

    /**
     * Check if a string contains only alphanumerical characters
     * @param s the string
     * @return true/false
     */
    public static boolean validAlphanumericString(String s)
    {
        String regex = "^[a-zA-Z0-9]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }

    /**
     * @hidden
     */
    public static void sendError(Player p, String s)
    {
        String i = ChatColor.translateAlternateColorCodes('&', "&c&lOops");
        String m = ChatColor.translateAlternateColorCodes('&', s);
        sendSound(p);
        p.sendTitle(i, m, 30, 50, 30);
    }

    /**
     * Temporarily gives invulnerability to a player
     * @param p the player
     * @param seconds how many seconds should the effect last
     */
    public static void tempGod(Player p, int seconds)
    {

        p.setInvulnerable(true);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
        {

            if(p != null)
            {
                p.setInvulnerable(false);
            }

        }, seconds * 20);

    }

    /**
     * Check if a String is actually an int
     * @param str the string
     * @return true/false
     */
    public static boolean isInt(String str)
    {
        try
        {
            Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException e) {}
        return false;
    }

    /**
     * Check if a char is actually an int
     * @param c the char
     * @return true/false
     */
    public static boolean isInt(char c)
    {
        try
        {
            Integer.parseInt(String.valueOf(c));
            return true;
        }
        catch (NumberFormatException e) {}
        return false;
    }


    /**
     * Check if a String is actually a double
     * @param str the string
     * @return true/false
     */
    public static boolean isDouble(String str)
    {
        try
        {
            Double.parseDouble(str);
            return true;
        }
        catch (NumberFormatException e) {}
        return false;
    }


    /**
     * Get a property from server.properties file
     * @param s the string you want to get
     * @param f the file you wish to access
     * @return the value you wish to get
     */
    public static String getProperty(String s, File f)
    {
        Properties pr = new Properties();

        try
        {
            FileInputStream in = new FileInputStream(f);
            pr.load(in);
            String string = pr.getProperty(s);
            return string;
        }

        catch (IOException e)
        { }

        return "";
    }

    /**
     * Get the main world of the server
     * @return the main world's name
     */
    public static String getMainWorld()
    {

        File s = new File("server.properties");
        return getProperty("level-name", s);

    }


}