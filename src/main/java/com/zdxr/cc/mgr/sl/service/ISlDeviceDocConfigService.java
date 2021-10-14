package com.zdxr.cc.mgr.sl.service;

import com.zdxr.cc.mgr.sl.data.SlDeviceDocConfigSaveVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceDocConfigVoData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文档检查项
 */
public interface ISlDeviceDocConfigService {
    /**
     * 保存文档检查项设置
     * @param voData 检查数据
     */
    void save(SlDeviceDocConfigSaveVoData voData);


    /**
     * 查询文档检查项目设置
     *
     * @param deviceId 设备id
     * @return
     */
    List<SlDeviceDocConfigVoData> selectList(Integer deviceId);

    /**
     * 导入excel
     *
     * @param deviceId
     * @param file
     */
    void importExcel(Integer deviceId, MultipartFile file);

    /**
     * 导出excel
     *
     * @param deviceId 设备id
     * @param response
     */
    void exportExcel(Integer deviceId, HttpServletResponse response);
}
