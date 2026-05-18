package com.stylelens.www.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stylelens.www.dto.UserCreateDTO;
import com.stylelens.www.dto.UserUpdateDTO;
import com.stylelens.www.dto.UserVO;

public interface UserService {

    UserVO createUser(UserCreateDTO dto);

    UserVO getUserById(Long id);

    Page<UserVO> listUsers(Integer pageNum, Integer pageSize);

    UserVO updateUser(UserUpdateDTO dto);

    void deleteUser(Long id);
}
