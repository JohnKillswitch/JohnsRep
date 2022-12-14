package johnsrep.johnsrep.Configs;

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
        "------------------------------------------------------------------------------------- #"})
public interface MessagesConfiguration {

    interface Messages {

        @ConfDefault.DefaultString("<gradient:#42C4FB:#44DDFC>Decalium</gradient><white>Rep <gray>➟")
        @ConfKey("prefix")
        String pluginPrefix();

        @ConfDefault.DefaultString("<green>+")
        @ConfKey("plus-symbol")
        String plusSymbol();

        @ConfDefault.DefaultString("<red>-")
        @ConfKey("minus-symbol")
        String minusSymbol();

        @ConfDefault.DefaultString("<gray><name> <white>[<value><white>] <white>[<gray><comment> <white>]")
        @ConfKey("reputation-format")
        String reputationFormat();

        @ConfDefault.DefaultString("<prefix> <red>You can`t give self a reputation")
        @ConfKey("reputation-self")
        String reputationSelf();

        @ConfDefault.DefaultString("<prefix> <red>You write command wrongly")
        @ConfKey("command-used-wrong")
        String commandUsedWrong();

        @ConfDefault.DefaultString("<prefix> <red>Right usage: /rep [Nickname] [+/-] [Comment]")
        @ConfKey("command-right-using")
        String commandRightUsing();

        @ConfDefault.DefaultString("<prefix> <green>You have successfully add reputation to <name>")
        @ConfKey("command-successfully-reputation")
        String commandSuccessfullyReputation();

        @ConfDefault.DefaultString("<white>Total:<white> <value>")
        @ConfKey("reputation-total-format")
        String reputationTotalFormat();

        @ConfDefault.DefaultString("<white>Total:<green> <value>")
        @ConfKey("reputation-total-format-plus")
        String reputationTotalFormatPlus();

        @ConfDefault.DefaultString("<white>Total:<red> <value>")
        @ConfKey("reputation-total-format-minus")
        String reputationTotalFormatMinus();

        @ConfDefault.DefaultString("<prefix> <red>You have not permissions for what")
        @ConfKey("command-no-permission")
        String commandNoPermission();

        @ConfDefault.DefaultString("<prefix> <white>player <yellow><name> <white>give you reputation!")
        @ConfKey("player-receive-reputation")
        String playerReceiveReputation();

        @ConfDefault.DefaultString("<prefix> <red>player <name> is never playing on server before")
        @ConfKey("player-not-found")
        String playerNotFound();

        @ConfDefault.DefaultString("<gray>--- <white>reputation of <name>: <gray>---")
        @ConfKey("reputation-of-player")
        String reputationOfPlayer();

        @ConfDefault.DefaultString("<prefix> <red>You have not enough time played on server to use this command")
        @ConfKey("need-more-played-time")
        String needMorePlayedTime();

    }
    @ConfKey("Messages")
    @SubSection
    @ConfComments({"There you can change messages for plugin."})
    Messages messages();

}
