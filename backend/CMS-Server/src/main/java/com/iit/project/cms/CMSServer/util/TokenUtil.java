package com.iit.project.cms.CMSServer.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.iit.project.cms.CMSServer.entity.User;

import java.util.Date;

import static com.iit.project.cms.CMSServer.common.Symbol.*;

public class TokenUtil {

    public static String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(KEY_SECRET);
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + 2592000000L);
        return JWT.create()
                .withIssuer(KEY_ISSUE)
                .withIssuedAt(now)
                .withExpiresAt(expireTime)
                .withClaim(KEY_USER, new Gson().toJson(user))
                .sign(algorithm);
    }
}
