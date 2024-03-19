# bukkit-alipay

**这只是一个例子, 用于演示如何在Minecraft服务器中使用支付宝支付.**

Bukkit插件对接支付宝支付, 用于Minecraft服务器内购买物品.

![GitHub top language](https://img.shields.io/github/languages/top/enaium/bukkit-alipay?style=flat-square&logo=kotlin)

## 数据库

![Static Badge](https://img.shields.io/badge/-PostgreSQL-gray?style=flat-square&logo=postgresql&logoColor=white)

`src/test/resources/schema.sql`

## 配置

- 直接修改源代码
- 设置系统环境变量: `ALIPAY_APP_ID`, `ALIPAY_MERCHANT_PRIVATE_KEY`, `ALIPAY_PUBLIC_KEY`.

## 使用

- `/alipay goods` 查看所有商品
- `/alipay place <goods>` 下单
- `/alipay pay <trade-id>` 支付
- `/alipay take <trade-id>` 提货