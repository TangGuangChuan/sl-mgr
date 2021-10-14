package com.zdxr.cc.mgr.sl.service;

import com.zdxr.cc.mgr.sl.data.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 数据发送记录
 */
public interface ISlDeviceSendGroupService {
    /**
     * 新增记录
     *
     * @param voData 数据信息
     */
    void insert(SlDeviceSendGroupInsertVoData voData);

    /**
     * 修改
     *
     * @param voData 数据信息
     */
    void update(SlDeviceSendGroupUpdateVoData voData);


    /**
     * 删除
     *
     * @param id 数据id
     */
    void deleteById(Integer id);


    /**
     * 删除下发数据
     *
     * @param voData
     * @return
     */
    Boolean deleteSendDataById(SendGroupDeleteVoData voData);

    /**
     * 查询分组情况
     *
     * @param deviceId 设备ID
     * @return
     */
    SlDeviceSendGroupInfoVoData selectList(Integer deviceId);

    /**
     * 下发
     *
     * @param groupIds
     */
    void sendGroup(String groupIds);

    /**
     * 操作端上传
     *
     * @param files  附件列表
     * @param voData 上传数据
     */
    void clientUpload(MultipartFile[] files, SlClientUploadVoData voData);

    /**
     * 下发tcp回调
     *
     * @param groupId
     */
    void sendGroupCallBack(Integer groupId);

    /**
     * 删除下发数据回调
     *
     * @param voData
     */
    void deleteSendDataCallBack(DeleteDataVoData voData);

    /**
     * 查看tcp当前连接
     *
     * @return
     */
    List<String> selectOperatorClient();
}
