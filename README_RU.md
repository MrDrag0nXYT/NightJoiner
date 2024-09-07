<center>
<img src="docs/NightJoiner-Title.png">
<p><b>NightJoiner</b> - плагин для Paper, который даёт возможность игрокам выбрать сообщение для входа или выхода на сервер</p>

<b><a href="README.md">English</a></b> | <u>Russian</u>
</center>

***

# 🚀 Особенности

- Хранение данных и сообщений игроков в базе данных (ну хоть не в YAML) с помощью библиотеки HikariCP:
  - [x] SQLite
  - [ ] MySQL ( #todo )
- **Сообщение дня (MOTD)** при входе игрока на сервер **с поддержкой плейсхолдеров PlaceholderAPI**
- Возможность **block** player for changing join message
- Установка стандартных сообщений **в конфиге** для заблокированных игроков или игроков без права на установку своего сообщения
- Поддержка **HEX цветов** через MiniMessage

# 💾 Технические требования

- Java **17 или новее**
- Paper (или форки, такие как Purpur) версии **1.18 и новее** <u>(не Spigot/CraftBukkit)</u>

# ⚡ Права и команды

## /nightjoiner
Главная команда плагина

> [!TIP]
> Сокращённый вариант: **/nj**

#### Использование:
- **/nightjoiner reload** - Перезагрузить плагин
  - Право: `nightjoiner.admin.reload`
- **/nightjoiner ban <ник>** - Заблокировать игрока
  - Право: `nightjoiner.admin.ban`
- **/nightjoiner unban <ник>** - Разблокировать игрока
  - Право: `nightjoiner.admin.unban`

## Управление сообщениями (для игроков)
- Установить сообщение для входа - **/setjoin <текст>**
  - Право: `nightjoiner.player.setjoin`
- Установить сообщение для выхода - **/setquit <текст>**
  - Право: `nightjoiner.player.setquit`
- Убрать сообщение для входа - **/resetjoin**
- Убрать сообщение для выхода - **/resetquit**

## Оповещать о входе/выходе:
- `nightjoiner.player.broadcast.join` - Оповещать о входе
- `nightjoiner.player.broadcast.quit` - Оповещать о выходе



***



# ⚙ Дополнительно

### Если вы нашли баг или хотите помочь в разработке - не стесняйтесь обращаться ко мне
  - Ссылки на контакты [тут](https://drakoshaslv.ru/)

### Также (по желанию) вы можете дать мне денег:
  - [DonationAlerts](https://www.donationalerts.com/r/mrdrag0nxyt)
  - TON:
    ```
    UQAwUJ_DWQ26_b94mFAy0bE1hrxVRHrq51umphFPreFraVL2
    ```
  - ETH:
    ```
    0xf5D0Ab258B0f8EeA7EA07cF1050B35cc12E06Ab0
    ```



<center><h3>Сделано специально для <a href="https://nshard.ru">NightShard</a></h3></center>
