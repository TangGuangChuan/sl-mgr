package com.zdxr.cc.mgr.sl.service;

import com.zdxr.cc.mgr.sl.data.ModelInvoVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceInfoVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceNoUpdateVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceNoVoData;
import com.zdxr.cc.mgr.sl.enums.base.ModelStatusEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 设备型号批次
 */
public interface ISlDeviceService {
    /**
     * 新增
     *
     * @param voData 批次数据
     */
    void insert(SlDeviceInfoVoData voData);

    /**
     * 修改
     *
     * @param voData 批次数据
     */
    void update(SlDeviceInfoVoData voData);

    /**
     * 删除
     *
     * @param id 任务ID
     */
    void deleteById(Integer id);

    /**
     * 查询编号
     *
     * @param deviceId 任务ID
     * @return
     */
    List<SlDeviceNoVoData> selectDeviceNoList(Integer deviceId);

    /**
     * 更新编号
     *
     * @param voData
     */
    void updateDeviceNo(SlDeviceNoUpdateVoData voData);

    /**
     * 首页总览
     *
     * @param status   状态
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return
     */
    ModelInvoVoData selectList(ModelStatusEnum status, String keyWords, Integer pageNum, Integer pageSize);


    /**
     * 导出zip
     * @param deviceId
     */
    void reportZip(Integer deviceId, HttpServletResponse response);
}
