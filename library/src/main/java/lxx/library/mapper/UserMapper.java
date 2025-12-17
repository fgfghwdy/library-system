package lxx.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lxx.library.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper
 * 负责操作 user 表
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // BaseMapper 已经封装了常用数据库操作方法
}
