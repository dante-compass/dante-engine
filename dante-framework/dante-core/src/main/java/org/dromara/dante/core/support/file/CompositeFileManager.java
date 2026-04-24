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

import org.dromara.dante.core.domain.FileAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>Description: 统一定义的文件管理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2026/1/4 22:12
 */
public interface CompositeFileManager {

    /**
     * 获取对象存储传输器实例 {@link OssTransformer}
     *
     * @return 对象存储传输器 {@link OssTransformer}
     */
    OssTransformer getOssTransformer();

    /**
     * 获取文件操作模板 {@link FileTemplate}
     *
     * @return 文件操作模板 {@link OssTransformer}
     */
    FileTemplate getFileTemplate();

    /**
     * 指定当前组合文件管理器默认目录
     *
     * @return 默认目录
     */
    String getDefaultDirectory();

    /**
     * 指定当前组合文件管理器默认存储桶
     *
     * @return 默认存储桶
     */
    String getDefaultBucketName();


    default Path getPath(String destination, String directory, String fileName) throws IOException {
        return getFileTemplate().getPath(destination, directory, fileName);
    }

    default Path getPath(String directory, String fileName) throws IOException {
        return getFileTemplate().getPath(directory, fileName);
    }

    default Path getPath(String fileName) throws IOException {
        return getPath(getDefaultDirectory(), fileName);
    }

    private InputStream newInputStream(Path path) throws IOException {
        return getFileTemplate().newInputStream(path);
    }

    default InputStream newInputStream(String destination, String directory, String fileName) throws IOException {
        return newInputStream(getPath(destination, directory, fileName));
    }

    default InputStream newInputStream(String directory, String fileName) throws IOException {
        return newInputStream(getPath(directory, fileName));
    }

    default InputStream newInputStream(String fileName) throws IOException {
        return newInputStream(getPath(fileName));
    }

    /**
     * 将本地指定文件上传到 OSS 中指定的存储桶下
     *
     * @param bucketName OSS 存储同名称
     * @param path       文件路径 {@link Path}
     * @return 文件属性 {@link FileAttributes}
     * @throws IOException 文件操作异常
     */
    private FileAttributes upload(String bucketName, Path path) throws IOException {
        // 检测确保新生成的文件存在
        if (getFileTemplate().exists(path)) {
            boolean success = getOssTransformer().upload(bucketName, path);
            if (success) {
                return FileAttributes.with(path).bucketName(bucketName).build();
            } else {
                // 如果上传失败，则删除本地文件，保证两端文件的一致性
                getFileTemplate().delete(path);
                return null;
            }
        }

        return null;
    }

    /**
     * 将字符串类型内容写入本地指定位置、指定目录下的指定文件。如果已经写入成功后，再上传至 OSS 指定存储桶。
     * <p>
     * 任意文件操作失败，都将抛出 {@link IOException}。文件上传 OSS 文件失败，将返回 null 并删除已生成文件，确保本地于 OSS 文件的一致性。
     *
     * @param bucketName OSS 存储桶名称
     * @param path       文件路径 {@link Path}
     * @param content    字符串内容 {@link Supplier}
     * @return 文件属性 {@link FileAttributes} 或者 null
     * @throws IOException 文件操作异常
     */
    private FileAttributes writeString(String bucketName, Path path, Supplier<String> content) throws IOException {

        // 如果文件已经存储在，则先删除
        if (getFileTemplate().exists(path)) {
            getFileTemplate().delete(path);
        }

        // 写入文件
        Path result = getFileTemplate().writeString(path, content.get());
        // 上传文件
        return upload(bucketName, result);
    }

    /**
     * 将字符串类型内容写入本地指定位置、指定目录下的指定文件。如果已经写入成功后，再上传至 OSS 指定存储桶。
     * <p>
     * 任意文件操作失败，都将抛出 {@link IOException}。文件上传 OSS 文件失败，将返回 null 并删除已生成文件，确保本地于 OSS 文件的一致性。
     *
     * @param bucketName  OSS 存储桶名称
     * @param destination 本地文件存储位置
     * @param directory   文件存储目录
     * @param fileName    文件名
     * @param content     字符串内容  {@link Supplier}
     * @return 文件属性 {@link FileAttributes} 或者 null
     * @throws IOException 文件操作异常
     */
    default FileAttributes writeString(String bucketName, String destination, String directory, String fileName, Supplier<String> content) throws IOException {
        return writeString(bucketName, getPath(destination, directory, fileName), content);
    }

