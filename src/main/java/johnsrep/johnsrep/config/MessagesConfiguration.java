package johnsrep.johnsrep.config;

import space.arim.dazzleconf.annote.*;

@ConfHeader({""})
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

        @ConfDefault.DefaultString("<gray><name> <#ff5050>[ <value> <gray><comment> <#ff5050>]")
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

    }
    @ConfKey("Messages")
    @SubSection
    @ConfComments({"There you can change messages for plugin."})
    Messages messages();

}
