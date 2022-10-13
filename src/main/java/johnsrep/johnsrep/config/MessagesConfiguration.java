package johnsrep.johnsrep.config;

import space.arim.dazzleconf.annote.*;

@ConfHeader({""})
public interface MessagesConfiguration {

    interface Messages {
        @ConfDefault.DefaultString("Reputation")
        @ConfKey("Reputation-name")
        String reputationName();

        @ConfDefault.DefaultString("+")
        @ConfKey("Plus-symbol")
        char plusSymbol();

        @ConfDefault.DefaultString("-")
        @ConfKey("Minus-symbol")
        String minusSymbol();

    }
    @ConfKey("Messages")
    @SubSection
    @ConfComments({"There you can change messages for plugin."})
    MessagesConfiguration.Messages reputation();

}
