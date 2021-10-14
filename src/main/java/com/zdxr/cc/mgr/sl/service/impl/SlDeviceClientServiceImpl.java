package com.zdxr.cc.mgr.sl.service.impl;

import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.data.SlDeviceClientVoData;
import com.zdxr.cc.mgr.sl.entity.SlDeviceClient;
import com.zdxr.cc.mgr.sl.mapper.SlDeviceClientMapper;
import com.zdxr.cc.mgr.sl.mapper.SlDeviceDocRecordMapper;
import com.zdxr.cc.mgr.sl.mapper.SlDeviceProjectRecordMapper;
import com.zdxr.cc.mgr.sl.mapper.SlDeviceSendGroupMapper;
import com.zdxr.cc.mgr.sl.service.ISlDeviceClientService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 客户端信息
 */
@Service
public class SlDeviceClientServiceImpl implements ISlDeviceClientService {
    @Autowired
    private SlDeviceClientMapper clientMapper;
    @Autowired
    private SlDeviceSendGroupMapper sendGroupMapper;
    @Autowired
    private SlDeviceProjectRecordMapper projectRecordMapper;
    @Autowired
    private SlDeviceDocRecordMapper docRecordMapper;
    /**
     * 保存操作端
     * @param voData 客户端信息
     */
    @Override
    @Transactional
    public void saveDeviceClient(SlDeviceClientVoData voData) {
        if (StringUtils.isBlank(voData.getClientCode()) || StringUtils.isBlank(voData.getClientName())) {
            throw new BizError("操作端编码和名称都不能为空");
        }
        SlDeviceClient client = clientMapper.findByCode(voData.getClientCode());
        if (client == null) {
            client = new SlDeviceClient();
            client.setClientCode(voData.getClientCode());
            client.setClientName(voData.getClientName());
            clientMapper.insert(client);
        } else {
            client.setClientName(voData.getClientName());
            clientMapper.updateById(client);
            sendGroupMapper.updateClient(client.getClientCode(), client.getClientName());
            projectRecordMapper.updateClient(client.getClientCode(), client.getClientName());
            docRecordMapper.updateClient(client.getClientCode(), client.getClientName());
        }
    }
}
