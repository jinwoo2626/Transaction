package com.example.DBridge2.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BoardListDTO {

    private int BOARDNUM;
    private String BOARDSUB;
    private String BOARDCONTENT;
    private String BOARDUPTIME;
    private String USERID;
    private String PRODUCTINFO;
    private String PRODUCTPRICE;
    private int PRODUCTCOUNT;
    private String PRODUCTSTATUS;
    private String FILEPATH;
    private String NICKNAME;
}
