package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对前端传来的明文密码进行md5加密后做比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    public void save(EmployeeDTO employeeDTO) {
        //1.将DTO类中的属性复制到实体类中
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        //2.手动定义密码为默认值 DEFAULT_PASSWORD(123456) 为md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //3.手动定义新增员工账号状态，默认为ENABLE
        employee.setStatus(StatusConstant.ENABLE);

        //4.手动定义创建时间和更新时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //5.手动设置当前记录创建人id和修改人id
        // 通过拦截器获取
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        //6.将实体类属性传到mapper类
        employeeMapper.insert(employee);
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO){

        //  1.设置分页查询参数：page，pageSize
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        //  2.调用Mapper方法，将结果代入Page类里
        Page<Employee> p = employeeMapper.pageQuery(employeePageQueryDTO);

        //  3.封装结果
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 启用/禁用员工账号
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {

        Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);

//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
    }

    /**
     * 根据id查询员工
     * @param id
     */
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        //直接返回的话，前端就也会看到密码，为了安全，这里特殊处理一下密码
        employee.setPassword("****");
        return employee;
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     */
    public void update(EmployeeDTO employeeDTO) {
        //1.将被修改的信息封装到实体类中
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        //2.手动修改更新时间
//        employee.setUpdateTime(LocalDateTime.now());

        //3.手动修改 修改人id
        // 通过拦截器获取
//        employee.setUpdateUser(BaseContext.getCurrentId());

        //4.将更新内容传到mapper类
        employeeMapper.update(employee);
    }


}
