package johnsrep.johnsrep.configs;

import space.arim.dazzleconf.annote.*;

@ConfHeader({
        "------------------------------------------------------------------------------------- #",
        "You can use MiniMessage format for ALL messages in this plugin                        #",
        "RGB colors:           <#00ff00>R G B!                                                 #",
        "Decorations:          <underlined>, <bold>, <italic>, <strikethrough>, <obfuscated>   #",
        "Reset formatting tag: <reset>                                                         #",
        "Clickable messages:   <click:run_command:/say hello>Click</click> to say hello        #",
        "Hoverable messages:   <hover:show_text:'<red>test'>TEST                               #",
        "Rainbow messages:     <yellow>Woo: <rainbow>|||||</rainbow>!                          #",
        "Gradient messages:    <yellow>Woo: <gradient>||||</gradient>!                         #",
        "Multiline messages:   its first line<newline> its second line!                        #",
        "                                                                                      #",
        "You can found more info about MiniMessage format in                                   #",
        "https://docs.adventure.kyori.net/minimessage/format.html#standard-tags                #",
        "------------------------------------------------------------------------------------- #"
})

public interface CommandsConfiguration {

    interface Commands {

        @ConfComments({
                "List of command that executes then player check self reputation",
                "You can use a <name> variable in commands"
        })
        @ConfDefault.DefaultStrings({
                "playsound block.anvil.hit master <name>",
                "tell <name> test command!"
        })
        @ConfKey("self-check-commands")
        String[] selfCheckCommands();

        @ConfComments({
                "List of command that executes then player add a - reputation",
                "You can use several variables here:",
                "<senderName> for command sender,",
                "<recipientName> for command recipient,",
                "<comment> for comment of added reputation"
        })
        @ConfDefault.DefaultStrings({
                "say <senderName> give a - reputation to <recipientName>"
        })
        @ConfKey("minus-reputation-added")
        String minusReputationAdded();

        @ConfComments({
                "List of command that executes then player add a + reputation",
                "You can use several variables here:",
                "<senderName> for command sender,",
                "<recipientName> for command recipient,",
                "<comment> for comment of added reputation"
        })
        @ConfDefault.DefaultStrings({
                "say <senderName> give a + reputation to <recipientName>"
        })
        @ConfKey("plus-reputation-added")
        String plusReputationAdded();

        @ConfComments({
                "List of command that executes then player check another",
                "player`s reputation. You can use a <name> variable in commands"
        })
        @ConfDefault.DefaultStrings({
                "playsound block.anvil.hit master <name>",
                "tell <name> test command!"
        })
        @ConfKey("another-check-commands")
        String anotherCheckCommands();
    }
}