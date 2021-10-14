package com.zdxr.cc.mgr.sl.data;

import com.zdxr.cc.mgr.sl.entity.SlDeviceDocRecord;
import com.zdxr.cc.mgr.sl.entity.SlDeviceProjectRecord;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SlSendMsgVoData implements Serializable {

    /**
     * 下发批次ID
     */
    private Integer groupId;

    /**
     * 下发数据
     */
    private List<SlDeviceProjectRecord> list;

    /**
     * 下发文档
     */
    private List<SlDeviceDocRecord> docList;
}
