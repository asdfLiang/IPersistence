package com.lagou.io;

import java.io.InputStream;

/**
 * @author liangzj
 * @date 2021/1/24 19:11
 */
public class Resources {

    /**
     * 根据路径将文件读取为输入流
     *
     * @param path
     * @return
     */
    public static InputStream getResourceAsStream(String path) {
        // TODO 这里再看一下
        InputStream resourceAsStream = Resources.class.getClassLoader().getResourceAsStream(path);
        System.out.println(Resources.class.getClassLoader().getResource(""));
        System.out.println(Resources.class.getClassLoader().getResource(path));
        return resourceAsStream;
    }
}
