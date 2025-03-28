package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrdersTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0/5 * * * * ?") // 每分钟触发一次
    public void processTimeOutOrders() {
        log.info("处理超时订单：{}", LocalDateTime.now());
        // select * from orders where status = ? and order_time < (当前时间-15)
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> orders = orderMapper.getByStatusAndOrderTimeLT(Orders.UN_PAID, time);
        if (orders != null && orders.size() > 0) {
            for (Orders order : orders) {
                order.setPayStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时，自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨一点
    public void processDeliveryOrder() {
        log.info("处理配送完成，未更新订单：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orders = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if (orders != null && orders.size() > 0) {
            for (Orders order : orders) {
                order.setPayStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }
}
