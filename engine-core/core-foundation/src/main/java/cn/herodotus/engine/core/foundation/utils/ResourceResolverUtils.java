/*
 * Copyright 2020-2030 码匠君<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 是 Dante Cloud 系统核心组件库，采用 APACHE LICENSE 2.0 开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1. 请不要删除和修改根目录下的LICENSE文件。
 * 2. 请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3. 请保留源码和相关描述文件的项目出处，作者声明等。
 * 4. 分发源码时候，请注明软件出处 <https://gitee.com/dromara/dante-cloud>
 * 5. 在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/dromara/dante-cloud>
 * 6. 若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.core.foundation.utils;

import cn.hutool.v7.core.codec.binary.Base64;
import cn.hutool.v7.core.data.id.IdUtil;
import cn.hutool.v7.core.io.IORuntimeException;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.swing.FontUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>Description: 资源文件处理工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/29 21:39
 */
public class ResourceResolverUtils {

    private static final Logger log = LoggerFactory.getLogger(ResourceResolverUtils.class);

    private static volatile ResourceResolverUtils INSTANCE;

    private final PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver;

    private ResourceResolverUtils() {
        this.pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
    }

    private static ResourceResolverUtils getInstance() {
        if (ObjectUtils.isEmpty(INSTANCE)) {
            synchronized (ResourceResolverUtils.class) {
                if (ObjectUtils.isEmpty(INSTANCE)) {
                    INSTANCE = new ResourceResolverUtils();
                }
            }
        }

        return INSTANCE;
    }

    private PathMatchingResourcePatternResolver getPathMatchingResourcePatternResolver() {
        return this.pathMatchingResourcePatternResolver;
    }

    private static PathMatchingResourcePatternResolver getResolver() {
        return getInstance().getPathMatchingResourcePatternResolver();
    }

    public static Resource getResource(String location) {
        Resource resource = getResolver().getResource(location);
        log.debug("[Herodotus] |- Resource at location [{}] is [{}]!", location, resource.exists());
        return resource;
    }

    /**
     * 根据 Ant-Path 模式，读取资源文件。
     * <p>
     * {@link PathMatchingResourcePatternResolver} 对 classpath 和 classpath* 处理机制不同。
     * 在 Spring Boot Jar 环境，{@link PathMatchingResourcePatternResolver#getResource(String)} 无法读取资源文件。
     * 只能通过 Ant Path 模式，批量读取指定位置下的文件。
     *
     * @param locationPattern 位置模式
     * @return {@link Resource}
     * @throws IOException 文件读取错误
     */
    public static Resource[] getResources(String locationPattern) throws IOException {
        return getResolver().getResources(locationPattern);
    }

    public static File getFile(String location) {
        Resource resource = getResource(location);
        try {
            // 在 Spring Boot Jar 环境，resource.getFile() 未必会正确读取到文件资源。
            if (resource.isFile()) {
                return resource.getFile();
            } else {
                return ResourceUtils.getFile(location);
            }
        } catch (FileNotFoundException e) {
            log.warn("[Herodotus] |- File not found in location [{}]", location, e);
            return new File(location);
        } catch (IOException e) {
            log.error("[Herodotus] |- Cannot found resource use location [{}]", location);
            return new File(location);
        }
    }

    public static InputStream getInputStream(String location) {
        File file = getFile(location);
        return IoUtil.toStream(file);
    }

    public static String getFilename(String location) {
        return getResource(location).getFilename();
    }

    public static URL getURL(String location) throws IOException {
        return getResource(location).getURL();
    }

    public static long contentLength(String location) throws IOException {
        return getResource(location).contentLength();
    }

    public static long lastModified(String location) throws IOException {
        return getResource(location).lastModified();
    }

    public static boolean isUrl(String location) {
        return ResourceUtils.isUrl(location);
    }

    public static boolean isJarUrl(URL url) {
        return ResourceUtils.isJarURL(url);
    }

    public static boolean isFileUrl(URL url) {
        return ResourceUtils.isFileURL(url);
    }

    public static boolean isJarFileUrl(URL url) {
        return ResourceUtils.isJarFileURL(url);
    }

    public static boolean isClasspathUrl(String location) {
        return Strings.CS.startsWith(location, ResourceLoader.CLASSPATH_URL_PREFIX);
    }

