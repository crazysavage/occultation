package com.occultation.www.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-26 18:08
 */
public class FileUtil {

    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    public static final String URL_PROTOCOL_FILE = "file";

    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    public static File getFile(String resourceLocation) throws FileNotFoundException {
        Assert.notNull(resourceLocation, "Resource location must not be null");
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            String description = "class path resource [" + path + "]";
            ClassLoader cl = ClassUtils.getDefaultClassLoader();
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                throw new FileNotFoundException(description +
                        " cannot be resolved to absolute file path because it does not exist");
            }
            return getFile(url, description);
        }
        try {
            // try URL
            return getFile(new URL(resourceLocation));
        }
        catch (MalformedURLException ex) {
            // no URL -> treat as file path
            return new File(resourceLocation);
        }
    }

    public static File getFile(URL resourceUrl) throws FileNotFoundException {
        return getFile(resourceUrl, "URL");
    }

    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
        Assert.notNull(resourceUrl, "Resource URL must not be null");
        if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
            throw new FileNotFoundException(
                    description + " cannot be resolved to absolute file path " +
                            "because it does not reside in the file system: " + resourceUrl);
        }
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        }
        catch (URISyntaxException ex) {
            // Fallback for URLs that are not valid URIs (should hardly ever happen).
            return new File(resourceUrl.getFile());
        }
    }

    public static URI toURI(URL url) throws URISyntaxException {
        return toURI(url.toString());
    }

    public static URI toURI(String location) throws URISyntaxException {
        return new URI(StringUtils.replace(location, " ", "%20"));
    }

    public static List<String> readLine(String path) {

        File file;
        try {
            file = getFile(path);
        } catch (FileNotFoundException e) {
            log.error("get file error",e);
            return Collections.emptyList();
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file),
                        "utf-8"))) {
            reader.readLine();
            String temp;
            List<String> res = new ArrayList<>();
            while ( (temp = reader.readLine()) != null) {
                res.add(temp);
            }
            return res;

        } catch (Exception e) {
            log.warn("{} is not exist or is directory",path,e);
            return Collections.emptyList();
        }

    }

    public static Properties loadPropertyFile(String fullFile) {
        Properties p = new Properties();
        File file;
        try {
            file = FileUtil.getFile(fullFile);
        } catch (FileNotFoundException e) {
            return p;
        }
        try (InputStream in = new FileInputStream(file)){
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

}
