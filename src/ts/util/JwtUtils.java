package ts.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * JwtUtils
 * 参见 https://ninghao.net/blog/2834
 * iss: token的发行者
 * sub: token的题目
 * aud: token的客户
 * exp: 经常使用的，以数字时间定义失效期，也就是当前时间以后的某个时间本token失效
 * nbf: 定义在此时间之前，JWT不会接受处理。
 * iat: JWT发布时间，能用于决定JWT年龄
 * jti: JWT唯一标识. 能用于防止 JWT重复使用，一次只用一个token
 *
 */
public class JwtUtils {
    private static final String base64Security = "base64Security";
    private static final String issuer = "ZZU";
    private static final long TTLMillis = 1000*60*60*24; //生存期 24 hours

    public static Claims parseJWT(String jsonWebToken) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                .parseClaimsJws(jsonWebToken).getBody();
    }

    public static String createJWT(String name, String phone, String audience) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long newMillis = System.currentTimeMillis();
        Date now = new Date(newMillis);
        //生成签名密钥 就是一个base64加密后的字符串
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("phone", phone);
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("alg", "HS256") //header
                .setIssuedAt(now)//创建时间
                .setSubject(jsonObject.toString())//主题，也差不多是个人的一些信息
                .setIssuer(issuer)//签发者
                .setAudience(audience)//个人签名
                .signWith(signatureAlgorithm, signingKey);//第三段密钥
        //添加Token过期时间
        //过期时间
        long expMillis = newMillis+TTLMillis;
        //现在是什么时间
        Date exp = new Date(expMillis);
        //系统时间之前的token都是不可以被承认的
        builder.setExpiration(exp).setNotBefore(now);
        //生成JWT
        return builder.compact();
    }

    public static void main(String[] args) {
        String s= createJWT("王芮", "1234", "1");
        System.out.println(s);
        System.out.println(parseJWT(s));
    }

}
