package lxx.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lxx.library.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
