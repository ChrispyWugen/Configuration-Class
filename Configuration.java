import SAP_webservice.AnalyticsDataUploadIn;
import com.sun.xml.internal.ws.client.BindingProviderProperties;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import javax.xml.ws.BindingProvider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Configuration {

    private String NamespaceURI = "";
    private String USERNAME = "";
    private String PASSWORD = "";
    private String PathToCSV = ""; //path to a file (in this case CSV) that you will work on later
    private String WSDL_Location = ""; //path to a WSDL file to create a webservice later on
    private static Configuration config = null; //singleton
    private String fileEnding = "";

    /**
     * Class holds the application configuration from properties file
     */
    private Configuration() {
    }

    public static synchronized Configuration getInstance() {
        if (config == null) {
            config = new Configuration();
        }
        return config;
    }

    /**
     * Reads a PROPERTIES file and stores the data in a new instance of this class
     *
     * @param file - PROPERTIES file that will be read
     * @throws IOException - if no PROPERTIES file can be read
     */
    public void readConfigParameters(File file) throws IOException {
        Configurations configurations = new Configurations();
        // access configuration properties
        try {
            org.apache.commons.configuration2.Configuration config = configurations.properties(file);
            this.NamespaceURI = config.getString("system.namespaceURI");
            this.USERNAME = config.getString("system.user");
            this.PASSWORD = config.getString("system.password");
            this.PathToCSV = config.getString("system.directory");
            this.WSDL_Location = config.getString("wsdl.Location");

        } catch (ConfigurationException cex) {
            cex.printStackTrace();
            System.out.println("File configurations.properties could not be read.\nPlease check whether it is in the same path from where you start the application");
            System.exit(1);
        }
    }

    public String getUSERNAME() {
        return this.USERNAME;
    }

    public String getPASSWORD() {
        return this.PASSWORD;
    }

    public String getWSDL_Location() {
        return this.WSDL_Location;
    }

    public String getNamespaceURI() {
        return this.NamespaceURI;
    }

    public String getPathToCSV() {
        return this.PathToCSV;
    }

    public String getFileEnding() {
        return this.fileEnding;
    }


    /**
     * Sets USERNAME and PASSWORD for the web service request
     *
     * @param port
     */
    public void setAuthentification(AnalyticsDataUploadIn port) {
        Map<String, Object> ctx = ((BindingProvider) port).getRequestContext();
        ctx.put(BindingProvider.USERNAME_PROPERTY, this.getUSERNAME());
        ctx.put(BindingProvider.PASSWORD_PROPERTY, this.getPASSWORD());
    }

    /**
     * Sets request and connect timeouts for the web service request
     *
     * @param port
     */
    public void setTimeout(AnalyticsDataUploadIn port) {
        Map<String, Object> ctx = ((BindingProvider) port).getRequestContext();
        ctx.put(BindingProviderProperties.REQUEST_TIMEOUT, 600000); // Timeout in millis
        ctx.put(BindingProviderProperties.CONNECT_TIMEOUT, 600000); // Timeout in millis
    }

    /**
     * Sets file ending
     *
     * @param fileEnding
     */
    public void setFileEnding(String fileEnding) {
        this.fileEnding = fileEnding;
    }

}
