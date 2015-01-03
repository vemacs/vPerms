package me.vemacs.vperms;

import lombok.Getter;
import lombok.Setter;
import me.vemacs.vperms.storage.GroupDataSource;
import me.vemacs.vperms.storage.RedisDataSource;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class vPermsPlugin extends JavaPlugin {
    @Getter
    private static vPermsPlugin instance;
    @Getter
    private static JedisPool jedisPool;
    @Getter @Setter
    private static GroupDataSource dataSource;

    @Override
    public void onEnable() {
        instance = this;
        String ip = getConfig().getString("ip");
        int port = getConfig().getInt("port");
        String password = getConfig().getString("password");
        if (password == null || password.equals(""))
            jedisPool = new JedisPool(new JedisPoolConfig(), ip, port, 0);
        else
            jedisPool = new JedisPool(new JedisPoolConfig(), ip, port, 0, password);
        dataSource = new RedisDataSource();
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}