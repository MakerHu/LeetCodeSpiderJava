package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {
    public Properties Prop = new Properties();
//    public InputStream myResource = Settings.class.getResourceAsStream("src/main/resources/settings.properties");
    {
        try {
//            Prop.load(myResource);
            Prop.load(new FileInputStream("src/main/resources/settings.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getConf(String props) {
        return Prop.getProperty(props);
    }
}
