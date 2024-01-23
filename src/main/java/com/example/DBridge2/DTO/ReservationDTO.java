package com.example.DBridge2.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReservationDTO {

    private int BOARDNUM;
    private int USERID;
    private int HOSTUSERID;
    private String BOARDSUB;
    private String NICKNAME;
    private int AMOUNT;
    private int PRODUCTPRICE;
    private int QUANTITY;
    private int RESERVEID;
    private String DBTOKEN;
}
