package com.sellermatch.process.apply.repositiory;

import com.sellermatch.process.apply.domain.Apply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface ApplyRepository extends PagingAndSortingRepository<Apply, Integer> {

    Page<Apply> findAll(Pageable pageable);

    int countByApplyMemIdAndApplyProjIdAndApplyType(String applyMemId, String applyProjId, String applyType);

    @Query(value = "UPDATE ApplyList " +
            "SET Apply_proj_state = :applyProjState, Apply_update_date = now() " +
            "WHERE Apply_id = :applyId AND Apply_proj_id = :applyProjId AND Apply_type = :ApplyType", nativeQuery = true)
    int updateApply(@Param("applyProjState") String applyProjState, @Param("applyId") String applyId, @Param("applyProjId") String applyProjId, @Param("ApplyType") String ApplyType);
}
