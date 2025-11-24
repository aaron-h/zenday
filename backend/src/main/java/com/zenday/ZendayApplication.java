package com.zenday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 禅修功课管理系统 - 主启动类
 * Zenday Meditation Practice System - Main Application
 */
@SpringBootApplication
public class ZendayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZendayApplication.class, args);
        System.out.println("""

            ╔════════════════════════════════════════╗
            ║   Zenday Meditation Practice System   ║
            ║        禅修功课管理系统已启动           ║
            ║                                        ║
            ║   API: http://localhost:8080/api      ║
            ║   H2:  http://localhost:8080/api/h2-console
            ╚════════════════════════════════════════╝

            """);
    }
}
