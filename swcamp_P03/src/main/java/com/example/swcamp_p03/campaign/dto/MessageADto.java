package com.example.swcamp_p03.campaign.dto;

import lombok.Getter;

@Getter
public class MessageADto {
    public Long messageACnt;
    public Long clickCnt;
    public Long messageSuccessCnt;
    public Double uniqueCTR;

    public MessageADto(Long messageACnt, Long clickCnt, Long messageSuccessCnt) {
        this.messageACnt = messageACnt;
        this.clickCnt = clickCnt;
        this.messageSuccessCnt = messageSuccessCnt;
        double ctr = clickCnt / (double) messageSuccessCnt;
        this.uniqueCTR = Math.round(ctr * 10000) / 100.0;
    }
}
