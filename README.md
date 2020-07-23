# miaosha2

### 介绍
这是一个秒杀系统，主要应对在高并发下秒杀商品这个业务场景，用到的工具有：
- Spring Boot
- MyBatis
- MySQL
- Redis
- RabbitMQ
- Nginx

### 如何运行？
1. 配置 application.properties 中 Redis、MySQL、RabbitMQ 的相关信息
2. 运行 miaosha2.sql 数据库文件
3. 访问 http://localhost:8080/login.html

### 秒杀场景简要流程
![秒杀流程](https://github.com/Codeman625/miaosha2/blob/master/img/miaosha.jpg '秒杀流程')
1. 服务器启动时，先将商品的库存信息写入redis
2. 收到秒杀请求后，在 Redis 中预减库存
3. 将请求写入 RabbitMQ ，马上发送 “排队中” 的反馈给前端，前端收到请求后便开始轮询秒杀结果
4. 在 RabbitMQ 出队方法中，再次判断库存数量和订单情况
5. 执行“减库存、生成订单”这个事务，并将订单结果写入 Redis 中，等待前端的轮询
6. 前端轮询到 Redis 中的订单信息后，提示“秒杀成功”，结束流程

### 项目难点
- 如何针对像 “商品列表” 等这种 DB 查询结果进行缓存，减少对 DB 的访问？
- 如何解决超卖问题？
- 如何防止恶意用户使用脚本来秒杀商品？
- 登录模块如何做到避免密码的明文传输？
- ………………
