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

package org.dromara.dante.core.support.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.dante.core.constant.SymbolConstants;
import org.dromara.dante.core.utils.WellFormedUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * <p>Description: 服务本地文件操作定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/23 14:20
 */
public interface FileTemplate {

    /**
     * 本地文件存放默认根地址。
     * <p>
     * 外部可以通过 Properties 进行设置，设置之后则统一使用该地址。
     *
     * @return 本地文件存放地址
     */
    String getDefaultDestination();

    /**
     * 优化路径策略。文件路径末端是否包含分隔符的处理。
     * <p>
     * 当前处理逻辑为，如果文件路径末端有分割符，则去掉。
     *
     * @param path 路径
     * @return 优化后的路径
     */
    private String wellFormedPath(String path) {
        return WellFormedUtils.robustness(path, SymbolConstants.FORWARD_SLASH, false, false);
    }

    /**
     * 获取有效的本地文件存放根地址
     * <p>
     * 如果参数 destination 的值为空，那么直接使用系统默认地址。
     *
     * @param destination 本地文件存放根地址
     * @return 本地文件存放根地址
     */
    default String getWellFormedDestination(String destination) {
        return StringUtils.isNotEmpty(destination) ? wellFormedPath(destination) : FileUtils.getUserDirectory().getPath();
    }

    /**
     * 获取有效的本地文件存目录。
     * <p>
     * 如果目录为 null，则返回 {@link #getDefaultDestination()} 对应的根目录
     *
     * @param destination 本地文件存放根地址
     * @param directory   文件目录
     * @return 文件目录对应的具体路径 {@link Path}
     */
    default Path getWellFormedDirectory(String destination, String directory) {
        return StringUtils.isNotBlank(directory) ? Paths.get(getWellFormedDestination(destination), wellFormedPath(directory)) : Paths.get(getWellFormedDestination(destination));
    }

    /**
     * 根据文件位置、文件夹和文件名获取完整的文件路径
     *
     * @param destination 本地文件存放根地址
     * @param directory   文件夹
     * @param name        文件名
     * @return 文件路径 {@link Path}
     * @throws IOException IO 异常
     */
    default Path getPath(String destination, String directory, String name) throws IOException {
        Path wellFormedDirectory = getWellFormedDirectory(destination, directory);

        // 本接口中的 exists 方法引用到了 getPath 方法，所以这里直接使用原生方法，避免循环引用
        if (!Files.exists(wellFormedDirectory)) {
            // 如果目录不存在
            Files.createDirectories(wellFormedDirectory);
        }

        return wellFormedDirectory.resolve(name);
    }

    /**
     * 根据文件夹和文件名获取完整的文件路径
     *
     * @param directory 文件夹
     * @param name      文件名
     * @return 文件路径 {@link Path}
     * @throws IOException IO 异常
     */
    default Path getPath(String directory, String name) throws IOException {
        return getPath(getDefaultDestination(), directory, name);
    }

    /**
     * 根据文件名获取完整的文件路径。
     * <p>
     * 不设置任何文件夹，直接存放于已经设置的本地文件存放根目录下。
     *
     * @param name 文件名
     * @return 文件路径 {@link Path}
     */
    default Path getPath(String name) throws IOException {
        return getPath(null, name);
    }

    /**
     * 删除指定路径对应的文件
     *
     * @param path 文件路径
     * @return 是否删除成功。true 成功，false 失败
     * @throws IOException IO 异常
     */
    default boolean delete(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

    /**
     * 删除指定位置、指定目录下的指定文件
     *
     * @param destination 本地文件存放根地址
     * @param directory   目录
     * @param name        文件名
     * @return 是否删除成功。true 成功，false 失败
     * @throws IOException IO 异常
     */
    default boolean delete(String destination, String directory, String name) throws IOException {
        Path path = getPath(destination, directory, name);
        return delete(path);
    }

    /**
     * 删除默认文件存放位置下，指定目录下的指定文件
     *
     * @param directory 目录
     * @param name      文件名
     * @return 是否删除成功。true 成功，false 失败
     * @throws IOException IO 异常
     */
    default boolean delete(String directory, String name) throws IOException {
        return delete(getDefaultDestination(), directory, name);
    }

    /**
     * 删除默认文件存放位置（根目录）下的指定文件
     *
     * @param name 文件名
     * @return 是否删除成功。true 成功，false 失败
     */
    default boolean delete(String name) throws IOException {
        return delete(null, name);
    }

    /**
     * 将字符串类型内容写入指定路径下的文件
     *
     * @param path    文件路径
     * @param content 内容
     * @return 文件路径 {@link Path}
     * @throws IOException IO 异常
     */
    default Path writeString(Path path, String content) throws IOException {
        return Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC);
    }

