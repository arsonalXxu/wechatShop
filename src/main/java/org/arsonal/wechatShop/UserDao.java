package org.arsonal.wechatShop;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.arsonal.wechatShop.generate.User;
import org.arsonal.wechatShop.generate.UserExample;
import org.arsonal.wechatShop.generate.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDao {
    private final SqlSessionFactory sqlSessionFactory;

    @Autowired
    public UserDao(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void insertUser(User user) {

        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            final UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.insert(user);
        }

    }

    public User getUserByTel(String tel) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            final UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            UserExample userExample = new UserExample();
            userExample.createCriteria().andTelEqualTo(tel);
            return mapper.selectByExample(userExample).get(0);
        }
    }
}
