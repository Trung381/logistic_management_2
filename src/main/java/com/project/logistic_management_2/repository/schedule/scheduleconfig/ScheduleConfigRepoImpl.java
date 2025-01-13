package com.project.logistic_management_2.repository.schedule.scheduleconfig;

import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import static com.project.logistic_management_2.entity.schedule.QScheduleConfig.scheduleConfig;

import com.project.logistic_management_2.entity.schedule.ScheduleConfig;
import com.project.logistic_management_2.enums.Pagination;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
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

    private final ConstructorExpression<ScheduleConfigDTO> constructorExpression =
            Projections.constructor(ScheduleConfigDTO.class,
                    scheduleConfig.id.as("id"),
                    scheduleConfig.placeA.as("placeA"),
                    scheduleConfig.placeB.as("placeB"),
                    scheduleConfig.amount.as("amount"),
                    scheduleConfig.note.as("note"),
                    scheduleConfig.createdAt.as("createdAt"),
                    scheduleConfig.updatedAt.as("updatedAt"));

    @Override
    public List<ScheduleConfigDTO> getAll(int page) {
        long offset = (long) (page - 1) * Pagination.TEN.getSize();
        return query.from(scheduleConfig)
                .where(scheduleConfig.deleted.eq(false))
                .select(constructorExpression)
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }

    @Override
    public List<ScheduleConfigDTO> getAll() {
        return query.from(scheduleConfig)
                .where(scheduleConfig.deleted.eq(false))
                .select(constructorExpression)
                .fetch();
    }

    BooleanBuilder initGetOneBuilder(String id) {
        return new BooleanBuilder()
                .and(scheduleConfig.id.eq(id))
                .and(scheduleConfig.deleted.eq(false));
    }

    @Override
    public Optional<ScheduleConfig> getByID(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
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
        BooleanBuilder builder = initGetOneBuilder(id);
        return query.update(scheduleConfig)
                .where(builder)
                .set(scheduleConfig.deleted, true)
                .execute();
    }

    @Override
    public long countByID(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        Long res = query.from(scheduleConfig)
                .where(builder)
                .select(scheduleConfig.id.count().coalesce(0L))
                .fetchOne();

        return res == null ? 0 : res;
    }
}
