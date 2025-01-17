package org.pixeltime.enchantmentsenhance.command.console

import org.bukkit.command.CommandSender
import org.pixeltime.enchantmentsenhance.Main
import org.pixeltime.enchantmentsenhance.command.SubConsoleCommand
import org.pixeltime.enchantmentsenhance.listener.EnchantmentListener
import org.pixeltime.enchantmentsenhance.manager.SettingsManager
import org.pixeltime.enchantmentsenhance.util.ClassGetter
import java.util.Arrays

class DebugConsoleCommand : SubConsoleCommand() {

    override fun onCommand(sender: CommandSender, args: Array<String>) {
        val sb = StringBuilder()
        sb.appendln("")
        if (args[0] == "gen") {
            when {
                args[1] == "en" -> {

                    // Generate English Enchantments Wiki.
                    sb.appendln("=====Generating English Enchantments Wiki=====")
                    sb.appendln("| Enchantments ID| Effect| Level|")
                    sb.appendln("| :---:| :---------| :---:")
                    for (enchClass in ClassGetter.getClassesForPackage(Main.getMain(), "org.pixeltime.enchantmentsenhance.event.enchantment")) {
                        if (EnchantmentListener::class.java.isAssignableFrom(enchClass)) {
                            try {
                                val enchantmentListener = enchClass.newInstance() as EnchantmentListener
                                sb.appendln("|" + enchantmentListener.javaClass.simpleName + "|" + enchantmentListener.desc()[0] + "|" + Main.getApi().getEnchantmentMaxLevel(enchantmentListener.javaClass.simpleName) + "|")
                            } catch (e: InstantiationException) {
                                e.printStackTrace()
                            } catch (e: IllegalAccessException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    // Generate English Commands Wiki.
                    sb.appendln("=====Generating English Commands Wiki=====")
                    sb.appendln("| Player Command| Permission| Aliases|")
                    sb.appendln("| :---|:---|:---|")
                    for (command in Main.getCommandManager().commands) {
                        try {
                            sb.appendln(("|" + command.usage().replace("|", "&#124;") + " - " + SettingsManager.lang.getString("help." + command.name())!!.replace("|", "&#124;") + "|" + command.permission + "|" + Arrays.toString(command.aliases()) + "|").replace("&[a-z0-9]".toRegex(), "").replace("\n", "<br />"))
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
                args[1] == "cn" -> {
                    // Generate Chinese Enchantments Wiki.
                    sb.appendln("=====Generating Chinese Enchantments Wiki=====")
                    sb.appendln("| 附魔ID| 中文名称| 效果|")
                    sb.appendln("| :---:|:---:| :--------------------------:|")
                    for (enchClass in ClassGetter.getClassesForPackage(Main.getMain(), "org.pixeltime.enchantmentsenhance.event.enchantment")) {
                        if (EnchantmentListener::class.java.isAssignableFrom(enchClass)) {
                            try {
                                val enchantmentListener = enchClass.newInstance() as EnchantmentListener
                                sb.appendln("|" + enchantmentListener.javaClass.simpleName + "|" + enchantmentListener.lang()[0] + "|" + enchantmentListener.desc()[1] + "|")
                            } catch (e: InstantiationException) {
                                e.printStackTrace()
                            } catch (e: IllegalAccessException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
        if (args[0] == "test") {
            sb.append("Hello World")
        }
        print(sb.toString())
    }


    override fun name(): String {
        return "debug"
    }


    override fun usage(): String {
        return "/enhance debug"
    }


    override fun aliases(): Array<String> {
        return arrayOf("debug", "tiaoshi", "ts")
    }


    fun log(s: String) {
        Main.getMain().logger.info("DEBUG >>>$s")
    }

}
