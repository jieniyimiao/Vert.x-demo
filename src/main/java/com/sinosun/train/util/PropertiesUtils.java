package com.sinosun.train.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created on 2019/8/1 15:20.
 *
 * @author caogu
 */
public class PropertiesUtils {

    /**
     * 加载property文件
     *
     * @param fileName 文件名，全路径
     * @return property
     * @throws IOException IO异常
     */
    public static Properties readProperty(String fileName) throws IOException {
        Properties properties = null;
        File file = new File(fileName);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            properties = new Properties();
            properties.load(in);
            in.close();
        }
        return properties;
    }
}
