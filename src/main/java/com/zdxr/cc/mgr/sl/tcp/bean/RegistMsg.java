package com.zdxr.cc.mgr.sl.tcp.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistMsg extends Base {

    private String clientCode;

    private String clientName;

    private String token;

    private String userId;
}
