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

$.sm2 = {
    createKeyPair: () => {
        return sm2.generateKeyPairHex();
    },

    encrypt: (content, publicKey) => {
        return '04' + sm2.doEncrypt(content, publicKey, this.cipherMode);
    },

    decrypt: (content, privateKey) => {
        let data = content.substring(2).toLocaleLowerCase();
        return sm2.doDecrypt(data, privateKey, this.cipherMode);
    }
}

$.sm4 = {
    encrypt: (content, publicKey) => {
        return sm4.encrypt(content, publicKey);
    },

    decrypt: (content, privateKey) => {
        return sm4.decrypt(content, privateKey);
    }
}

$.security = {
    exchange: (url, sessionId, backendPublicKey) => {
        const pair = $.sm2.createKeyPair();
        const encryptData = $.sm2.encrypt(pair.publicKey, backendPublicKey);

        return new Promise((resolve, reject) => {
            $.http.post(url, {publicKey: encryptData, sessionId: sessionId}, "json")
                .then(result => {
                    console.log("------exchange data------", result)
                    const confidential = result.data;
                    const key = $.sm2.decrypt(confidential, pair.privateKey);
                    console.log("------key------", key)
                    resolve(key);
                })
                .catch(error => {
                    reject(error);
                });
        })
    },

    captcha: (url, sessionId, category) => {
        return new Promise((resolve, reject) => {
            $.http.get(url, {identity:sessionId, category: category})
                .then(result => {
                    let src = result.data.graphicImageBase64;
                    resolve(src)
                })
                .catch(error => {
                    reject(error);
                });
        })
    },

    encryptSignInFormData: function (tank, fighter, missile, symmetric) {
        return {
            encryptTank: $.sm4.encrypt(tank, symmetric),
            encryptFighter: $.sm4.encrypt(fighter, symmetric),
            encryptMissile: $.sm4.encrypt(missile, symmetric)
        };
    }
}