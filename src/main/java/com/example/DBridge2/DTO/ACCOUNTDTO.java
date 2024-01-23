package com.example.DBridge2.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ACCOUNTDTO {

    private String ACCOUNTNUM;
    private String BANKINFO;
    private int AMOUNT;
    private String USERNAME;
    private String DBTOKEN;
}
