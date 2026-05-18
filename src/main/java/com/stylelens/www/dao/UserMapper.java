package com.stylelens.www.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stylelens.www.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
