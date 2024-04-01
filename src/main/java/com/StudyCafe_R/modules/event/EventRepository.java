package com.StudyCafe_R.modules.event;

import com.StudyCafe_R.modules.event.domain.Event;
import com.StudyCafe_R.modules.study.domain.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface EventRepository extends JpaRepository<Event,Long> {
    @EntityGraph(value = "Event.withEnrollments", type = EntityGraph.EntityGraphType.LOAD)
    List<Event> findByStudyOrderByStartDateTime(Study study);
}
