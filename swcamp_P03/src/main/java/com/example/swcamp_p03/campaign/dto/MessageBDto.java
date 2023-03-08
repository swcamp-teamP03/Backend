package com.example.swcamp_p03.campaign.dto;

import lombok.Getter;

@Getter
public class MessageBDto {
    public Long messageBCnt;
    public Long messageSuccessCnt;
    public Double uniqueCTR;

    public MessageBDto(Long messageBCnt, Long clickCnt, Long messageSuccessCnt) {
        this.messageBCnt = messageBCnt;
        this.messageSuccessCnt = messageSuccessCnt;
        double ctr = clickCnt / (double) messageSuccessCnt;
        this.uniqueCTR = Math.round(ctr * 10000) / 100.0;
    }
}
