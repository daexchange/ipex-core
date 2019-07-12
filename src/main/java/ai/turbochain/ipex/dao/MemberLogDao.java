package ai.turbochain.ipex.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import ai.turbochain.ipex.entity.MemberLog;


public interface MemberLogDao extends MongoRepository<MemberLog,Long> {
}
