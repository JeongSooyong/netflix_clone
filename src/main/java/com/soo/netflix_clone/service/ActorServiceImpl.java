package com.soo.netflix_clone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soo.netflix_clone.model.IActorDao;
import com.soo.netflix_clone.vo.ActorVo;
import com.soo.netflix_clone.vo.MovieVo;


@Service // 비즈니스 로직을 명시하는 어노테이션
public class ActorServiceImpl implements IActorService {

    // IActorDao 자동 주입
    @Autowired
    private IActorDao dao;

    // 영화 출연 배우
    @Override
    public List<ActorVo> selectActorsByMovie(int movieNo) {
        return dao.selectActorsByMovie(movieNo);
    }

    // 배우 조회
    @Override
    public ActorVo selectActor(int actorNo) {
        return dao.selectActor(actorNo);
    }

    // 배우 등록
    @Override
    public int insertActor(ActorVo vo) {
        return dao.insertActor(vo);
    }

    // 배우 전체 조회
    @Override
    public List<ActorVo> selectAllActor() {
        return dao.selectAllActor();
    }

    // 배우 검색
    @Override
    public List<ActorVo> searchActor(String keyword) {
        return dao.searchActor(keyword);
    }

    // 출연 배우 insert
    @Override
    public int insertMovieActors(int movieNo, List<Integer> actorNos) {

        if (actorNos == null || actorNos.isEmpty()) {
            return 0;
        }
        
        return dao.insertMovieActors(movieNo, actorNos);
    }

    // 출연 배우 조회
    @Override
    public List<ActorVo> getActorsByMovie(int movieNo) {
        return dao.getActorsByMovie(movieNo);
    }

    // 배우 출연 영화 조회
    @Override
    public List<MovieVo> selectMoviesByActorNo(int actorNo) {
        return dao.selectMoviesByActorNo(actorNo);
    }

    // 배우 정보 수정
    @Override
    public int updateActor(ActorVo vo) {
        return dao.updateActor(vo);
    }

    // 배우 비공개
    public int actorPrivate(int actorNo) {
        return dao.actorPrivate(actorNo);
    }

}
