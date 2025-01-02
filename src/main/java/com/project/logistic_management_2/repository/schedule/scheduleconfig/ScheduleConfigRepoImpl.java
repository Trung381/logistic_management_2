package com.project.logistic_management_2.repository.schedule.scheduleconfig;

import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import static com.project.logistic_management_2.entity.QScheduleConfig.scheduleConfig;

import com.project.logistic_management_2.entity.ScheduleConfig;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ScheduleConfigRepoImpl extends BaseRepo implements ScheduleConfigRepoCustom {
    public ScheduleConfigRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<ScheduleConfigDTO> getAll() {
        return query.from(scheduleConfig)
                .where(scheduleConfig.deleted.eq(false))
                .select(
                        Projections.fields(ScheduleConfigDTO.class,
                                scheduleConfig.id.as("id"),
                                scheduleConfig.placeA.as("placeA"),
                                scheduleConfig.placeB.as("placeB"),
                                scheduleConfig.amount.as("amount"),
                                scheduleConfig.note.as("note"),
                                scheduleConfig.createdAt.as("createdAt"),
                                scheduleConfig.updatedAt.as("updatedAt"))
                )
                .fetch();
    }

    @Override
    public Optional<ScheduleConfig> getByID(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(scheduleConfig.id.eq(id))
                .and(scheduleConfig.deleted.eq(false));

        return Optional.ofNullable(
                query.from(scheduleConfig)
                        .where(builder)
                        .select(scheduleConfig)
                        .fetchOne()
        );
    }

    @Override
    @Modifying
    @Transactional
    public long delete(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(scheduleConfig.id.eq(id))
                .and(scheduleConfig.deleted.eq(false));

        return query.update(scheduleConfig)
                .where(builder)
                .set(scheduleConfig.deleted, true)
                .execute();
    }
}
