## 🌅 Sunshine Grace

This mod adds a "grace period" for players who are joining an existing world for the first time.

All new players will receive 10 minutes of Sunshine Grace, which will protect them from all hostile mobs as long as they stay on the surface. Attacking a hostile mob will remove the effect.

Sunshine Grace aims to assist new players joining in the middle of the night and lets those players get settled before having to deal with the dangers of the dark.

## 🖥️ Download

| Modrinth | CurseForge |
|:---:|:---:|
| <div align="center">[![Modrinth Downloads](https://img.shields.io/modrinth/dt/PjYGDgNf?logo=modrinth)](https://modrinth.com/project/PjYGDgNf)</div> | <div align="center">[![CurseForge Downloads](https://cf.way2muchnoise.eu/full_1634042.svg)](https://www.curseforge.com/minecraft/mc-mods/sunshine-grace)</div> |

## 🔧 Config

Different changes to how the mod functions can be changed in the config file.

```
{
  "enableFirstJoinEffect": true,
  "enableAttackingMonsterRemovesEffect": true,
  "duration": 12000,
  "minimumYValue": 60.0
}
```

- `enableFirstJoinEffect`: Toggle granting new players the effect
  - **default:** `true`
- `enableAttackingMonsterRemovesEffect`: Toggle attacking a hostile mob removing the effect
  - **default:** `true`
- `duration`: Duration (in ticks) that the effect lasts for 
  - **default:** `12000` (10 minutes)
- `minimumYValue`: The minimum Y level that a player can be before hostile mobs aggro
  - **default**: `60.0`

## ⚙️ Modpack Usage

Sunshine Grace is free to be used in any custom mod packs as long as credit is provided. Compatibility with other mods has not been tested for.

## 🐛 Issues

If you happen to experience any issues while using this mod, please report it to our [GitHub issue tracker](https://github.com/Sir-Cow/sunshine-grace/issues)! Please be as clear as possible when reporting any bugs or issues.
