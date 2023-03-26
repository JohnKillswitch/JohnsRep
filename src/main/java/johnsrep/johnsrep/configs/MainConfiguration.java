package johnsrep.johnsrep.configs;

import space.arim.dazzleconf.annote.*;

@ConfHeader({"Main configuration file."})
public interface MainConfiguration {

    interface Database {
        @ConfComments({"MySQL or H2"})
        @ConfDefault.DefaultString("SQLite")
        @ConfKey("type")
        String dbType();

        @ConfDefault.DefaultString("3306")
        @ConfKey("Database-Port")
        String portDB();

        @ConfDefault.DefaultString("localhost")
        @ConfKey("Database-IP")
        String ipDB();

        @ConfDefault.DefaultString("YourDatabaseName")
        @ConfKey("Database-Name")
        String nameDB();

        @ConfDefault.DefaultString("Notch")
        @ConfKey("Database-Username")
        String usernameDB();

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
    }
    @ConfKey("OtherSettings")
    @SubSection
    OtherSettings otherSettings();



}
