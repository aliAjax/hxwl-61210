package com.hxwl.app61210.inspector;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InspectorMapper {

    int insert(Inspector inspector);

    int updateById(Inspector inspector);

    int deleteById(@Param("id") Long id);

    Inspector selectById(@Param("id") Long id);

    List<Inspector> selectList(@Param("name") String name,
                               @Param("phone") String phone,
                               @Param("responsibleZone") String responsibleZone,
                               @Param("enabled") Integer enabled);

    List<Inspector> selectAllEnabled();
}
