package ai.turbochain.ipex.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * @author GS
 * @description
 * @date 2018/1/18 11:29
 */
@Configuration
public class QueryDslConfig {
	
	/**
	 * 	装配
	 * @param entityManager
	 * @return
	 */
    @Bean
    public JPAQueryFactory getJPAQueryFactory(EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }
}