    /**
     * 将字符串类型内容写入指定位置、指定目录下的指定文件
     *
     * @param destination 本地文件存放根地址
     * @param directory   文件坐在文件夹
     * @param fileName    文件名。可以是包含相对路径的文件名。
     * @param content     写入的内容
     * @return 文件 {@link Path}
     * @throws IOException IO 异常
     */
    default Path writeString(String destination, String directory, String fileName, String content) throws IOException {
        Path path = getPath(destination, directory, fileName);
        return writeString(path, content);
    }

    /**
     * 将字符串类型内容写入默认位置、指定目录下的指定文件
     *
     * @param directory 文件坐在文件夹
     * @param fileName  文件名。可以是包含相对路径的文件名。
     * @param content   写入的内容
     * @return 文件 {@link Path}
     * @throws IOException IO 异常
     */
    default Path writeString(String directory, String fileName, String content) throws IOException {
        return writeString(getDefaultDestination(), directory, fileName, content);
    }

    /**
     * 将字符串类型内容写入默认位置（根目录）下的指定文件
     *
     * @param fileName 文件名。可以是包含相对路径的文件名。
     * @param content  写入的内容
     * @return 文件路径 {@link Path}
     */
    default Path writeString(String fileName, String content) throws IOException {
        return writeString(null, fileName, content);
    }

