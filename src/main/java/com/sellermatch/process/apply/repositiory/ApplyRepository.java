package com.sellermatch.process.apply.repositiory;

import com.sellermatch.process.apply.domain.Apply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface ApplyRepository extends PagingAndSortingRepository<Apply, Integer> {

    Page<Apply> findAll(Pageable pageable);

    int countByApplyMemIdAndApplyProjIdAndApplyType(String applyMemId, String applyProjId, String applyType);

    @Transactional
    @Modifying
    @Query(value = "UPDATE ApplyList " +
            "SET Apply_proj_state = :applyProjState, Apply_update_date = now() " +
            "WHERE Apply_idx = :ApplyIdx AND Apply_type = :ApplyType", nativeQuery = true)
    int updateApply(@Param("ApplyIdx") Integer ApplyIdx, @Param("applyProjState") String applyProjState, @Param("ApplyType") String ApplyType);

    @Query(value = "SELECT " +
            "apply_idx" +
            ",Mem_id" +
            ",Mem_tel" +
            ",Mem_sort" +
            ",Mem_nick" +
            ",Proj_title" +
            ",Proj_id " +
            "FROM ApplyList al, ProjectList pl, MemList ml " +
            "WHERE ml.Mem_id  = al.apply_mem_id " +
            "AND pl.Proj_id = al.applyProjId " +
            "AND al.Apply_id = :applyId"
            , nativeQuery = true)
    Apply getAcceptedProjectOwner(@Param("applyId") String applyId);

    @Query(value = "SELECT " +
            "apply_idx" +
            ",Mem_id" +
            ",Mem_tel" +
            ",Mem_sort" +
            ",Mem_nick" +
            ",Proj_title" +
            ",Proj_id " +
            "FROM ApplyList al, ProjectList pl, MemList ml " +
            "WHERE ml.Mem_id  = pl.Proj_mem_id " +
            "AND pl.Proj_id = :applyProjId " +
            "AND al.Apply_id = :applyId"
            , nativeQuery = true)
    Apply getAcceptedRecommandOwner(@Param("applyProjId") String applyProjId, @Param("applyId") String applyId);
}