    /**
     * 将字符串类型内容写入本地指定位置、指定目录下的指定文件。如果已经写入成功后，再上传至 OSS 指定存储桶。
     * <p>
     * 任意文件操作失败，都将抛出 {@link IOException}。文件上传 OSS 文件失败，将返回 null 并删除已生成文件，确保本地于 OSS 文件的一致性。
     *
     * @param bucketName OSS 存储桶名称
     * @param directory  文件存储目录
     * @param fileName   文件名
     * @param content    字符串内容  {@link Supplier}
     * @return 文件属性 {@link FileAttributes} 或者 null
     * @throws IOException 文件操作异常
     */
    default FileAttributes writeString(String bucketName, String directory, String fileName, Supplier<String> content) throws IOException {
        return writeString(bucketName, getPath(directory, fileName), content);
    }

    /**
     * 将字符串类型内容写入本地指定位置、指定目录下的指定文件。如果已经写入成功后，再上传至 OSS 指定存储桶。
     * <p>
     * 任意文件操作失败，都将抛出 {@link IOException}。文件上传 OSS 文件失败，将返回 null 并删除已生成文件，确保本地于 OSS 文件的一致性。
     *
     * @param bucketName OSS 存储桶名称
     * @param fileName   文件名
     * @param content    字符串内容  {@link Supplier}
     * @return 文件属性 {@link FileAttributes} 或者 null
     * @throws IOException 文件操作异常
     */
    default FileAttributes writeString(String bucketName, String fileName, Supplier<String> content) throws IOException {
        return writeString(bucketName, getPath(fileName), content);
    }

    /**
     * 将字符串类型内容写入本地默认位置、默认目录下的指定文件。如果已经写入成功后，再上传至 OSS 默认存储桶。
     * <p>
     * 任意文件操作失败，都将抛出 {@link IOException}。文件上传 OSS 文件失败，将返回 null 并删除已生成文件，确保本地于 OSS 文件的一致性。
     *
     * @param fileName 文件名
     * @param content  字符串内容  {@link Supplier}
     * @return 文件属性 {@link FileAttributes} 或者 null
     * @throws IOException 文件操作异常
     */
    default FileAttributes writeString(String fileName, Supplier<String> content) throws IOException {
        return writeString(getDefaultBucketName(), fileName, content);
    }


    /**
     * 将输出流 {@link OutputStream} 型内容写入本地指定位置、指定目录下的指定文件。如果已经写入成功后，再上传至 OSS 指定存储桶。
     * <p>
     * 任意文件操作失败，都将抛出 {@link IOException}。文件上传 OSS 文件失败，将返回 null 并删除已生成文件，确保本地于 OSS 文件的一致性。
     *
     * @param bucketName OSS 存储桶名称
     * @param path       文件路径 {@link Path}
     * @param writer     输出流写入器 {@link Consumer}
     * @return 文件属性 {@link FileAttributes} 或者 null
     * @throws IOException 件操作异常
     */
    private FileAttributes writeOutputStream(String bucketName, Path path, Consumer<OutputStream> writer) throws IOException {
        try (OutputStream outputStream = getFileTemplate().newOutputStream(path)) {
            writer.accept(outputStream);
            return upload(bucketName, path);
        }
    }

    /**
     * 将输出流 {@link OutputStream} 型内容写入本地指定位置、指定目录下的指定文件。如果已经写入成功后，再上传至 OSS 指定存储桶。
     * <p>
     * 任意文件操作失败，都将抛出 {@link IOException}。文件上传 OSS 文件失败，将返回 null 并删除已生成文件，确保本地于 OSS 文件的一致性。
     *
     * @param bucketName  OSS 存储桶名称
     * @param destination 本地文件存储位置
     * @param directory   文件存储目录
     * @param fileName    文件名
     * @param writer      输出流写入器 {@link Consumer}
     * @return 文件属性 {@link FileAttributes} 或者 null
     * @throws IOException 件操作异常
     */
    default FileAttributes writeOutputStream(String bucketName, String destination, String directory, String fileName, Consumer<OutputStream> writer) throws IOException {
        return writeOutputStream(bucketName, getPath(destination, directory, fileName), writer);
    }

