package com.occultation.www.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    public static InputStream getInputStream(String resourceLocation) throws FileNotFoundException {
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
            return getInputStream(url, description);
        }
        try {
            // try URL
            return getInputStream(new URL(resourceLocation));
        }
        catch (MalformedURLException ex) {
            // no URL -> treat as file path
            return new FileInputStream(new File(resourceLocation));
        }
    }

    public static InputStream getInputStream(URL resourceUrl) throws FileNotFoundException {
        return getInputStream(resourceUrl, "URL");
    }

    public static InputStream getInputStream(URL resourceUrl, String description) throws FileNotFoundException {
        Assert.notNull(resourceUrl, "Resource URL must not be null");
        if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {

            try {
                return resourceUrl.openStream();

            } catch (IOException e) {
                throw new FileNotFoundException(
                    description + " cannot be resolved to absolute file path " +
                        "because it does not reside in the file or jar system: " + resourceUrl);
            }

        }
        try {
            return new FileInputStream(new File(toURI(resourceUrl).getSchemeSpecificPart()));
        }
        catch (URISyntaxException ex) {
            return new FileInputStream(new File(resourceUrl.getFile()));
        }
    }

    public static URI toURI(URL url) throws URISyntaxException {
        return toURI(url.toString());
    }

    public static URI toURI(String location) throws URISyntaxException {
        return new URI(StringUtils.replace(location, " ", "%20"));
    }

    public static List<String> readLine(String path) {
        return readLine(path,false);
    }

    public static List<String> readLine(String path,boolean isThrow) {
        InputStream is;

        try {
            is = getInputStream(path);
        } catch (FileNotFoundException e) {
            if (isThrow) {
                throw new RuntimeException(e);
            }
            log.warn("file {} is not found",path);
            return Collections.emptyList();
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    is, StandardCharsets.UTF_8))) {
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
        try (InputStream in = FileUtil.getInputStream(fullFile)){
            p.load(in);
        } catch (IOException e) {
            log.error("cant read input stream",e);
        }
        return p;
    }

}
