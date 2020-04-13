package com.jingjun.service;

import com.jingjun.dao.GoodsDao;
import com.jingjun.domain.MiaoshaGoods;
import com.jingjun.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service
public class GoodsService {

    @Resource
    private GoodsDao goodsDao;

    public List<GoodsVo> listAllGoods() {
        return goodsDao.listAllGoods();
    }

    public GoodsVo getGoodById(long goodId) {
        return goodsDao.getGoodById(goodId);
    }

    /**
     * 减少库存
     */
    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int result = goodsDao.reduceStock(g);
        return result > 0;
    }
}
