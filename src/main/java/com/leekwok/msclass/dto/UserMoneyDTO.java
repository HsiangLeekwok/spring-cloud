package com.leekwok.msclass.dto;

import java.math.BigDecimal;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2020/02/15 21:39<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class UserMoneyDTO {

    private Integer userId;
    private BigDecimal money;
    private String event;
    private String description;

    public UserMoneyDTO() {
    }

    public UserMoneyDTO(Integer userId, BigDecimal money, String event, String description) {
        this.userId = userId;
        this.money = money;
        this.event = event;
        this.description = description;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "UserMoneyDTO{" +
                "userId=" + userId +
                ", money=" + money +
                ", event='" + event + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
