package com.example.DBridge2.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class COMMENTDTO {

    private int COMMENTNUM;
    private int USERID;
    private int BOARDNUM;
    private String NICKNAME;
    private String COMMENTCONTENT;
}
