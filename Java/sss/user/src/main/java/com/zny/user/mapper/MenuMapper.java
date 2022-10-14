package com.zny.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.user.model.menu.MenuModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date:2022/9/2
 * 菜单mapper
 */

@Repository
@Mapper
public interface MenuMapper extends BaseMapper<MenuModel> {
}
