package lxx.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("book")
public class Book {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String author;
    private String description;
    private Integer status; // 0=在馆, 1=借出

    // 库存数量
    private Integer stock; // 书的剩余可借数量
}
