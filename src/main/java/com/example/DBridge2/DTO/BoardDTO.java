package com.example.DBridge2.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class BoardDTO {

    private int BOARDNUM;
    private String BOARDSUB;
    private String BOARDCONTENT;
    private String BOARDUPTIME;
    private Long USERID;
    private String PRODUCTINFO;
    private int PRODUCTPRICE;
    private int PRODUCTCOUNT;
    private String PRODUCTSTATUS;
}
