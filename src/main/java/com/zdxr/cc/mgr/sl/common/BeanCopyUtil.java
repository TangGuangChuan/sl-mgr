package com.zdxr.cc.mgr.sl.common;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BeanCopyUtil {


    public static <T> List<T> copyList(List list, Class<T> clazz) {
        List<T> ls = new ArrayList<>();
        try {
            if (list != null && list.size() > 0) {
                for (Object obj : list) {
                    T t = clazz.newInstance();
                    BeanCopyUtil.copy(obj, t);
                    ls.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ls;
    }

    public static Object copy(Object source, Object target) {
        if (source != null) {
            BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        }
        return target;
    }

    /**
     * 获取空字段
     *
     * @param source
     * @return
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
