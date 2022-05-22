package com.cydeo.entity;


import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "projects")
@NoArgsConstructor
@Getter
@Setter
@Entity
@Where(clause = "is_deleted=false")
public class Project extends BaseEntity{

    private String projectName;

    @Column(unique = true)
    private String projectCode;

    // @NotNull
    //@ManyToOne(cascade = {CascadeType.ALL})// ask ?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User assignedManager;

    //  @NotNull
    @Column(columnDefinition = "DATE")
    private LocalDate startDate;


    //  @NotNull
    @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    //  @NotBlank
    private String projectDetail;

    @Enumerated(EnumType.STRING)
    private Status projectStatus;

//    private int completeTaskCounts;
//    private int unfinishedTaskCounts;

}
