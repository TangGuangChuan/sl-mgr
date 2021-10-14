package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.data.SlDeviceVoData;
import com.zdxr.cc.mgr.sl.entity.SlDevice;
import com.zdxr.cc.mgr.sl.enums.base.ModelStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface SlDeviceMapper extends SlMapper<SlDevice> {



    @SelectProvider(type = SqlProvider.class, method = "selectList")
    List<SlDeviceVoData> selectList(@Param("status") ModelStatusEnum status, @Param("keyWords") String keyWords);

    @SelectProvider(type = SqlProvider.class, method = "selectCount")
    List<SlDeviceVoData> selectCount(@Param("status") ModelStatusEnum status, @Param("keyWords") String keyWords);

    class SqlProvider {
        public String selectList(@Param("status") ModelStatusEnum status, @Param("keyWords") String keyWords) {
            StringBuilder sb = new StringBuilder("select * from sl_device where id>0");
            if (status != null) {
                sb.append(" and status = #{status}");
            }
            if (StringUtils.isNotBlank(keyWords)) {
                sb.append(" and ( model_name like concat('%',#{keyWords},'%') or  device_name like concat('%',#{keyWords},'%'))");
            }
            sb.append(" order by id");
            return sb.toString();
        }

        public String selectCount(@Param("status") ModelStatusEnum status, @Param("keyWords") String keyWords) {
            StringBuilder sb = new StringBuilder("select * from sl_device where 1=1");
            if (status != null) {
                sb.append(" and status = #{status}");
            }
            if (StringUtils.isNotBlank(keyWords)) {
                sb.append(" and ( model_name like concat('%',#{keyWords},'%') or  device_name like concat('%',#{keyWords},'%'))");
            }
            sb.append(" order by id");
            return sb.toString();
        }
    }
}
