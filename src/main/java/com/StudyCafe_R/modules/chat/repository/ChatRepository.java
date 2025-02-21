package com.StudyCafe_R.modules.chat.repository;

import com.StudyCafe_R.modules.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {


    @Query("SELECT c FROM Chat c WHERE c.studyPath = ?1 ORDER BY c.createdAt ASC")
    List<Chat> findChatsByStudyPath(String studyPath);

}
