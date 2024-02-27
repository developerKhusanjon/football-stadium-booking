package org.stadium.corelib.repo.impl;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository
public class UserSessionRepositoryImpl {
    private final EntityManager entityManager;

    public UserSessionRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Modifying
    @Transactional
    public boolean enableUserWithConfirmationCode(String token, String code) {
        String jpql = "update users\n" +
                "set is_enable = true \n" +
                "from user_credential\n" +
                "         join user_session session\n" +
                "              on user_credential.id = session.user_credentials_id\n" +
                "where session.user_token like (:token)\n" +
                "  and sms_code like (:code)\n" +
                "  and (session.created_at >= (now() - interval '15 minute') or (session.updated_at >= (now() - interval '15 minute')))\n";
        try {
            Query query = entityManager.createNativeQuery(jpql);
            query.setParameter("token", token);
            query.setParameter("code", code);
            int isUpdate = query.executeUpdate();
            if(isUpdate > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return false;
    }
}
