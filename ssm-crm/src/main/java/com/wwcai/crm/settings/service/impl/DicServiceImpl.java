package com.wwcai.crm.settings.service.impl;

import com.wwcai.crm.settings.dao.DicTypeDao;
import com.wwcai.crm.settings.dao.DicValueDao;
import com.wwcai.crm.settings.domain.DicType;
import com.wwcai.crm.settings.domain.DicValue;
import com.wwcai.crm.settings.service.DicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {

   /* private DicTypeDao dicTypeDao =
            SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao =
            SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);*/

   @Resource
   private DicTypeDao dicTypeDao;
   @Resource
   private DicValueDao dicValueDao;

    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String, List<DicValue>> map = new HashMap<>();

        // 将字典类型取出
        List<DicType> dtList = dicTypeDao.getTypeList();

        // 将字典类型遍历
        for(DicType dt : dtList) {
            // 取得每一种类型的字典类型编码
            String code = dt.getCode();

            // 根据每一个字典类型来取得字典值的列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            map.put(code + "List", dvList);

        }

        return map;
    }
}
