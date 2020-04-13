package com.jingjun.dao;

import com.jingjun.domain.Goods;
import com.jingjun.domain.MiaoshaGoods;
import com.jingjun.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 *
 */
@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from goods g left join miaosha_goods mg on g.id=mg.goods_id")
    public List<GoodsVo> listAllGoods();

    @Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from goods g left join miaosha_goods mg on g.id=mg.goods_id where g.id=#{goodId}")
    GoodsVo getGoodById(@Param("goodId") long goodId);

    @Update("update miaosha_goods set stock_count=stock_count-1 where goods_id=#{goodsId} and stock_count>0")
    int reduceStock(MiaoshaGoods goods);
}