    /**
     * 将输出流 {@link OutputStream} 型内容写入本地指定位置、指定目录下的指定文件。如果已经写入成功后，再上传至 OSS 指定存储桶。
     * <p>
     * 任意文件操作失败，都将抛出 {@link IOException}。文件上传 OSS 文件失败，将返回 null 并删除已生成文件，确保本地于 OSS 文件的一致性。
     *
     * @param bucketName OSS 存储桶名称
     * @param directory  文件存储目录
     * @param fileName   文件名
     * @param writer     输出流写入器 {@link Consumer}
     * @return 文件属性 {@link FileAttributes} 或者 null
     * @throws IOException 件操作异常
     */
    default FileAttributes writeOutputStream(String bucketName, String directory, String fileName, Consumer<OutputStream> writer) throws IOException {
        return writeOutputStream(bucketName, getPath(directory, fileName), writer);
    }

    /**
     * 将输出流 {@link OutputStream} 型内容写入本地指定位置、指定目录下的指定文件。如果已经写入成功后，再上传至 OSS 指定存储桶。
     * <p>
     * 任意文件操作失败，都将抛出 {@link IOException}。文件上传 OSS 文件失败，将返回 null 并删除已生成文件，确保本地于 OSS 文件的一致性。
     *
     * @param bucketName OSS 存储桶名称
     * @param fileName   文件名
     * @param writer     输出流写入器 {@link Consumer}
     * @return 文件属性 {@link FileAttributes} 或者 null
     * @throws IOException 件操作异常
     */
    default FileAttributes writeOutputStream(String bucketName, String fileName, Consumer<OutputStream> writer) throws IOException {
        return writeOutputStream(bucketName, getPath(fileName), writer);
    }

    /**
     * 将输出流 {@link OutputStream} 型内容写入本地指定位置、指定目录下的指定文件。如果已经写入成功后，再上传至 OSS 指定存储桶。
     * <p>
     * 任意文件操作失败，都将抛出 {@link IOException}。文件上传 OSS 文件失败，将返回 null 并删除已生成文件，确保本地于 OSS 文件的一致性。
     *
     * @param fileName 文件名
     * @param writer   输出流写入器 {@link Consumer}
     * @return 文件属性 {@link FileAttributes} 或者 null
     * @throws IOException 文件操作异常
     */
    default FileAttributes writeOutputStream(String fileName, Consumer<OutputStream> writer) throws IOException {
        return writeOutputStream(getDefaultBucketName(), fileName, writer);
    }

    /**
     * 读取指定路径对应的文件，并返回字符串类型内容。如果本地不存在对应文件，尝试从 OSS 指定存储桶中下载，然后在此读取。
     *
     * @param bucketName OSS 存储桶名称
     * @param path       文件路径 {@link Path}
     * @return 字符串内容 或者 null
     * @throws IOException 文件操作异常
     */
    private String readString(String bucketName, Path path) throws IOException {
        if (getFileTemplate().exists(path)) {
            return getFileTemplate().readString(path);
        }

        boolean success = getOssTransformer().download(bucketName, path);
        if (success) {
            return getFileTemplate().readString(path);
        } else {
            return null;
        }
    }

    /**
     * 读取指定路径对应的文件，并返回字符串类型内容。如果本地不存在对应文件，尝试从 OSS 指定存储桶中下载，然后在此读取。
     *
     * @param bucketName  OSS 存储桶名称
     * @param destination 本地文件存储位置
     * @param directory   文件存储目录
     * @param fileName    文件名
     * @return 字符串内容 或者 null
     * @throws IOException 文件操作异常
     */
    default String readString(String bucketName, String destination, String directory, String fileName) throws IOException {
        return readString(bucketName, getPath(destination, directory, fileName));
    }

    /**
     * 读取指定路径对应的文件，并返回字符串类型内容。如果本地不存在对应文件，尝试从 OSS 指定存储桶中下载，然后在此读取。
     *
     * @param bucketName OSS 存储桶名称
     * @param directory  文件存储目录
     * @param fileName   文件名
     * @return 字符串内容 或者 null
     * @throws IOException 文件操作异常
     */
    default String readString(String bucketName, String directory, String fileName) throws IOException {
        return readString(bucketName, getPath(directory, fileName));
    }

