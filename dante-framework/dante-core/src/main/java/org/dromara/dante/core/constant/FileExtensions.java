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

package org.dromara.dante.core.constant;

/**
 * <p>Description: 文件名后缀 </p>
 *
 * @author : gengwei.zheng
 * @date : 2026/1/7 22:04
 */
public interface FileExtensions {

    // 文档类型
    String DOC = "doc";
    String DOCX = "docx";
    String PDF = "pdf";
    String PPT = "ppt";
    String PPTX = "pptx";
    String XLS = "xls";
    String XLSX = "xlsx";
    String ZIP = "zip";

    String SUFFIX_DOC = SymbolConstants.PERIOD + DOC;
    String SUFFIX_DOCX = SymbolConstants.PERIOD + DOCX;
    String SUFFIX_PDF = SymbolConstants.PERIOD + PDF;
    String SUFFIX_PPT = SymbolConstants.PERIOD + PPT;
    String SUFFIX_PPTX = SymbolConstants.PERIOD + PPTX;
    String SUFFIX_XLS = SymbolConstants.PERIOD + XLS;
    String SUFFIX_XLSX = SymbolConstants.PERIOD + XLSX;
    String SUFFIX_ZIP = SymbolConstants.PERIOD + ZIP;

    // 配置文件类型
    String PROPERTIES = "properties";
    String JSON = "json";
    String XML = "xml";
    String YAML = "yaml";
    String YML = "yml";

    String SUFFIX_JSON = SymbolConstants.PERIOD + JSON;
    String SUFFIX_PROPERTIES = SymbolConstants.PERIOD + PROPERTIES;
    String SUFFIX_XML = SymbolConstants.PERIOD + XML;
    String SUFFIX_YAML = SymbolConstants.PERIOD + YAML;
    String SUFFIX_YML = SymbolConstants.PERIOD + YML;

    // PKI 系统相关类型
    String CRT = "crt";
    String JKS = "jks";
    String KEY = "key";
    String P12 = "p12";
    String PEM = "pem";

    String SUFFIX_CRT = SymbolConstants.PERIOD + CRT;
    String SUFFIX_JKS = SymbolConstants.PERIOD + JKS;
    String SUFFIX_KEY = SymbolConstants.PERIOD + KEY;
    String SUFFIX_P12 = SymbolConstants.PERIOD + P12;
    String SUFFIX_PEM = SymbolConstants.PERIOD + PEM;
}
