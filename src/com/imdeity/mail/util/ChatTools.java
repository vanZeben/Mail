package com.imdeity.mail.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatTools {
	public static final String Black = "\u00A70";
	public static final String Navy = "\u00A71";
	public static final String Green = "\u00A72";
	public static final String Blue = "\u00A73";
	public static final String Red = "\u00A74";
	public static final String Purple = "\u00A75";
	public static final String Gold = "\u00A76";
	public static final String LightGray = "\u00A77";
	public static final String Gray = "\u00A78";
	public static final String DarkPurple = "\u00A79";
	public static final String LightGreen = "\u00A7a";
	public static final String LightBlue = "\u00A7b";
	public static final String Rose = "\u00A7c";
	public static final String LightPurple = "\u00A7d";
	public static final String Yellow = "\u00A7e";
	public static final String White = "\u00A7f";
	public static final int lineLength = 54;

	public static List<String> listArr(Object[] args) {
		return list(Arrays.asList(args));
	}

	public static List<String> listArr(Object[] args, String prefix) {
		return list(Arrays.asList(args), prefix);
	}

	@SuppressWarnings("rawtypes")
	public static List<String> list(List args) {
		return list(args, "");
	}

	@SuppressWarnings("rawtypes")
	public static List<String> list(List args, String prefix) {
		if (args.size() > 0) {
			String line = "";
			for (int i = 0; i < args.size() - 1; i++)
				line += args.get(i) + ", ";
			line += args.get(args.size() - 1).toString();

			return color(prefix + line);
		}

		return new ArrayList<String>();
	}

	public static List<String> wordWrap(String[] tokens) {
		List<String> out = new ArrayList<String>();
		out.add("");

		for (String s : tokens) {
			if (stripColour(out.get(out.size() - 1)).length()
					+ stripColour(s).length() + 1 > lineLength)
				out.add("");
			out.set(out.size() - 1, out.get(out.size() - 1) + s + " ");
		}

		return out;
	}

	public static List<String> color(String line) {
		List<String> out = wordWrap(line.split(" "));

		String c = "f";
		for (int i = 0; i < out.size(); i++) {
			if (!out.get(i).startsWith("¤") && !c.equalsIgnoreCase("f"))
				out.set(i, "¤" + c + out.get(i));

			for (int index = 0; index < lineLength; index++)
				try {
					if (out.get(i).substring(index, index + 1)
							.equalsIgnoreCase("\u00A7"))
						c = out.get(i).substring(index + 1, index + 2);
				} catch (Exception e) {
				}
		}

		return out;
	}

	public static String stripColour(String s) {
		String out = "";
		for (int i = 0; i < s.length() - 1; i++) {
			String c = s.substring(i, i + 1);
			if (c.equals("¤"))
				i += 1;
			else
				out += c;
		}
		return out;
	}

	public static String strip(String line) {
		for (ChatColor cc : ChatColor.values())
			line.replaceAll(cc.toString(), "");
		return line;
	}

	public static String formatMessage(String msg) {
		String message = msg;

		if (message.contains("<black>") || message.contains("&0")) {
			message = message.replaceAll("<black>", "" + ChatColor.BLACK)
					.replaceAll("&0", "" + ChatColor.BLACK);

		}
		if (message.contains("<darkblue>") || message.contains("&1")) {
			message = message
					.replaceAll("<darkblue>", "" + ChatColor.DARK_BLUE)
					.replaceAll("&1", "" + ChatColor.DARK_BLUE);
		}
		if (message.contains("<darkgreen>") || message.contains("&2")) {
			message = message.replaceAll("<darkgreen>",
					"" + ChatColor.DARK_GREEN).replaceAll("&2",
					"" + ChatColor.DARK_GREEN);
		}
		if (message.contains("<teal>") || message.contains("&3")) {
			message = message.replaceAll("<teal>", "" + ChatColor.DARK_AQUA)
					.replaceAll("&3", "" + ChatColor.DARK_AQUA);
		}
		if (message.contains("<darkred>") || message.contains("&4")) {
			message = message.replaceAll("<darkred>", "" + ChatColor.DARK_RED)
					.replaceAll("&4", "" + ChatColor.DARK_RED);
		}
		if (message.contains("<darkpurple>") || message.contains("&5")) {
			message = message.replaceAll("<darkpurple>",
					"" + ChatColor.DARK_PURPLE).replaceAll("&5",
					"" + ChatColor.DARK_PURPLE);
		}
		if (message.contains("<gold>") || message.contains("&6")) {
			message = message.replaceAll("<gold>", "" + ChatColor.GOLD)
					.replaceAll("&6", "" + ChatColor.GOLD);
		}
		if (message.contains("<gray>") || message.contains("&7")) {
			message = message.replaceAll("<gray>", "" + ChatColor.GRAY)
					.replaceAll("&7", "" + ChatColor.GRAY);
		}
		if (message.contains("<darkgray>") || message.contains("&8")) {
			message = message
					.replaceAll("<darkgray>", "" + ChatColor.DARK_GRAY)
					.replaceAll("&8", "" + ChatColor.DARK_GRAY);
		}
		if (message.contains("<blue>") || message.contains("&9")) {
			message = message.replaceAll("<blue>", "" + ChatColor.BLUE)
					.replaceAll("&9", "" + ChatColor.BLUE);
		}
		if (message.contains("<green>") || message.contains("&a")) {
			message = message.replaceAll("<green>", "" + ChatColor.GREEN)
					.replaceAll("&a", "" + ChatColor.GREEN);
		}
		if (message.contains("<aqua>") || message.contains("&b")) {
			message = message.replaceAll("<aqua>", "" + ChatColor.AQUA)
					.replaceAll("&b", "" + ChatColor.AQUA);
		}
		if (message.contains("<red>") || message.contains("&c")) {
			message = message.replaceAll("<red>", "" + ChatColor.RED)
					.replaceAll("&c", "" + ChatColor.RED);
		}
		if (message.contains("<purple>") || message.contains("&d")) {
			message = message.replaceAll("<purple>",
					"" + ChatColor.LIGHT_PURPLE).replaceAll("&d",
					"" + ChatColor.LIGHT_PURPLE);
		}
		if (message.contains("<yellow>") || message.contains("&e")) {
			message = message.replaceAll("<yellow>", "" + ChatColor.YELLOW)
					.replaceAll("&e", "" + ChatColor.YELLOW);
		}
		if (message.contains("<white>") || message.contains("&f")) {
			message = message.replaceAll("<white>", "" + ChatColor.WHITE)
					.replaceAll("&f", "" + ChatColor.WHITE);
		}
		return message;
	}

	public static void formatAndSend(String msg, Player player) {
		String message = formatMessage(msg);
		player.sendMessage(message);
	}

	public static String formatSitTitle(String title) {
		String line = "}------------------------------------------------------{";
		int pivot = line.length() / 2;
		String center = "{ " + Yellow + title + Gold + " }";
		String out = Gold + line.substring(0, pivot - center.length() / 2);
		out += center + line.substring(pivot + center.length() / 2);
		return out;
	}

	public static String formatSituation(String requirement, String command,
			String help) {
		String out = "  ";
		if (requirement.length() > 0)
			out += Rose + requirement + ": ";
		out += Blue + command;
		if (help.length() > 0)
			out += LightGray + ": " + help;
		return out;
	}

	public static String formatComment(String requirement, String command,
			String help) {
		String out = "  ";
		if (requirement.length() > 0)
			out += Rose + "=> " + requirement;
		out += Blue + command;
		if (help.length() > 0)
			out += LightGray + ": " + help;
		return out;
	}
}