    /**
     * 读取指定路径对应的文件，并返回字符串类型内容。如果本地不存在对应文件，尝试从 OSS 指定存储桶中下载，然后在此读取。
     *
     * @param bucketName OSS 存储桶名称
     * @param fileName   文件名
     * @return 字符串内容 或者 null
     * @throws IOException 文件操作异常
     */
    default String readString(String bucketName, String fileName) throws IOException {
        return readString(bucketName, getPath(fileName));
    }

    /**
     * 读取指定路径对应的文件，并返回字符串类型内容。如果本地不存在对应文件，尝试从 OSS 指定存储桶中下载，然后在此读取。
     *
     * @param fileName 文件名
     * @return 字符串内容 或者 null
     * @throws IOException 文件操作异常
     */
    default String readString(String fileName) throws IOException {
        return readString(getDefaultBucketName(), fileName);
    }

    /**
     * 以输入流 {@link InputStream} 读取指定路径对应的文件，并返回字符串类型内容。如果本地不存在对应文件，尝试从 OSS 指定存储桶中下载，然后在此读取。
     *
     * @param bucketName OSS 存储同名称
     * @param path       文件路径 {@link Path}
     * @param function   输入流读取功能 {@link Function}
     * @param <T>        返回数据的类型
     * @return 返回指定类型数据
     * @throws IOException 文件操作异常
     */
    private <T> T readInputStream(String bucketName, Path path, Function<InputStream, T> function) throws IOException {

        if (getFileTemplate().exists(path)) {
            try (InputStream inputStream = getFileTemplate().newInputStream(path)) {
                return function.apply(inputStream);
            }
        }

        boolean success = getOssTransformer().download(bucketName, path);
        if (success) {
            try (InputStream inputStream = getFileTemplate().newInputStream(path)) {
                return function.apply(inputStream);
            }
        }

        return null;
    }

    /**
     * 以输入流 {@link InputStream} 读取指定路径对应的文件，并返回字符串类型内容。如果本地不存在对应文件，尝试从 OSS 指定存储桶中下载，然后在此读取。
     *
     * @param bucketName  OSS 存储桶名称
     * @param destination 本地文件存储位置
     * @param directory   文件存储目录
     * @param fileName    文件名
     * @param function    输入流读取功能 {@link Function}
     * @param <T>         返回数据的类型
     * @return 返回指定类型数据
     * @throws IOException 文件操作异常
     */
    default <T> T readInputStream(String bucketName, String destination, String directory, String fileName, Function<InputStream, T> function) throws IOException {
        return readInputStream(bucketName, getPath(destination, directory, fileName), function);
    }

    /**
     * 以输入流 {@link InputStream} 读取指定路径对应的文件，并返回字符串类型内容。如果本地不存在对应文件，尝试从 OSS 指定存储桶中下载，然后在此读取。
     *
     * @param bucketName OSS 存储桶名称
     * @param directory  文件存储目录
     * @param fileName   文件名
     * @param function   输入流读取功能 {@link Function}
     * @param <T>        返回数据的类型
     * @return 返回指定类型数据
     * @throws IOException 文件操作异常
     */
    default <T> T readInputStream(String bucketName, String directory, String fileName, Function<InputStream, T> function) throws IOException {
        return readInputStream(bucketName, getPath(directory, fileName), function);
    }

    /**
     * 以输入流 {@link InputStream} 读取指定路径对应的文件，并返回字符串类型内容。如果本地不存在对应文件，尝试从 OSS 指定存储桶中下载，然后在此读取。
     *
     * @param bucketName OSS 存储桶名称
     * @param fileName   文件名
     * @param function   输入流读取功能 {@link Function}
     * @param <T>        返回数据的类型
     * @return 返回指定类型数据
     * @throws IOException 文件操作异常
     */
    default <T> T readInputStream(String bucketName, String fileName, Function<InputStream, T> function) throws IOException {
        return readInputStream(bucketName, getPath(fileName), function);
    }