    /**
     * 向指定路径对应的文件，创建写入文件流（需要手动关闭流）
     *
     * @param path 文件路径
     * @return 流 {@link OutputStream}
     * @throws IOException IO 异常
     */
    default OutputStream newOutputStream(Path path) throws IOException {
        return Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC);
    }

    /**
     * 向指定位置、指定目录下的对应的文件，创建输出流（需要手动关闭流）
     *
     * @param destination 本地文件存放根地址
     * @param directory   文件坐在文件夹
     * @param fileName    文件名。可以是包含相对路径的文件名。
     * @return 流 {@link OutputStream}
     * @throws IOException IO 异常
     */
    default OutputStream newOutputStream(String destination, String directory, String fileName) throws IOException {
        Path path = getPath(destination, directory, fileName);
        return newOutputStream(path);
    }

    /**
     * 向默认位置、指定目录下的对应的文件，创建输出流（需要手动关闭流）
     *
     * @param directory 文件坐在文件夹
     * @param fileName  文件名。可以是包含相对路径的文件名。
     * @return 流 {@link OutputStream}
     * @throws IOException IO 异常
     */
    default OutputStream newOutputStream(String directory, String fileName) throws IOException {
        return newOutputStream(getDefaultDestination(), directory, fileName);
    }

    /**
     * 向默认位置（根目录）下的对应的文件，创建输出流（需要手动关闭流）
     *
     * @param fileName 文件名。可以是包含相对路径的文件名。
     * @return 流 {@link OutputStream}
     * @throws IOException IO 异常
     */
    default OutputStream newOutputStream(String fileName) throws IOException {
        return newOutputStream(null, fileName);
    }

    /**
     * 读取指定路径对应文件，并返回字符串类型内容
     *
     * @param path 文件路径
     * @return 文件内容
     * @throws IOException IO 异常
     */
    default String readString(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    /**
     * 读取指定位置、指定目录下的指定文件，并返回字符串类型内容
     *
     * @param destination 本地文件存放根地址
     * @param directory   文件坐在文件夹
     * @param fileName    文件名。可以是包含相对路径的文件名。
     * @return 文件内容
     * @throws IOException IO 异常
     */
    default String readString(String destination, String directory, String fileName) throws IOException {
        Path path = getPath(destination, directory, fileName);
        return readString(path);
    }

    /**
     * 读取默认位置、指定目录下的指定文件，并返回字符串类型内容
     *
     * @param directory 文件坐在文件夹
     * @param fileName  文件名。可以是包含相对路径的文件名。
     * @return 文件内容
     * @throws IOException IO 异常
     */
    default String readString(String directory, String fileName) throws IOException {
        return readString(getDefaultDestination(), directory, fileName);
    }

    /**
     * 读取默认位置（根目录）下的指定文件，并返回字符串类型内容
     *
     * @param fileName 文件名。可以是包含相对路径的文件名。
     * @return 文件内容
     * @throws IOException IO 异常
     */
    default String readString(String fileName) throws IOException {
        return readString(null, fileName);
    }

    /**
     * 读取指定路径对应的文件，创建输入流（需要手动关闭流）
     *
     * @param path 文件路径
     * @return 流 {@link InputStream}
     * @throws IOException IO 异常
     */
    default InputStream newInputStream(Path path) throws IOException {
        return Files.newInputStream(path, StandardOpenOption.READ);
    }

    /**
     * 读取指定位置、指定目录下的指定文件，创建输入流（需要手动关闭流）
     *
     * @param destination 本地文件存放根地址
     * @param directory   文件坐在文件夹
     * @param fileName    文件名。可以是包含相对路径的文件名。
     * @return 流 {@link InputStream}
     * @throws IOException IO 异常
     */
    default InputStream newInputStream(String destination, String directory, String fileName) throws IOException {
        Path path = getPath(destination, directory, fileName);
        return newInputStream(path);
    }

    /**
     * 读取默认位置、指定目录下的指定文件，创建输入流（需要手动关闭流）
     *
     * @param directory 文件坐在文件夹
     * @param fileName  文件名。可以是包含相对路径的文件名。
     * @return 流 {@link InputStream}
     * @throws IOException IO 异常
     */
    default InputStream newInputStream(String directory, String fileName) throws IOException {
        return newInputStream(getDefaultDestination(), directory, fileName);
    }

    /**
     * 读取指定位置（根目录）下的指定文件，创建输入流（需要手动关闭流）
     *
     * @param fileName 文件名。可以是包含相对路径的文件名。
     * @return 流 {@link InputStream}
     * @throws IOException IO 异常
     */
    default InputStream newInputStream(String fileName) throws IOException {
        return newInputStream(null, fileName);
    }

    /**
     * 判断指定路径对应文件是否存在
     *
     * @param path 文件路径 {@link Path}
     * @return 是否存在。true 存在；false 不存在。
     */
    default boolean exists(Path path) {
        return Files.exists(path);
    }

    /**
     * 判断指定位置、指定目录下指定的文件是否存在
     *
     * @param destination 本地文件存放根地址
     * @param directory   文件坐在文件夹
     * @param fileName    文件名。可以是包含相对路径的文件名。
     * @return 是否存在。true 存在；false 不存在。
     */
    default boolean exists(String destination, String directory, String fileName) throws IOException {
        Path path = getPath(destination, directory, fileName);
        return exists(path);
    }

    /**
     * 判断默认位置、指定目录下指定的文件是否存在
     *
     * @param directory 文件坐在文件夹
     * @param fileName  文件名。可以是包含相对路径的文件名。
     * @return 是否存在。true 存在；false 不存在。
     */
    default boolean exists(String directory, String fileName) throws IOException {
        return exists(getDefaultDestination(), directory, fileName);
    }

    /**
     * 判断默认位置（根目录）下指定的文件是否存在
     *
     * @param fileName 文件名。可以是包含相对路径的文件名。
     * @return 是否存在。true 存在；false 不存在。
     */
    default boolean exists(String fileName) throws IOException {
        return exists(null, fileName);
    }

    /**
     * 读取指定路径对应文件属性
     *
     * @param path 文件路径 {@link Path}
     * @return 文件属性 {@link BasicFileAttributes}
     * @throws IOException IO 异常
     */
    default BasicFileAttributes readAttributes(Path path) throws IOException {
        return Files.readAttributes(path, BasicFileAttributes.class);
    }

    /**
     * 读取指定位置、指定目录下的指定文件属性
     *
     * @param destination 本地文件存放根地址
     * @param directory   文件坐在文件夹
     * @param fileName    文件名。可以是包含相对路径的文件名。
     * @return 文件属性 {@link BasicFileAttributes}
     * @throws IOException IO 异常
     */
    default BasicFileAttributes readAttributes(String destination, String directory, String fileName) throws IOException {
        Path path = getPath(destination, directory, fileName);
        return readAttributes(path);
    }

    /**
     * 读取默认位置、指定目录下的指定文件属性
     *
     * @param directory 文件坐在文件夹
     * @param fileName  文件名。可以是包含相对路径的文件名。
     * @return 文件属性 {@link BasicFileAttributes}
     * @throws IOException IO 异常
     */
    default BasicFileAttributes readAttributes(String directory, String fileName) throws IOException {
        return readAttributes(getDefaultDestination(), directory, fileName);
    }

    /**
     * 读取默认位置（根目录）下的指定文件属性
     *
     * @param fileName 文件名。可以是包含相对路径的文件名。
     * @return 文件属性 {@link BasicFileAttributes}
     * @throws IOException IO 异常
     */
    default BasicFileAttributes readAttributes(String fileName) throws IOException {
        return readAttributes(null, fileName);
    }
}
