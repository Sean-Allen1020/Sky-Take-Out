package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {


    @Autowired
    AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     * @param addressBook
     */
    public void save(AddressBook addressBook) {

        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    /**
     * 条件查询
     * @return
     */
    public List<AddressBook> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        return addressBookMapper.list(addressBook);
    }

    /**
     * 条件查询(查询默认地址)
     * @param addressBook
     * @return
     */
    public List<AddressBook> list(AddressBook addressBook) {
        return addressBookMapper.list(addressBook);
    }

    /**
     * 根据id查询地址信息
     * @param id
     * @return
     */
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    /**
     * 根据id修改地址
     * @param addressBook
     */
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook
     */
    @Transactional
    public void setDefault(AddressBook addressBook) {

        // 先将用户所有默认地址设为非默认，防止多地址默认
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.updateIsDefaultByUserId(addressBook);

        // 再将传入的地址设为默认地址
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }

    /**
     * 根据id删除地址
     * @param id
     */
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }
}
