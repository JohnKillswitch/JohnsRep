package johnsrep.johnsrep.config;

import space.arim.dazzleconf.annote.*;

@ConfHeader({""})
public interface MainConfiguration {

    interface Database {
        @ConfDefault.DefaultInteger(3306)
        @ConfKey("Database-Port")
        int portDB();

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



}
