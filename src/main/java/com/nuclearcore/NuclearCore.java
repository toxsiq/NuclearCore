package com.nuclearcore;

import com.nuclearcore.caixas.CaixaService;
import com.nuclearcore.bosses.BossService;
import com.nuclearcore.commands.AmuletoCommand;
import com.nuclearcore.commands.ArmazemCommand;
import com.nuclearcore.commands.BossCommand;
import com.nuclearcore.commands.CaixaCommand;
import com.nuclearcore.commands.ChequeCommand;
import com.nuclearcore.commands.IsqueiroCommand;
import com.nuclearcore.commands.KeyCommand;
import com.nuclearcore.commands.LuckyBlockCommand;
import com.nuclearcore.commands.MinaCommand;
import com.nuclearcore.commands.PescaCommand;
import com.nuclearcore.commands.PrestigioCommand;
import com.nuclearcore.commands.RankupCommand;
import com.nuclearcore.commands.RoboCommand;
import com.nuclearcore.commands.SkinUnlockCommand;
import com.nuclearcore.commands.SpawnerCommand;
import com.nuclearcore.commands.UsinaCommand;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.items.ItemFactory;
import com.nuclearcore.items.ItemKeys;
import com.nuclearcore.items.SkinDisplayResolver;
import com.nuclearcore.keys.KeyService;
import com.nuclearcore.luckyblocks.LuckyBlockService;
import com.nuclearcore.mina.MinaService;
import com.nuclearcore.pesca.PescaService;
import com.nuclearcore.plantacoes.PlantacoesService;
import com.nuclearcore.prestiges.PrestigeService;
import com.nuclearcore.rankup.RankService;
import com.nuclearcore.robos.RoboService;
import com.nuclearcore.spawners.SpawnerService;
import com.nuclearcore.usina.UsinaService;
import com.nuclearcore.utils.RewardService;
import org.bukkit.plugin.java.JavaPlugin;

public class NuclearCore extends JavaPlugin {
    private PlayerDataManager dataManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        dataManager = new PlayerDataManager(this);

        EconomyService economyService = new EconomyService(dataManager);
        RankService rankService = new RankService(this, dataManager, economyService);
        PrestigeService prestigeService = new PrestigeService(this, dataManager, economyService);
        SkinDisplayResolver skinDisplayResolver = new SkinDisplayResolver(this);
        ItemKeys itemKeys = new ItemKeys(this);
        ItemFactory itemFactory = new ItemFactory(itemKeys, skinDisplayResolver);
        UsinaService usinaService = new UsinaService(this, dataManager, economyService, skinDisplayResolver);
        MinaService minaService = new MinaService(this, dataManager, economyService, prestigeService);
        PlantacoesService plantacoesService = new PlantacoesService(this, dataManager, economyService);
        PescaService pescaService = new PescaService(this, dataManager, economyService);
        SpawnerService spawnerService = new SpawnerService(this, economyService);
        RoboService roboService = new RoboService(this, economyService);
        RewardService rewardService = new RewardService(this, economyService);
        LuckyBlockService luckyBlockService = new LuckyBlockService(this, rewardService);
        KeyService keyService = new KeyService(this, rewardService);
        CaixaService caixaService = new CaixaService(this, rewardService);
        BossService bossService = new BossService(this, rewardService);

        registerCommands(rankService, prestigeService, usinaService, itemFactory, minaService, plantacoesService,
                pescaService, economyService, itemKeys, roboService, caixaService, dataManager, bossService);
        registerListeners(usinaService, dataManager, economyService, itemKeys, skinDisplayResolver, minaService,
                plantacoesService, pescaService, luckyBlockService, keyService, spawnerService, bossService);

        long autosave = getConfig().getInt("settings.autosave-seconds", 120);
        getServer().getScheduler().runTaskTimerAsynchronously(this, dataManager::saveAll, autosave * 20L, autosave * 20L);
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.saveAll();
        }
    }

    private void registerCommands(RankService rankService,
                                  PrestigeService prestigeService,
                                  UsinaService usinaService,
                                  ItemFactory itemFactory,
                                  MinaService minaService,
                                  PlantacoesService plantacoesService,
                                  PescaService pescaService,
                                  EconomyService economyService,
                                  ItemKeys itemKeys,
                                  RoboService roboService,
                                  CaixaService caixaService,
                                  PlayerDataManager dataManager,
                                  BossService bossService) {
        getCommand("rankup").setExecutor(new RankupCommand(dataManager, rankService));
        getCommand("prestigio").setExecutor(new PrestigioCommand(dataManager, prestigeService));
        getCommand("usina").setExecutor(new UsinaCommand(usinaService));
        getCommand("isqueiro").setExecutor(new IsqueiroCommand(dataManager, itemFactory));
        getCommand("mina").setExecutor(new MinaCommand(minaService, itemFactory));
        getCommand("armazem").setExecutor(new ArmazemCommand(plantacoesService, itemFactory));
        getCommand("pesca").setExecutor(new PescaCommand(pescaService, itemFactory));
        getCommand("amuleto").setExecutor(new AmuletoCommand(dataManager));
        getCommand("cheque").setExecutor(new ChequeCommand(economyService, itemFactory, getConfig().getDouble("cheque.tax", 0.1)));
        getCommand("skinunlock").setExecutor(new SkinUnlockCommand(dataManager));
        getCommand("spawner").setExecutor(new SpawnerCommand(itemFactory));
        getCommand("robo").setExecutor(new RoboCommand(itemFactory, roboService, itemKeys));
        getCommand("luckyblock").setExecutor(new LuckyBlockCommand(itemFactory));
        getCommand("key").setExecutor(new KeyCommand(itemFactory));
        getCommand("caixa").setExecutor(new CaixaCommand(caixaService));
        getCommand("boss").setExecutor(new BossCommand(bossService));
    }

    private void registerListeners(UsinaService usinaService,
                                   PlayerDataManager dataManager,
                                   EconomyService economyService,
                                   ItemKeys itemKeys,
                                   SkinDisplayResolver skinDisplayResolver,
                                   MinaService minaService,
                                   PlantacoesService plantacoesService,
                                   PescaService pescaService,
                                   LuckyBlockService luckyBlockService,
                                   KeyService keyService,
                                   SpawnerService spawnerService,
                                   BossService bossService) {
        var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new com.nuclearcore.listeners.PlayerDataListener(dataManager), this);
        pluginManager.registerEvents(new com.nuclearcore.listeners.IsqueiroListener(this, dataManager, economyService, skinDisplayResolver, itemKeys), this);
        pluginManager.registerEvents(new com.nuclearcore.listeners.UsinaListener(this, usinaService, dataManager, economyService, itemKeys), this);
        pluginManager.registerEvents(new com.nuclearcore.listeners.MinaListener(minaService), this);
        pluginManager.registerEvents(new com.nuclearcore.listeners.PlantacoesListener(this, plantacoesService), this);
        pluginManager.registerEvents(new com.nuclearcore.listeners.PescaListener(pescaService), this);
        pluginManager.registerEvents(new com.nuclearcore.listeners.LuckyBlockListener(luckyBlockService, itemKeys), this);
        pluginManager.registerEvents(new com.nuclearcore.listeners.KeyListener(keyService, itemKeys), this);
        pluginManager.registerEvents(new com.nuclearcore.listeners.ChequeListener(economyService, itemKeys), this);
        pluginManager.registerEvents(new com.nuclearcore.listeners.SpawnerListener(spawnerService, itemKeys), this);
        pluginManager.registerEvents(new com.nuclearcore.listeners.BossListener(bossService), this);
    }
}
