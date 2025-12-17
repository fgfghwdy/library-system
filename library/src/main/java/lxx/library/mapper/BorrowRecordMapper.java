package lxx.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lxx.library.entity.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 借阅记录表 Mapper
 * 负责操作 borrow_record 表
 */
@Mapper
public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {
}
