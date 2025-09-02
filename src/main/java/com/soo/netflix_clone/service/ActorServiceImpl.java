package com.soo.netflix_clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soo.netflix_clone.model.IActorDao;
import com.soo.netflix_clone.vo.ActorVo;


@Service // 비즈니스 로직을 명시하는 어노테이션
public class ActorServiceImpl implements IActorService {

    // IActorDao 자동 주입
    @Autowired
    private IActorDao dao;

    // 배우 조회
    @Override
    public ActorVo selectActor(int actorNo) {
        return dao.selectActor(actorNo);
    }

}
