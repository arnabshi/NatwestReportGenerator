package com.NatwestReportGen.reportGeneratorCsv.Repository;

import com.NatwestReportGen.reportGeneratorCsv.Entity.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceRepo extends JpaRepository<Reference,Integer> {
    Reference findByRefkey1(String refkey1);
    Reference findByRefkey2(String refkey2);
}
