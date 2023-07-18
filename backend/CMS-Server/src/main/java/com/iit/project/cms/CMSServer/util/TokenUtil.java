package com.iit.project.cms.CMSServer.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.iit.project.cms.CMSServer.common.Symbol;
import com.iit.project.cms.CMSServer.entity.User;

import java.util.Date;

import static com.iit.project.cms.CMSServer.common.Symbol.*;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class TokenUtil {

    public static String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(KEY_SECRET);
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + TOKEN_EXPIRES_TIME);
        return JWT.create()
                .withIssuer(KEY_ISSUE)
                .withIssuedAt(now)
                .withExpiresAt(expireTime)
                .withClaim(KEY_USER, new Gson().toJson(user))
                .sign(algorithm);
    }

    public static boolean isValidJwt(String jwtToken) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(KEY_SECRET)).build();
            DecodedJWT decodedJWT = verifier.verify(jwtToken);
            // 验证是否在有效期内
            // 验证是否在有效期内
            if (decodedJWT.getExpiresAt().before(new Date())) {
                return false; // JWT 已过期
            }
            // 验证uid是不是同一个人
            //todo
            return true;
        } catch (JWTVerificationException ex) {
            return false; // JWT 验证失败
        }
    }
}