    /**
     * 以输入流 {@link InputStream} 读取指定路径对应的文件，并返回字符串类型内容。如果本地不存在对应文件，尝试从 OSS 指定存储桶中下载，然后在此读取。
     *
     * @param fileName 文件名
     * @param function 输入流读取功能 {@link Function}
     * @param <T>      返回数据的类型
     * @return 返回指定类型数据
     * @throws IOException 文件操作异常
     */
    default <T> T readInputStream(String fileName, Function<InputStream, T> function) throws IOException {
        return readInputStream(getDefaultBucketName(), fileName, function);
    }

    /**
     * 以读取字符串的方式读取指定路径对应的文件，并转换成指定类型对象
     *
     * @param bucketName OSS 存储同名称
     * @param path       文件路径 {@link Path}
     * @param function   字符串读取功能 {@link Function}
     * @param <T>        返回数据的类型
     * @return 返回指定类型数据
     * @throws IOException 文件操作异常
     */
    private <T> T readString(String bucketName, Path path, Function<String, T> function) throws IOException {
        if (getFileTemplate().exists(path)) {
            String content = getFileTemplate().readString(path);
            return function.apply(content);
        }
        boolean success = getOssTransformer().download(bucketName, path);
        if (success) {
            String content = getFileTemplate().readString(path);
            return function.apply(content);
        }

        return null;
    }

    /**
     * 以读取字符串的方式读取指定路径对应的文件，并转换成指定类型对象
     *
     * @param bucketName  OSS 存储桶名称
     * @param destination 本地文件存储位置
     * @param directory   文件存储目录
     * @param fileName    文件名
     * @param function    字符串读取功能 {@link Function}
     * @param <T>         返回数据的类型
     * @return 返回指定类型数据
     * @throws IOException 文件操作异常
     */
    default <T> T readString(String bucketName, String destination, String directory, String fileName, Function<String, T> function) throws IOException {
        return readString(bucketName, getPath(destination, directory, fileName), function);
    }

    /**
     * 以读取字符串的方式读取指定路径对应的文件，并转换成指定类型对象
     *
     * @param bucketName OSS 存储桶名称
     * @param directory  文件存储目录
     * @param fileName   文件名
     * @param function   字符串读取功能 {@link Function}
     * @param <T>        返回数据的类型
     * @return 返回指定类型数据
     * @throws IOException 文件操作异常
     */
    default <T> T readString(String bucketName, String directory, String fileName, Function<String, T> function) throws IOException {
        return readString(bucketName, getPath(directory, fileName), function);
    }

    /**
     * 以读取字符串的方式读取指定路径对应的文件，并转换成指定类型对象
     *
     * @param bucketName OSS 存储桶名称
     * @param fileName   文件名
     * @param function   字符串读取功能 {@link Function}
     * @param <T>        返回数据的类型
     * @return 返回指定类型数据
     * @throws IOException 文件操作异常
     */
    default <T> T readString(String bucketName, String fileName, Function<String, T> function) throws IOException {
        return readString(bucketName, getPath(fileName), function);
    }

    /**
     * 以读取字符串的方式读取指定路径对应的文件，并转换成指定类型对象
     *
     * @param fileName 文件名
     * @param function 字符串读取功能 {@link Function}
     * @param <T>      返回数据的类型
     * @return 返回指定类型数据
     * @throws IOException 文件操作异常
     */
    default <T> T readString(String fileName, Function<String, T> function) throws IOException {
        return readString(getDefaultBucketName(), fileName, function);
    }

    /**
     * 删除本地指定路径对应的文件以及 OSS 中指定存储桶中对应的文件
     *
     * @param bucketName OSS 存储同名称
     * @param path       文件路径 {@link Path}
     * @return 是否删除成功。true 成功；false 失败
     * @throws IOException 文件操作异常
     */
    private boolean delete(String bucketName, Path path) throws IOException {
        // 本地存在则默认对象存储中也存在该文件
        if (getFileTemplate().exists(path)) {
            // 先删除对象存储中的文件
            boolean success = getOssTransformer().remove(bucketName, path.getFileName().toString());
            // 对象存储中删除成功后，再删除本地文件。否则，则不删除本地文件。
            if (success) {
                return getFileTemplate().delete(path);
            }
        }

        return false;
    }


