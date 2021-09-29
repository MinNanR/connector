package site.minnan.connector.infrastructure.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricCrypto;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import javafx.scene.control.PasswordField;
import org.springframework.beans.factory.annotation.Value;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * 密码加密器与解密器
 *
 * @author Minnan on 2021/09/28
 */
public class PasswordEncoder {

    @Value("${jwt.secret}")
    private String secret;

    private AES aes;

    private String keyBase64;

    public PasswordEncoder(byte[] key) {
        this.aes = new AES(key);
        this.keyBase64 = Base64.getEncoder().encodeToString(key);
    }

    public PasswordEncoder(String keyBase64) {
        this.keyBase64 = keyBase64;
        byte[] key = Base64.getDecoder().decode(keyBase64);
        this.aes = new AES(key);
    }

    public String getKey() {
        return keyBase64;
    }

    public PasswordEncoder() {
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        this.keyBase64 = Base64.getEncoder().encodeToString(key);
        this.aes = new AES(key);
    }

    /**
     * 加密密码
     *
     * @param password
     * @return
     */
    public String encode(String password) {
        return aes.encryptHex(password);
    }

    /**
     * 解密密码
     *
     * @param encodedPassword
     * @return
     */
    public String decode(String encodedPassword) {
        return aes.decryptStr(encodedPassword);
    }

    /**
     * 检查两个密码是否匹配
     *
     * @param password        输入的密码
     * @param encodedPassword 加密后的密码
     * @return
     */
    public boolean match(String password, String encodedPassword) {
        return Objects.equals(encodedPassword, encode(password));
    }


}
