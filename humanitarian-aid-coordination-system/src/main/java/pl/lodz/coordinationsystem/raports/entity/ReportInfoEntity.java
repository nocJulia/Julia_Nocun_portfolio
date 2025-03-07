package pl.lodz.coordinationsystem.raports.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "reports")
public class ReportInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "authority_id", nullable = false)
    private Long authorityId;

    @Column(name = "generation_date", nullable = false)
    private Date generationDate;


    public ReportInfoEntity() {
    }

    public ReportInfoEntity(Long id, Long authorityId, Date generationDate) {
        this.id = id;
        this.authorityId = authorityId;
        this.generationDate = generationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public Date getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(Date generationDate) {
        this.generationDate = generationDate;
    }
}