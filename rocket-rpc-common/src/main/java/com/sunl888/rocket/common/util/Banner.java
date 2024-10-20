package com.sunl888.rocket.common.util;

public class Banner {
    public static void serverStartup(int port) {
        String banner = """ 
                                                                                      \s
                ,------.              ,--.            ,--.  ,------. ,------.  ,-----.\s
                |  .--. ' ,---.  ,---.|  |,-. ,---. ,-'  '-.|  .--. '|  .--. ''  .--./\s
                |  '--'.'| .-. || .--'|     /| .-. :'-.  .-'|  '--'.'|  '--' ||  |    \s
                |  |\\  \\ ' '-' '\\ `--.|  \\  \\\\   --.  |  |  |  |\\  \\ |  | --' '  '--'\\\s
                `--' '--' `---'  `---'`--'`--'`----'  `--'  `--' '--'`--'      `-----'\s
                """;
        System.out.println(banner);
        System.out.println("RocketRPC 服务启动完成，端口: " + port);
        System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        System.out.println("JDK 版本: " + System.getProperty("java.version"));
        System.out.println("=====================================");
    }
}
