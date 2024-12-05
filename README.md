<h3 align="center">
  <img src="docs/NightJoiner-Title.png">
  <br>
  <br>
  <p>Paper plugin that gives your player ability to set custom join/quit message</p>
  <br>
  <br>
  <u>English</u> | <b><a href="README_RU.md">Russian</a></b>
</h3>

***

# 🚀 Features

- Store player messages in database with HikariCP:
    - [x] SQLite
    - [x] MySQL
- On-join **message of the day (MOTD)** for players with PlaceholderAPI placeholders support
- Ability to **block** player for changing join message
- Set default messages **in config** for blocked or players without permission to choose custom message
- **HEX Codes** by MiniMessage support
- **Vanish**
  plugin ([SuperVanish](https://www.spigotmc.org/resources/1331/), [PremiumVanish](https://www.spigotmc.org/resources/14404/), ...)
  support

# 💾 Requirements

- Java **17+**
- Paper (or forks such as Purpur) **1.18+** <u>(not Spigot/CraftBukkit)</u>
- **PlaceholderAPI** plugin

# ⚡ Commands and permissions

## /nightjoiner

Main plugin command

> [!TIP]
> Alias: **/nj**

#### Usage:

- **/nightjoiner reload** - Reload plugin
    - Permission: `nightjoiner.admin.reload`
- **/nightjoiner ban <player>** - Ban player
    - Permission: `nightjoiner.admin.ban`
- **/nightjoiner unban <player>** - Unban player
    - Permission: `nightjoiner.admin.unban`
- **/nightjoiner reset <player>** - Reset player messages
    - Permission: `nightjoiner.admin.reset`

## Messages manage (for players)

- Set message for join - **/setjoin <text>**
    - Permission: `nightjoiner.player.setjoin`
- Set message for quit - **/setquit <text>**
    - Permission: `nightjoiner.player.setquit`
- Remove message for join - **/resetjoin**
- Remove message for quit - **/resetquit**

## Broadcast player join/quit:

- `nightjoiner.player.broadcast.join` - Broadcast player join
- `nightjoiner.player.broadcast.quit` - Broadcast player quit

# 🌐 Translations

If you need alaready translated files, check [this](docs/translations/TRANSLATIONS.md)



***

# ⚙ Other information

### If you find a bug or want to help with development, feel free to contact me

- Contact links [here](https://drakoshaslv.ru/)

### You can also (optionally) donate me:

- [DonationAlerts](https://www.donationalerts.com/r/mrdrag0nxyt)
- TON:
  ```
  UQAwUJ_DWQ26_b94mFAy0bE1hrxVRHrq51umphFPreFraVL2
  ```
- ETH:
  ```
  0xf5D0Ab258B0f8EeA7EA07cF1050B35cc12E06Ab0
  ```

<h3 align="center">Made specially for <a href="https://nshard.ru">NightShard</a></h3>
