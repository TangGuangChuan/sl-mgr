package com.zdxr.cc.mgr.sl.service;

import com.zdxr.cc.mgr.sl.data.SlDeviceProjectConfigInfoVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceProjectConfigSaveVoData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 实验项目配置
 */
public interface ISlDeviceProjectConfigService {
    /**
     * 试验项目保存
     *
     * @param voData 配置数据
     */
    void save(SlDeviceProjectConfigSaveVoData voData);

    /**
     * 查询型号批次设置
     * @param deviceId 数据ID
     * @return
     */
    SlDeviceProjectConfigInfoVoData selectList(Integer deviceId);

    /**
     * 导入excel
     *
     * @param deviceId 数据ID
     * @param file 文件
     */
    void importExcel(Integer deviceId, MultipartFile file);

    /**
     * 导出excel
     * @param deviceId 数据ID
     * @param response
     */
    void exportExcel(Integer deviceId, HttpServletResponse response);
}
