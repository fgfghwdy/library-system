package lxx.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("borrow_record")
public class BorrowRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;    // 借书人ID
    private Integer bookId;    // 图书ID
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
}