    /**
     * 删除本地指定路径对应的文件以及 OSS 中指定存储桶中对应的文件
     *
     * @param bucketName  OSS 存储桶名称
     * @param destination 本地文件存储位置
     * @param directory   文件存储目录
     * @param fileName    文件名
     * @return 是否删除成功。true 成功；false 失败
     * @throws IOException 文件操作异常
     */
    default boolean delete(String bucketName, String destination, String directory, String fileName) throws IOException {
        return delete(bucketName, getPath(destination, directory, fileName));
    }

    /**
     * 删除本地指定路径对应的文件以及 OSS 中指定存储桶中对应的文件
     *
     * @param bucketName OSS 存储桶名称
     * @param directory  文件存储目录
     * @param fileName   文件名
     * @return 是否删除成功。true 成功；false 失败
     * @throws IOException 文件操作异常
     */
    default boolean delete(String bucketName, String directory, String fileName) throws IOException {
        return delete(bucketName, getPath(directory, fileName));
    }

    /**
     * 删除本地指定路径对应的文件以及 OSS 中指定存储桶中对应的文件
     *
     * @param bucketName OSS 存储桶名称
     * @param fileName   文件名
     * @return 是否删除成功。true 成功；false 失败
     * @throws IOException 文件操作异常
     */
    default boolean delete(String bucketName, String fileName) throws IOException {
        return delete(bucketName, getPath(fileName));
    }

    /**
     * 删除本地指定路径对应的文件以及 OSS 中指定存储桶中对应的文件
     *
     * @param fileName 文件名
     * @return 是否删除成功。true 成功；false 失败
     * @throws IOException 文件操作异常
     */
    default boolean delete(String fileName) throws IOException {
        return delete(getDefaultBucketName(), fileName);
    }

    /**
     * 检测本地地是否存在文件。如果本地不存在，则先尝试从 OSS 下载到本地后，再进行检测。
     *
     * @param bucketName OSS 存储同名称
     * @param path       文件路径 {@link Path}
     * @return 文件是否存在。true 存在；false 不存在；
     */
    private boolean exists(String bucketName, Path path) {
        if (getFileTemplate().exists(path)) {
            return true;
        } else {
            boolean success = getOssTransformer().download(bucketName, path);
            if (success) {
                return getFileTemplate().exists(path);
            } else {
                return false;
            }
        }
    }

    /**
     * 检测本地地是否存在文件。如果本地不存在，则先尝试从 OSS 下载到本地后，再进行检测。
     *
     * @param bucketName  OSS 存储桶名称
     * @param destination 本地文件存储位置
     * @param directory   文件存储目录
     * @param fileName    文件名
     * @return 文件是否存在。true 存在；false 不存在；
     * @throws IOException 文件操作异常
     */
    default boolean exists(String bucketName, String destination, String directory, String fileName) throws IOException {
        return exists(bucketName, getPath(destination, directory, fileName));
    }

    /**
     * 检测本地地是否存在文件。如果本地不存在，则先尝试从 OSS 下载到本地后，再进行检测。
     *
     * @param bucketName OSS 存储桶名称
     * @param directory  文件存储目录
     * @param fileName   文件名
     * @return 文件是否存在。true 存在；false 不存在；
     * @throws IOException 文件操作异常
     */
    default boolean exists(String bucketName, String directory, String fileName) throws IOException {
        return exists(bucketName, getPath(directory, fileName));
    }

    /**
     * 检测本地地是否存在文件。如果本地不存在，则先尝试从 OSS 下载到本地后，再进行检测。
     *
     * @param bucketName OSS 存储桶名称
     * @param fileName   文件名
     * @return 文件是否存在。true 存在；false 不存在；
     * @throws IOException 文件操作异常
     */
    default boolean exists(String bucketName, String fileName) throws IOException {
        return exists(bucketName, getPath(fileName));
    }

    /**
     * 检测本地地是否存在文件。如果本地不存在，则先尝试从 OSS 下载到本地后，再进行检测。
     *
     * @param fileName 文件名
     * @return 文件是否存在。true 存在；false 不存在；
     * @throws IOException 文件操作异常
     */
    default boolean exists(String fileName) throws IOException {
        return exists(getDefaultBucketName(), fileName);
    }
}
