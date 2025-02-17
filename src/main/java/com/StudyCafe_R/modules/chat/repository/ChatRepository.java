package com.StudyCafe_R.modules.chat.repository;

import com.StudyCafe_R.modules.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
