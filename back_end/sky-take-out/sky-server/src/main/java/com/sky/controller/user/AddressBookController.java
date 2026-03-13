package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "地址簿接口")
@Slf4j
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBook addressBook) {
        log.info("新增地址: {}", addressBook);
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前用户所有地址")
    public Result<List<AddressBook>> list() {
        log.info("查询当前登录用户的所有地址信息");
        List<AddressBook> addressBookList = addressBookService.list();
        return Result.success(addressBookList);
    }

    /**
     * 根据id查询地址信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        log.info("根据id查询地址信息: {}", id);
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault() {
        log.info("查询默认地址");
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(1);

        List<AddressBook> addressBookList = addressBookService.list(addressBook);
        if (addressBookList != null && !addressBookList.isEmpty()) {
            return Result.success(addressBookList.get(0));
        } else {
            return Result.error("没有查询到默认地址");
        }
    }

    /**
     * 根据id修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        log.info("修改地址: {}", addressBook);
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址: {}", addressBook.getId());
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 根据id删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result delete(Long id) {
        log.info("根据id删除地址: {}", id);
        addressBookService.deleteById(id);
        return Result.success();
    }
}
