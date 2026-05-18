package com.stylelens.www.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stylelens.www.common.Result;
import com.stylelens.www.dto.UserCreateDTO;
import com.stylelens.www.dto.UserUpdateDTO;
import com.stylelens.www.dto.UserVO;
import com.stylelens.www.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Result<UserVO> createUser(@Valid @RequestBody UserCreateDTO dto) {
        return Result.success(userService.createUser(dto));
    }

    @GetMapping("/{id}")
    public Result<UserVO> getUser(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @GetMapping
    public Result<Page<UserVO>> listUsers(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(userService.listUsers(pageNum, pageSize));
    }

    @PutMapping
    public Result<UserVO> updateUser(@Valid @RequestBody UserUpdateDTO dto) {
        return Result.success(userService.updateUser(dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}