    public static boolean isClasspathAllUrl(String location) {
        return Strings.CS.startsWith(location, ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX);
    }

    /**
     * 将 {@link Resource} 转换为 byte
     *
     * @param resource 资源 {@link Resource}
     * @return byte 数组
     */
    public static byte[] toBytes(Resource resource) {
        try {
            InputStream inputStream = resource.getInputStream();
            return FileCopyUtils.copyToByteArray(inputStream);
        } catch (IOException e) {
            log.error("[Herodotus] |- Converter resource to byte[] error!", e);
            return null;
        }
    }

    /**
     * 将 {@link Resource} 转换为 Base64 数据。
     * <p>
     * 例如：将图片类型的 Resource 转换为可以直接在前端展现的 Base64 数据
     *
     * @param resource 资源 {@link Resource}
     * @return Base64 类型的字符串
     */
    public static String toBase64(Resource resource) {
        byte[] bytes = toBytes(resource);
        return Base64.encode(bytes);
    }

    /**
     * 根据 locationPattern 读取指定位置下的所有图片，并将其转换为 Base64 格式图片。
     *
     * @param locationPattern 注意：必须使用 classpath*: 作为前缀，同时包含通配符的路径。
     * @return Base64 格式图片
     */
    public static Map<String, String> getBase64Images(String locationPattern) {
        if (ResourceResolverUtils.isClasspathAllUrl(locationPattern)) {
            try {
                Resource[] resources = getResources(locationPattern);
                if (ArrayUtils.isNotEmpty(resources)) {
                    return Arrays.stream(resources).map(ResourceResolverUtils::toBase64)
                            .filter(StringUtils::isNotBlank)
                            .collect(Collectors.toMap(item -> IdUtil.fastSimpleUUID(), item -> item));
                }
            } catch (IOException e) {
                log.error("[Herodotus] |- Analysis the  location [{}] catch io error!", locationPattern, e);
            }
        }

        return new ConcurrentHashMap<>(8);
    }

    /**
     * 读取 resource 代表的 字体文件
     *
     * @param resource {@link Resource}
     * @return 字体 {@link Font}
     */
    public static Font getFont(Resource resource) {
        try {
            if (ObjectUtils.isNotEmpty(resource)) {
                if (resource.isFile()) {
                    return FontUtil.createFont(resource.getFile());
                } else {
                    return FontUtil.createFont(resource.getInputStream());
                }
            }
        } catch (IORuntimeException e) {
            // 虽然 java.awt.Font 抛出的是 IOException, 因为使用 Hutool FontUtil 将错误又包装了一次。所以出错时必须要拦截 IORuntimeException，否则会导致错误不被拦截直接抛出，应用启动失败。
            log.warn("[Herodotus] |- Can not read font in the resources folder, maybe in docker.");
            // 2022-10-21 尝试在 docker alpine 下解决字体问题的多种方式之一。目前改用 debian，下面代码已经不再需要。暂留，确保确实没有问题后再做处理
            // 可以确定两个问题：1）openjdk不包括sum.awt 的字体控件；2）alpine linux v3.9的基础镜像也未安装有 fontconfig 和 ttf-dejavu字体。
        } catch (IOException e) {
            log.error("[Herodotus] |- Resource object in resources folder catch io error!", e);
        }

        return null;
    }

    /**
     * 根据 locationPattern 读取指定位置下的所有字体。
     *
     * @param locationPattern 注意：必须使用 classpath*: 作为前缀，同时包含通配符的路径。
     * @return 字体 {@link Font}
     */
    public static Map<String, Font> getFonts(String locationPattern) {

        if (ResourceResolverUtils.isClasspathAllUrl(locationPattern)) {
            try {
                Resource[] resources = getResources(locationPattern);
                if (ArrayUtils.isNotEmpty(resources)) {
                    return Arrays.stream(resources).map(ResourceResolverUtils::getFont)
                            .filter(ObjectUtils::isNotEmpty)
                            .collect(Collectors.toMap(Font::getFontName, font -> font));
                }
            } catch (IOException e) {
                log.error("[Herodotus] |- Analysis the  location [{}] catch io error!", locationPattern, e);
            }
        }

        return new ConcurrentHashMap<>(8);
    }
}
