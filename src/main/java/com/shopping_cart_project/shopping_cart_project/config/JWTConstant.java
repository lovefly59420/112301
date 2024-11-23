package com.shopping_cart_project.shopping_cart_project.config;

import io.github.cdimascio.dotenv.Dotenv;

public class JWTConstant {
    static Dotenv dotenv = Dotenv.load();
    // 存放加密用的常數，可以自行設定，只要有32個以上的英文或數字組成就可以
    public static final String SECRET = dotenv.get("JWT_CONSTANT");
}
