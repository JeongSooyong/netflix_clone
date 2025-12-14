package com.soo.netflix_clone.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soo.netflix_clone.vo.ActorVo;
import com.soo.netflix_clone.vo.MovieVo;

@Repository // DB에 접근하는 것을 명시하는 어노테이션
public class ActorDaoImpl implements IActorDao {

    // MyBatis와 Spring이 통합될때 사용되는 객체
    private final SqlSessionTemplate session;

    // 매퍼(Mapper).xml 파일의 네임스페이스를 저장하는 문자열 NS
    private final String NS = "com.soo.netflix_clone.model.ActorDaoImpl.";

    @Autowired
    public ActorDaoImpl(SqlSessionTemplate session) {
        this.session = session;
    }

    // 영화 출연 배우
    @Override
    public List<ActorVo> selectActorsByMovie(int movieNo) {
        return session.selectList(NS + "selectActorsByMovie", movieNo);
    }

    // 배우 조회
    @Override
    public ActorVo selectActor(int actorNo) {
        return session.selectOne(NS + "selectActor", actorNo);
    }

    // 배우 등록
    @Override
    public int insertActor(ActorVo vo) {
        return session.insert(NS + "insertActor", vo);
    }

    // 배우 전체 조회
    @Override
    public List<ActorVo> selectAllActor() {
        return session.selectList(NS + "selectAllActor");
    }

    // 배우 검색
    @Override
    public List<ActorVo> searchActor(String keyword) {
        return session.selectList(NS + "searchActor", keyword);
    }

    // 출연 배우 insert
    @Override
    public int insertMovieActors(int movieNo, List<Integer> actorNos) {

        // // 삽입할 배우가 없으면 0을 반환
        if (actorNos == null || actorNos.isEmpty()) {
            return 0; 
        }
        
        // 여러 매개변수를 MyBatis 매퍼로 전달하기 위해 Map 사용
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("movieNo", movieNo);
        paramMap.put("actorNos", actorNos); 

        return session.insert(NS + "insertMovieActors", paramMap);
    }

    // 출연 배우 조회
    @Override
    public List<ActorVo> getActorsByMovie(int movieNo) {
        return session.selectOne(NS + "getActorsByMovie", movieNo);
    }
    
    // 배우 출연 영화 조회
    @Override
    public List<MovieVo> selectMoviesByActorNo(int actorNo) {
        return session.selectList(NS + "selectMoviesByActorNo", actorNo);
    }

    // 배우 정보 수정
    @Override
    public int updateActor(ActorVo vo) {
        return session.update(NS + "updateActor", vo);
    }

    // 배우 비공개
    @Override
    public int actorPrivate(int actorNo) {
        return session.update(NS + "actorPrivate", actorNo);
    }


}
