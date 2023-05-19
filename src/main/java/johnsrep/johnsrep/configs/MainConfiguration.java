package johnsrep.johnsrep.configs;

import space.arim.dazzleconf.annote.*;

@ConfHeader({"Main configuration file."})
public interface MainConfiguration {

    interface Database {
        @ConfComments({"SQLite or MySQL"})
        @ConfDefault.DefaultString("SQLite")
        @ConfKey("type")
        String dbType();

        @ConfComments({"NOT NEED IN SQLite mode!"})
        @ConfDefault.DefaultString("3306")
        @ConfKey("Database-Port")
        String portDB();

        @ConfComments({"NOT NEED IN SQLite mode!"})
        @ConfDefault.DefaultString("localhost")
        @ConfKey("Database-IP")
        String ipDB();

        @ConfComments({"NOT NEED IN SQLite mode!"})
        @ConfDefault.DefaultString("YourDatabaseName")
        @ConfKey("Database-Name")
        String nameDB();

        @ConfComments({"NOT NEED IN SQLite mode!"})
        @ConfDefault.DefaultString("Notch")
        @ConfKey("Database-Username")
        String usernameDB();

        @ConfComments({"NOT NEED IN SQLite mode!"})
        @ConfDefault.DefaultString("qwerty")
        @ConfKey("Database-Password")
        String passwordDB();
    }
    @ConfKey("Database")
    @SubSection
    @ConfComments({"Write your database information here."})
    Database database();

    interface OtherSettings {
        @ConfComments({"You need to write time in hours"})
        @ConfDefault.DefaultInteger(2)
        @ConfKey("needTimePlayed")
        int needTimePlayed();

        @ConfComments({"You need to write time in seconds"})
        @ConfDefault.DefaultInteger(15)
        @ConfKey("cooldownToSetReputation")
        int cooldownToSetReputation();
    }
    @ConfKey("OtherSettings")
    @SubSection
    OtherSettings otherSettings();



}
